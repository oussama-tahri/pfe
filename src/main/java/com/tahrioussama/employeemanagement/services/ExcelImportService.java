package com.tahrioussama.employeemanagement.services;

import com.tahrioussama.employeemanagement.entities.Absence;
import com.tahrioussama.employeemanagement.entities.Calendar;
import com.tahrioussama.employeemanagement.entities.Employee;
import com.tahrioussama.employeemanagement.repositories.AbsenceRepository;
import com.tahrioussama.employeemanagement.repositories.CalendarRepository;
import com.tahrioussama.employeemanagement.repositories.EmployeeRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

@Service
public class ExcelImportService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private AbsenceRepository absenceRepository;

    @Autowired
    private CalendarRepository calendarRepository;

    public void importDataFromExcel(MultipartFile file) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0); // Assuming data is in the first sheet


            // Iterate through each row in the sheet
            Iterator<Row> rowIterator = sheet.iterator();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                // Skip header row
                if (row.getRowNum() == 0) continue;

                // Process data from each row
                processRowData(row);
            }
        }
    }

    private void processRowData(Row row) {
        DataFormatter formatter = new DataFormatter();

        // Assuming the structure of your Excel file, map data to entities
        String firstName = formatter.formatCellValue(row.getCell(0));
        String lastName = formatter.formatCellValue(row.getCell(1));
        String employeeId = formatter.formatCellValue(row.getCell(2));
        String workType = formatter.formatCellValue(row.getCell(3));
        // You can add more fields as needed

        // Create and save Employee entity
        Employee employee = new Employee();
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setEmployeeId(employeeId);
        employee.setWorkType(workType);
        employeeRepository.save(employee);

        // Create and save Absence entity if applicable
        // Assuming absences are in the same row or in a separate sheet
        // Extract absence data from the row and save it similarly
        // For example:
        Cell absenceCell = row.getCell(4); // Assuming absence information is in the 5th column
        if (absenceCell != null) {
            String absenceInfo = formatter.formatCellValue(absenceCell);
            if (!absenceInfo.isEmpty()) {
                String[] absenceDetails = absenceInfo.split(";"); // Assuming absence details are separated by semicolon
                for (String detail : absenceDetails) {
                    String[] absenceData = detail.split(":"); // Assuming absence data is in the format "date:reason"
                    if (absenceData.length == 2) {
                        String dateString = absenceData[0];
                        String reason = absenceData[1];
                        // Parse date string to Date object
                        // Implement the logic to parse date string to a Date object based on your date format
                        // For example:
                        Date date = parseDateString(dateString);
                        // Create and save Absence entity
                        Absence absence = new Absence();
                        absence.setDate(date);
                        absence.setAbsenceType(reason);
                        absence.setEmployee(employee);
                        absenceRepository.save(absence);
                    }
                }
            }
        }

        // Create and save Calendar entity for the employee if not already exists
        Cell weekStartDateCell = row.getCell(5);
        Cell weekEndDateCell = row.getCell(6);
        Cell baseHoursCell = row.getCell(7);

        if (weekStartDateCell != null && weekEndDateCell != null && baseHoursCell != null) {
            String weekStartDateString = formatter.formatCellValue(weekStartDateCell);
            String weekEndDateString = formatter.formatCellValue(weekEndDateCell);
            // Assuming base hours are provided for each employee
            int baseHours = (int) baseHoursCell.getNumericCellValue(); // Assuming base hours is in 8th column
            // Parse date strings to Date objects
            Date weekStartDate = parseDateString(weekStartDateString);
            Date weekEndDate = parseDateString(weekEndDateString);
            // Check if calendar for this week already exists
            Calendar existingCalendar = calendarRepository.findByWeekStartDateAndWeekEndDate(weekStartDate, weekEndDate);
            if (existingCalendar == null) {
                // Create and save Calendar entity
                Calendar calendar = new Calendar();
                calendar.setWeekStartDate(weekStartDate);
                calendar.setWeekEndDate(weekEndDate);
                calendar.setBaseHours(baseHours);
                calendarRepository.save(calendar);
            }
        }
    }


    private Date parseDateString(String dateString) {
        // Format de date utilisé dans le fichier Excel
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy"); // Adapté au format de votre fichier Excel
        try {
            // Convertir la chaîne de date en objet Date
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            // Gérer l'exception si la conversion échoue
            e.printStackTrace();
            return null;
        }
    }
}
