package com.example.todolist.exceptions;

public class AuthTokenParseException extends BaseTaskException {
    public AuthTokenParseException() {
        super("Failed to parse authentication token");
    }
}
