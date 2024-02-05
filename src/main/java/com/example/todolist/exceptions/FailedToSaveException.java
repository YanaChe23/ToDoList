package com.example.todolist.exceptions;

public class FailedToSaveException extends BaseTaskException {
    int test;
    public FailedToSaveException(String message) {
        super(message);
    }
}
