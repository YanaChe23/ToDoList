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
import org.springframework.web.bind.annotation.RequestParam;

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
            return "all-tasks";
        } else {
            Task task = new Task();
            model.addAttribute("task", task);
            return "no-tasks";
        }
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
        return pickPageToReturn(model, Deadline.today, "today-tasks");
    }

    @RequestMapping("/selectTasksForWeek")
    public String returnTasksForWeek(Model model) {
        return pickPageToReturn(model, Deadline.week, "week-tasks");
    }

    @RequestMapping("/selectTasksForSomeday")
    public String returnTasksForSomeday(Model model) {
        return pickPageToReturn(model, Deadline.someday, "someday-tasks");
    }

    private String pickPageToReturn(Model model, Deadline deadline, String pageWithTasks) {
        List<Task> tasks = filterTasksByDeadline(deadline);
        if (tasks.size() > 0) {
            model.addAttribute("tasks", tasks);
            return pageWithTasks;
        } else {
            Task task = new Task();
            model.addAttribute("task", task);
            return "no-tasks_by_deadline";
        }
    }

    private List<Task> filterTasksByDeadline(Deadline deadline) {
        return taskServiceImpl.findTasksByDeadline(deadline);
    }

    @GetMapping("/edit")
    public String editTask(@RequestParam Integer id, Model model) {
        Task task = taskServiceImpl.getTask(id);
        model.addAttribute("task", task);
        return "edit-task-info";
    }

    @GetMapping("/saveEdited")
    public String saveEditedTask(@ModelAttribute("task")Task editedTask) {
        Task task = taskServiceImpl.getTask(editedTask.getId());
        task.setDescription(editedTask.getDescription());
        task.setDeadline(editedTask.getDeadline());
        taskServiceImpl.saveTask(task);
        return "redirect:/all";
    }
}


