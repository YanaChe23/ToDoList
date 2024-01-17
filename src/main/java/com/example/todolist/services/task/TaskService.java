package com.example.todolist.services.task;

import com.example.todolist.dtos.TaskDTO;
import com.example.todolist.entities.Deadline;
import com.example.todolist.entities.Task;
import com.example.todolist.entities.User;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface TaskService {
    List<Task> getAllTask();
    Task saveTask(TaskDTO task);
    Task getTask(int id);

    Task editTask(TaskDTO task, int id);
    String deleteTask(int id);
    void deleteAllTasks();
//    List<Task> findTasksByDeadline(Deadline deadline);

}
