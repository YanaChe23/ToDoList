package com.example.todolist.exceptions;

import com.example.todolist.exceptions.BaseTaskException;

public class ItemNotFoundException extends BaseTaskException {
    public ItemNotFoundException(String message) {
        super(message);
    }
}
