package com.tahrioussama.employeemanagement.controller;

import com.tahrioussama.employeemanagement.services.ExcelImportService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@RestController
@RequestMapping("/api/import")
@CrossOrigin("*")
@AllArgsConstructor
public class ExcelImportController {

    private ExcelImportService excelImportService;


    // POST http://localhost:8080/api/import/excel
    // -> Body
    // -> form-data
    // -> Key : file of Type File
    // -> Value : choisissez un fichier Excel dans votre système de fichiers
    @PostMapping("/excel")
    public ResponseEntity<String> importExcelFile(@RequestParam("file") MultipartFile file) {
        try {
            // Appel du service pour importer les données du fichier Excel
            excelImportService.importDataFromExcel(file);
            return ResponseEntity.status(HttpStatus.OK).body("Fichier Excel importé avec succès !");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de l'importation du fichier Excel.");
        }
    }
}