// добавить тестты на вывод тасок по дедлайну в service & controller
package com.example.todolist.controllers;

import com.example.todolist.dtos.TaskDTO;
import com.example.todolist.entities.Deadline;
import com.example.todolist.entities.Task;
import com.example.todolist.services.task.TaskServiceImpl;
import com.example.todolist.services.user.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TasksController {
    @Autowired
    UserServiceImpl userServiceImpl;
    @Autowired
    TaskServiceImpl taskServiceImpl;

    @PostMapping("/tasks")
    public Task saveTask(@RequestBody TaskDTO task) {
        return taskServiceImpl.saveTask(task);
    }

    @GetMapping("/tasks")
    public List<Task> getAllTasks() {
        return taskServiceImpl.getAllTask();
    }

    @GetMapping("/tasks/{id}")
    public Task getTask(@PathVariable  int id) {
        return taskServiceImpl.getTask(id);
    }

    @GetMapping("/tasks/deadline/{deadline}")
    public List<Task> showTasksByDeadline(@PathVariable Deadline deadline) {
        return taskServiceImpl.getTasksByDeadline(deadline);
    }

    @PatchMapping("/tasks/{id}")
    public Task editTask(@RequestBody TaskDTO task, @PathVariable int id) {
        return taskServiceImpl.editTask(task, id);
    }

    @DeleteMapping("/tasks/{id}")
    public String deleteTask(@PathVariable  int id) {
        return taskServiceImpl.deleteTask(id);
    }
}


