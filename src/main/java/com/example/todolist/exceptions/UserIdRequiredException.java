package com.example.todolist.exceptions;

public class UserIdRequiredException extends BaseTaskException {
    public UserIdRequiredException() {
        super("The property user_id can't be null.");
    }
}
