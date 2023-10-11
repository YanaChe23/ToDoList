package com.example.todolist.services.task;

import com.example.todolist.entities.Deadline;
import com.example.todolist.entities.Task;
import com.example.todolist.entities.User;
import com.example.todolist.services.user.UserServiceImpl;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TaskServiceImplTest {
    @Autowired
    TaskServiceImpl taskService;
    @Autowired
    UserServiceImpl userService;
    @LocalServerPort
    private Integer port;
    String baseURI;

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:15-alpine"
    );

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @BeforeEach
    void setUp() {
        baseURI = "http://localhost:" + port;
        RestAssured.baseURI = baseURI;
        taskService.deleteAllTasks();
        userService.deleteAllUsers();
        User user = new User(1, "Vlad");
        userService.saveUser(user);
        Task task = new Task(1, "Send an e-mail", Deadline.today);
        taskService.saveTask(task);
    }

    @Test
    public void deleteAndSaveTaskTest() {
        taskService.deleteAllTasks();
        Task task = new Task(1, "Send an e-mail", Deadline.today);
        taskService.saveTask(task);

        List<Task> listOfTasks = taskService.findAllTasks();
        assertEquals(listOfTasks.size(), 1);
    }

    @Test
    public void getTaskTest() {
        List<Task> listOfTasks = taskService.findAllTasks();
        int currentIdOfTask = listOfTasks.get(0).getId();
        Task task = taskService.getTask(currentIdOfTask);
        assertEquals(task.getDescription(), "Send an e-mail");
    }
}