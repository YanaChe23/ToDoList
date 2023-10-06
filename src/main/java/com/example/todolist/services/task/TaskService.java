package com.example.todolist.services.task;

import com.example.todolist.entities.Task;
import com.example.todolist.entities.User;

import java.util.List;

public interface TaskService {
    public List<Task> getAllTask();
    public void saveTask(Task Task);
    public Task getTask(int id);
    public void deleteTask(int id);
}
