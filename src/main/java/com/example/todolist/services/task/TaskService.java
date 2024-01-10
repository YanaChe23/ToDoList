package com.example.todolist.services.task;

import com.example.todolist.dtos.TaskDTO;
import com.example.todolist.entities.Deadline;
import com.example.todolist.entities.Task;
import com.example.todolist.entities.User;

import java.util.List;

public interface TaskService {
    List<Task> getAllTask();
    Task saveTask(TaskDTO task);
//    Task editTask(Task task);
    Task getTask(int id);
    String deleteTask(int id);
    void deleteAllTasks();
    List<Task> findAllTasks();
    List<Task> findTasksByDeadline(Deadline deadline);

}
