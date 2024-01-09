package com.example.todolist.controllers;

import com.example.todolist.entities.Deadline;
import com.example.todolist.entities.Task;
import com.example.todolist.services.task.TaskServiceImpl;
import com.example.todolist.services.user.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class TasksController {
    @Autowired
    UserServiceImpl userServiceImpl;
    @Autowired
    TaskServiceImpl taskServiceImpl;

    @GetMapping("/allTasks")
    public List<Task> getAllTasks() {
        return taskServiceImpl.getAllTask();
    }


    // TODO
    @PostMapping("/addTask")
    public Task saveTask(@RequestBody Task task) {
        return taskServiceImpl.saveTask(task);
    }

    @PatchMapping("/editTask")
    public Task saveEditedTask(@RequestBody Task task) {
        return taskServiceImpl.editTask(task);
    }

    @DeleteMapping("/deleteTask")
    public String deleteTask(@RequestParam int id) {
        taskServiceImpl.deleteTask(id);
        return "Deleted";
    }

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


