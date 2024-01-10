package com.example.todolist.exceptions;

import com.example.todolist.exceptions.task.TaskExceptionData;
import com.example.todolist.exceptions.task.TaskNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionAdvice {
    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<TaskExceptionData> handleCategoryNotFoundException(TaskNotFoundException exception) {
        TaskExceptionData fileEntityIssue = new TaskExceptionData();
        fileEntityIssue.setInfo(exception.getMessage());
        return new ResponseEntity<>(fileEntityIssue, HttpStatus.NOT_FOUND);
    }
}