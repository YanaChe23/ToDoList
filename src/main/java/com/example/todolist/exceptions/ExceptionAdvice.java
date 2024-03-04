package com.example.todolist.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionAdvice {
    @ExceptionHandler(UserIdRequiredException.class)
    public ResponseEntity<ExceptionData> handleUserIdRequiredException(UserIdRequiredException exception) {
        ExceptionData fileEntityIssue = new ExceptionData();
        fileEntityIssue.setInfo(exception.getMessage());
        return new ResponseEntity<>(fileEntityIssue, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AuthTokenParseException.class)
    public ResponseEntity<ExceptionData> handleAuthTokenParseException(AuthTokenParseException exception) {
        ExceptionData fileEntityIssue = new ExceptionData();
        fileEntityIssue.setInfo(exception.getMessage());
        return new ResponseEntity<>(fileEntityIssue, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FailedToSaveException.class)
    public ResponseEntity<ExceptionData> handleFailedToSaveException(FailedToSaveException exception) {
        ExceptionData fileEntityIssue = new ExceptionData();
        fileEntityIssue.setInfo(exception.getMessage());
        return new ResponseEntity<>(fileEntityIssue, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<ExceptionData> handleItemNotFoundExceptionException(ItemNotFoundException exception) {
        ExceptionData fileEntityIssue = new ExceptionData();
        fileEntityIssue.setInfo(exception.getMessage());
        return new ResponseEntity<>(fileEntityIssue, HttpStatus.NOT_FOUND);
    }
}