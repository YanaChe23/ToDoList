package com.example.todolist;

import com.example.todolist.controllers.TasksController;
import com.example.todolist.services.task.TaskServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class ToDoListApplicationTests {
    @Autowired
    private TasksController tasksController;
    @Autowired
    private TaskServiceImpl taskService;
    @Test
    void contextLoads() {
        assertNotNull(tasksController);
        assertNotNull(taskService);
    }
}
