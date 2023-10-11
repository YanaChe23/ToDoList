package com.example.todolist.controllers;

import com.example.todolist.entities.Deadline;
import com.example.todolist.entities.Task;
import com.example.todolist.services.task.TaskServiceImpl;
import com.example.todolist.services.user.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

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
        taskServiceImpl.saveTask(task);
        return "redirect:/all";
    }

    @RequestMapping("/selectTasksForToday")
    public String returnTasksForToday(Model model) {
        model.addAttribute("tasks", filterTasksByDeadline(Deadline.today));
        return "today-tasks";
    }

    @RequestMapping("/selectTasksForWeek")
    public String returnTasksForWeek(Model model) {
        model.addAttribute("tasks", filterTasksByDeadline(Deadline.week));
        return "week-tasks";
    }

    @RequestMapping("/selectTasksForSomeday")
    public String returnTasksForSomeday(Model model) {
        model.addAttribute("tasks", filterTasksByDeadline(Deadline.someday));
        return "someday-tasks";
    }

    private List<Task> filterTasksByDeadline(Deadline deadline) {
        return taskServiceImpl.findTasksByDeadline(deadline);
    }
}


