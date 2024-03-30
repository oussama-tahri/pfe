package com.tahrioussama.employeemanagement.exceptions;

public class NoSquadAssignedException extends RuntimeException {
    public NoSquadAssignedException(String message) {
        super(message);
    }
}
