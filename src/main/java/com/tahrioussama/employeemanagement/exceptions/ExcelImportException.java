package com.tahrioussama.employeemanagement.exceptions;

public class ExcelImportException extends RuntimeException {
    public ExcelImportException(String message, Exception cause) {
        super(message, cause);
    }
}
