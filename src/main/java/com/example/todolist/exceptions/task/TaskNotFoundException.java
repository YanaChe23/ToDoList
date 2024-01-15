package com.example.todolist.exceptions.task;

import com.example.todolist.exceptions.BaseTaskException;

public class TaskNotFoundException extends BaseTaskException {
    private static final String errorMessage = "Could not find a task with id ";
    int test;
    public TaskNotFoundException(int id) {
        super(errorMessage + id + ".");
    }
}
