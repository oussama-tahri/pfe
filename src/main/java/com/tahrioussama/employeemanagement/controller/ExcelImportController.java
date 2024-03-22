package com.tahrioussama.employeemanagement.controller;

import com.tahrioussama.employeemanagement.services.ExcelImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@RestController
@RequestMapping("/api/import")
public class ExcelImportController {

    @Autowired
    private ExcelImportService excelImportService;


    // POST http://localhost:8080/api/import/excel
    // -> Body
    // -> form-data
    // -> Key : file of Type File ->
    // Value : choisissez un fichier Excel dans votre système de fichiers
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



//            5. Sélectionnez l'onglet "Body".
//            6. Choisissez l'option "form-data".
//            7. Ajoutez une clé avec le nom `file`.
//            8. Cliquez sur "Select Files" à côté de la clé `file` et choisissez un fichier Excel dans votre système de fichiers.
//            9. Cliquez sur "Send" pour envoyer la requête.
