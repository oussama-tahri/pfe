package com.tahrioussama.employeemanagement.services;

import com.tahrioussama.employeemanagement.repositories.EmployeeRepository;
import com.tahrioussama.employeemanagement.repositories.PresenceRepository;
import com.tahrioussama.employeemanagement.services.ExcelImportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@SpringBootTest
public class ExcelImportServiceTest {

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private PresenceRepository presenceRepository;

    private ExcelImportService excelImportService;

    @BeforeEach
    void setUp() {
        excelImportService = new ExcelImportService(employeeRepository,presenceRepository);
    }

    @Test
    public void testImportDataFromExcel() {
        // Path to the Excel file in the resources directory
        String filePath = "\\EMG_Planning Présence sur Site_V1.0-01032024.xlsx";

        // Load the file as a MultipartFile
        MultipartFile multipartFile = createMultipartFile(filePath);

        // Call the method under test and assert the behavior
        try {
            excelImportService.importDataFromExcel(multipartFile);
            // Add assertions or further processing as needed
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception
        }
    }

    private MultipartFile createMultipartFile(String filePath) {
        try {
            // Load the file as a resource from the classpath
            ClassPathResource resource = new ClassPathResource(filePath);
            InputStream inputStream = resource.getInputStream();

            // Create a MultipartFile object from the input stream
            return new MockMultipartFile("file", resource.getFilename(), "application/vnd.ms-excel", inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}