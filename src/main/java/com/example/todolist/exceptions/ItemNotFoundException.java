package com.example.todolist.exceptions;

public class ItemNotFoundException extends BaseTaskException {
    public ItemNotFoundException(String message) {
        super(message);
    }
}
