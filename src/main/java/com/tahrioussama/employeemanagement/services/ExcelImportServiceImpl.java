package com.tahrioussama.employeemanagement.services;

import com.tahrioussama.employeemanagement.entities.Employee;
import com.tahrioussama.employeemanagement.entities.Presence;
import com.tahrioussama.employeemanagement.enums.PresenceStatus;
import com.tahrioussama.employeemanagement.exceptions.ExcelImportException;
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

    private final EmployeeRepository employeeRepository;
    private final PresenceRepository presenceRepository;

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

        // Assuming the structure of your Excel file, map data to entities
        String resourceName = formatter.formatCellValue(row.getCell(0));
        String site = formatter.formatCellValue(row.getCell(1));
        String tribe = formatter.formatCellValue(row.getCell(2));
        String squad = formatter.formatCellValue(row.getCell(3));
        String commentaire = formatter.formatCellValue(row.getCell(4));

        // Create and save Employee entity
        Employee employee = new Employee();
        employee.setResourceName(resourceName.toLowerCase());
        employee.setSite(site);
        employee.setTribe(tribe);
        employee.setSquad(squad);
        employee.setCommentaire(commentaire);
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
        List<PresenceStatus> presenceList = new ArrayList<>();
        for (int i = 5; i < row.getLastCellNum(); i++) {
            String presenceValue = formatter.formatCellValue(row.getCell(i));
            if (!presenceValue.isEmpty()) {
                // Parse presence value as PresenceStatus enum
                PresenceStatus presenceStatus = presenceValue.equals("1") ? PresenceStatus.PRESENT : PresenceStatus.ABSENT;
                presenceList.add(presenceStatus);
            }
        }

        // Limit the presence list to the first 21 elements
        if (presenceList.size() > 21) {
            presenceList = presenceList.subList(0, 21);
        }

        // Create and save Presence entities with dates mapped to presence values
        for (int i = 0; i < dates.size(); i++) {
            LocalDate date = dates.get(i);
            PresenceStatus presenceStatus = i < presenceList.size() ? presenceList.get(i) : PresenceStatus.ABSENT; // Default to ABSENT if no presence value is provided
            Presence presence = new Presence();
            presence.setEmployee(employee);
            presence.setEmployeeName(resourceName);
            presence.setDate(date);
            presence.setPresent(presenceStatus);
            try {
                presenceRepository.save(presence);
            } catch (Exception e) {
                throw new ExcelImportException("Failed to save presence data.", e);
            }
        }
    }
}