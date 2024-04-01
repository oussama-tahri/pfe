package com.tahrioussama.employeemanagement.services;

import com.tahrioussama.employeemanagement.dtos.EmployeeDTO;
import com.tahrioussama.employeemanagement.dtos.PresenceDTO;
import com.tahrioussama.employeemanagement.entities.Employee;
import com.tahrioussama.employeemanagement.entities.Presence;
import com.tahrioussama.employeemanagement.enums.PresenceStatus;
import com.tahrioussama.employeemanagement.exceptions.ExcelImportException;
import com.tahrioussama.employeemanagement.mappers.EmployeeMapper;
import com.tahrioussama.employeemanagement.mappers.PresenceMapper;
import com.tahrioussama.employeemanagement.repositories.EmployeeRepository;
import com.tahrioussama.employeemanagement.repositories.PresenceRepository;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
@AllArgsConstructor
public class ExcelImportServiceImpl implements ExcelImportService {

    private EmployeeRepository employeeRepository;
    private PresenceRepository presenceRepository;

    private EmployeeMapper employeeMapper;
    private PresenceMapper presenceMapper;

    @Override
    public void importDataFromExcel(MultipartFile file) throws ExcelImportException {
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0); // Assuming data is in the first sheet

            // Iterate through each row in the sheet
            Iterator<Row> rowIterator = sheet.iterator();
            int rowCount = 0;
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                rowCount++;

                // Skip first 4 rows
                if (rowCount <= 4) continue;

                // Process data from each row
                processRowData(row);
            }
        } catch (IOException e) {
            throw new ExcelImportException("An error occurred while reading the Excel file.", e);
        }
    }

    private void processRowData(Row row) throws ExcelImportException {
        DataFormatter formatter = new DataFormatter();

        // Check if the row is empty or doesn't contain any relevant data
        boolean isEmptyRow = true;
        for (int i = 0; i < row.getLastCellNum(); i++) {
            String cellValue = formatter.formatCellValue(row.getCell(i));
            if (!cellValue.isEmpty()) {
                isEmptyRow = false;
                break;
            }
        }

        if (isEmptyRow) {
            // Skip processing empty rows
            return;
        }

        // Assuming the structure of your Excel file, map data to DTOs
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setResourceName(formatter.formatCellValue(row.getCell(0)));
        employeeDTO.setSite(formatter.formatCellValue(row.getCell(1)));
        employeeDTO.setTribe(formatter.formatCellValue(row.getCell(2)));
        employeeDTO.setSquad(formatter.formatCellValue(row.getCell(3)));
        employeeDTO.setCommentaire(formatter.formatCellValue(row.getCell(4)));

        // Map DTO to entity and save
        Employee employee = employeeMapper.dtoToEmployee(employeeDTO);
        try {
            employeeRepository.save(employee);
        } catch (Exception e) {
            throw new ExcelImportException("Failed to save employee data.", e);
        }

        // Generate dates for the month of March 2024
        List<LocalDate> dates = new ArrayList<>();
        LocalDate startDate = LocalDate.of(2024, Month.MARCH, 1);
        LocalDate endDate = LocalDate.of(2024, Month.MARCH, 29);

        // Skip weekends (Saturday and Sunday)
        while (!startDate.isAfter(endDate)) {
            if (startDate.getDayOfWeek() != DayOfWeek.SATURDAY && startDate.getDayOfWeek() != DayOfWeek.SUNDAY) {
                dates.add(startDate);
            }
            startDate = startDate.plusDays(1);
        }

        // Extract presence data horizontally and map it to dates
        List<PresenceDTO> presenceDTOList = new ArrayList<>();
        for (int i = 5; i < row.getLastCellNum(); i++) {
            String presenceValue = formatter.formatCellValue(row.getCell(i));
            if (!presenceValue.isEmpty()) {
                // Parse presence value as PresenceStatus enum
                PresenceDTO presenceDTO = new PresenceDTO();
                presenceDTO.setDate(dates.get(i - 5)); // i - 5 to map to correct date
                presenceDTO.setPresent(presenceValue.equals("1") ? PresenceStatus.PRESENT : PresenceStatus.ABSENT);
                presenceDTO.setEmployeeName(employeeDTO.getResourceName());
                presenceDTOList.add(presenceDTO);
            }
        }

        // Limit the presence list to the first 21 elements
        if (presenceDTOList.size() > 21) {
            presenceDTOList = presenceDTOList.subList(0, 21);
        }

        // Map DTO to entity and save
        List<Presence> presenceList = new ArrayList<>();
        for (PresenceDTO presenceDTO : presenceDTOList) {
            Presence presence = presenceMapper.dtoToPresence(presenceDTO);
            presence.setEmployee(employee);
            presenceList.add(presence);
        }
        try {
            presenceRepository.saveAll(presenceList);
        } catch (Exception e) {
            throw new ExcelImportException("Failed to save presence data.", e);
        }
    }
}