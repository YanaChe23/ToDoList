package com.example.todolist.services.task;

import com.example.todolist.entities.Deadline;
import com.example.todolist.entities.Task;
import com.example.todolist.entities.User;

import java.util.List;

public interface TaskService {
    List<Task> getAllTask();
    Task saveTask(Task Task);
    Task editTask(Task task);
    Task getTask(int id);
    void deleteTask(int id);
    void deleteAllTasks();
    List<Task> findAllTasks();
    List<Task> findTasksByDeadline(Deadline deadline);

}
