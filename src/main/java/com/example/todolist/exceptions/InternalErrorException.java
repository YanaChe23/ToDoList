package com.example.todolist.exceptions;

public class InternalErrorException extends BaseTaskException {
    public InternalErrorException() {
        super("Something went wrong. Please try later.");
    }
}
