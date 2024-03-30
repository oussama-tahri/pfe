package com.tahrioussama.employeemanagement.controller;

import com.tahrioussama.employeemanagement.exceptions.ExcelImportException;
import com.tahrioussama.employeemanagement.services.ExcelImportServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/import")
@CrossOrigin("*")
@AllArgsConstructor
public class ExcelImportController {

    private ExcelImportServiceImpl excelImportService;


    // POST http://localhost:8080/api/import/excel
    // -> Body
    // -> form-data
    // -> Key : file of Type File
    // -> Value : choisissez un fichier Excel dans votre système de fichiers
    @PostMapping("/excel")
    public ResponseEntity<String> importExcelFile(@RequestParam("file") MultipartFile file) {
        try {
            excelImportService.importDataFromExcel(file);
            return ResponseEntity.ok("Fichier Excel importé avec succès !");
        } catch (ExcelImportException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}