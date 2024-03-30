package com.tahrioussama.employeemanagement.services;

import com.tahrioussama.employeemanagement.repositories.EmployeeRepository;
import com.tahrioussama.employeemanagement.repositories.PresenceRepository;
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

    private ExcelImportServiceImpl excelImportService;

    @BeforeEach
    void setUp() {
        excelImportService = new ExcelImportServiceImpl(employeeRepository,presenceRepository);
    }

    @Test
    public void testImportDataFromExcel() {
        // Path to the Excel file in the resources directory
        String filePath = "\\EMG_Planning Présence sur Site_V1.0-01032024.xlsx";

        // Load the file as a MultipartFile
        MultipartFile multipartFile = createMultipartFile(filePath);

        try {
            excelImportService.importDataFromExcel(multipartFile);
        } catch (IOException e) {
            e.printStackTrace();
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