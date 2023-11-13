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

    @GetMapping("/")
    public String redirectFromMainPage() {
        return "redirect:/all";
    }

    @GetMapping("/all")
    public String getAllTasks(Model model) {
        List<Task> tasks = taskServiceImpl.getAllTask();
        if (tasks.size() > 0) {
            model.addAttribute("tasks", tasks);
            return "list-of-tasks";
        } else {
            Task task = new Task();
            model.addAttribute("task", task);
            return "no-tasks";
        }
    }

    @GetMapping("/add")
    public String addNewTask(Model model) {
        Task task = new Task();
        model.addAttribute("task", task);
        return "task-info";
    }

    @PostMapping("/save")
    public String saveTask(@ModelAttribute("task")Task task) {
        task.setUserId(1);
        taskServiceImpl.saveTask(task);
        return "redirect:/all";
    }

    @GetMapping("/editTask")
    public String editTask(@RequestParam int id, Model model) {
        Task task = taskServiceImpl.getTask(id);
        model.addAttribute("task", task);
        return "edit-task-info";
    }

    @PutMapping("/saveEdited")
    public ResponseEntity<?> saveEditedTask(@ModelAttribute("task")Task editedTask) {
        try {
            Task task = taskServiceImpl.getTask(editedTask.getId());
            task.setDescription(editedTask.getDescription());
            task.setDeadline(editedTask.getDeadline());
            taskServiceImpl.saveTask(task);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error message");
        }
    }

    @GetMapping("/showTasksByDeadline")
    public String showTasksByDeadline(Model model, @RequestParam String buttonId) {
        try {
            Deadline deadline = Deadline.valueOf(buttonId);
            List<Task> tasks = taskServiceImpl.findTasksByDeadline(deadline);
            if (tasks.size() > 0) {
                model.addAttribute("tasks", tasks);
                return "list-of-tasks";
            } else {
                Task task = new Task();
                model.addAttribute("task", task);
                return "no-tasks";
            }
        } catch (Exception e) {
            System.err.println("Error: " + e);
            return "redirect:/all";
        }
    }

    @DeleteMapping("/deleteTask")
    public ResponseEntity<?> deleteTask(@RequestParam int id) {
        try {
            taskServiceImpl.deleteTask(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error message");
        }
    }
}


