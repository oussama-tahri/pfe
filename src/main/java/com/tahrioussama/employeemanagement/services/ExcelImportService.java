package com.tahrioussama.employeemanagement.services;

import com.tahrioussama.employeemanagement.exceptions.ExcelImportException;
import org.springframework.web.multipart.MultipartFile;


public interface ExcelImportService {

    void importDataFromExcel(MultipartFile file) throws ExcelImportException;
}
