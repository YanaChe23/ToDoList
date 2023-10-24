package com.example.todolist.exceptions.task;

import com.example.todolist.exceptions.BaseTaskException;

public class TaskNotFoundException extends BaseTaskException {
    public TaskNotFoundException(String message) {
        super(message);
    }
}
