package com.example.todolist.controllers;

import com.example.todolist.dtos.TaskDTO;
import com.example.todolist.entities.Task;
import com.example.todolist.services.task.TaskServiceImpl;
import com.example.todolist.services.user.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
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

    @DeleteMapping("/tasks/{id}")
    public String deleteTask(@PathVariable  int id) {
        return taskServiceImpl.deleteTask(id);
    }

    @PatchMapping("/tasks/{id}")
    public Task saveEditedTask(@RequestBody TaskDTO task, @PathVariable int id) {
        return taskServiceImpl.editTask(task, id);
    }
//gi


//    @GetMapping("/showTasksByDeadline")
//    public String showTasksByDeadline(Model model, @RequestParam String buttonId) {
//        try {
//            Deadline deadline = Deadline.valueOf(buttonId);
//            List<Task> tasks = taskServiceImpl.findTasksByDeadline(deadline);
//            if (tasks.size() > 0) {
//                model.addAttribute("tasks", tasks);
//                return "list-of-tasks";
//            } else {
//                Task task = new Task();
//                model.addAttribute("task", task);
//                return "no-tasks";
//            }
//        } catch (Exception e) {
//            System.err.println("Error: " + e);
//            return "redirect:/all";
//        }
//    }

}


