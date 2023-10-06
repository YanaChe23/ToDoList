package com.example.todolist.controllers;

import com.example.todolist.entities.Task;
import com.example.todolist.services.task.TaskServiceImpl;
import com.example.todolist.services.user.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TasksController {
    @Autowired
    UserServiceImpl userServiceImpl;
    @Autowired
    TaskServiceImpl taskServiceImpl;

    @GetMapping("/test")
    public String showMainPage() {
        Task task = new Task();
        task.setUserId(1);
        task.setDescription("Play ps");
        task.setTerm(2);
        System.out.println("!!!!!!!! " + task);
        taskServiceImpl.saveTask(task);
        return "greeting";
    }

    @GetMapping("/all")
    public String getAllTasks(Model model) {
        model.addAttribute("tasks", taskServiceImpl.getAllTask());
        return "all-tasks";
    }

    @RequestMapping("/add")
    public String addNewTask(Model model) {
        Task task = new Task();
        model.addAttribute("task", task);
        return "task-info";
    }

    @RequestMapping("/save")
    public String saveTask(@ModelAttribute("task")Task task) {
        task.setUserId(1);
        System.out.println("!!!!!!!! " + task);
        taskServiceImpl.saveTask(task);
        return "redirect:/all";
    }
}


