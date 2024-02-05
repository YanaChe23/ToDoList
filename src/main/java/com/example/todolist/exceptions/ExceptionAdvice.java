package com.example.todolist.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionAdvice {
    @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<ExceptionData> handleUserNotFoundException(ItemNotFoundException exception) {
        ExceptionData fileEntityIssue = new ExceptionData();
        fileEntityIssue.setInfo(exception.getMessage());
        return new ResponseEntity<>(fileEntityIssue, HttpStatus.NOT_FOUND);
    }
}