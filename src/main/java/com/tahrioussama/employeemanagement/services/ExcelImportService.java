package com.tahrioussama.employeemanagement.services;

import com.tahrioussama.employeemanagement.entities.Employee;
import com.tahrioussama.employeemanagement.entities.Presence;
import com.tahrioussama.employeemanagement.repositories.PresenceRepository;
import com.tahrioussama.employeemanagement.repositories.EmployeeRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.Iterator;

@Service
public class ExcelImportService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PresenceRepository presenceRepository;

    public void importDataFromExcel(MultipartFile file) throws IOException {
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
                processRowData(row, sheet);
            }
        }
    }

    private void processRowData(Row row, Sheet sheet) {
        DataFormatter formatter = new DataFormatter();

        // Assuming the structure of your Excel file, map data to entities
        String resourceName = formatter.formatCellValue(row.getCell(0));
        String site = formatter.formatCellValue(row.getCell(1));
        String tribe = formatter.formatCellValue(row.getCell(2));
        String squad = formatter.formatCellValue(row.getCell(3));
        String commentaire = formatter.formatCellValue(row.getCell(4));

        // Create and save Employee entity
        Employee employee = new Employee();
        employee.setResourceName(resourceName);
        employee.setSite(site);
        employee.setTribe(tribe);
        employee.setSquad(squad);
        employee.setCommentaire(commentaire);
        employee = employeeRepository.save(employee); // Save and retrieve generated ID

        // process the presence data
        // Now, process the presence data starting from the 5th row
        int rowIndex = 4; // 0-based index
        Row presenceRow = sheet.getRow(rowIndex); // Assuming the presence data is in the 5th row

        // Assuming attendance starts after the commentaire column
        int columnIndex = 5; // Assuming attendance starts after the 5th column

        LocalDate startDate = LocalDate.of(2024, Month.MARCH, 1);
        LocalDate endDate = LocalDate.of(2024, Month.MARCH, 29);

        while (startDate.isBefore(endDate) || startDate.isEqual(endDate)) {
            if (startDate.getDayOfWeek() != DayOfWeek.SATURDAY && startDate.getDayOfWeek() != DayOfWeek.SUNDAY) {
                Cell cell = presenceRow.getCell(columnIndex);

                // Assuming '1' means present and '0' means absent
                String cellValue = formatter.formatCellValue(cell);
                boolean isPresent = "1".equals(cellValue.trim());

                // Create and save Presence entity
                Presence presence = new Presence();
                presence.setEmployee(employee);
                presence.setDate(startDate);
                presence.setPresentOnSite(isPresent);
                presenceRepository.save(presence);

                columnIndex++;
            }
            startDate = startDate.plusDays(1);
        }
    }
}