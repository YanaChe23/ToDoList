package com.example.todolist.controllers;

import com.example.todolist.entities.Task;
import com.example.todolist.entities.User;
import com.example.todolist.services.task.TaskServiceImpl;
import com.example.todolist.services.user.UserServiceImpl;
import io.restassured.RestAssured;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import com.example.todolist.entities.Deadline;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class TasksControllerTest {
    @Autowired
    private TaskServiceImpl taskService;
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private TasksController tasksController;
    @Autowired
    private MockMvc mockMvc;
    @LocalServerPort
    private Integer port;
    private String baseURI;

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
        Task emailTask = new Task(1, "Send an e-mail", Deadline.today);
        userService.saveUser(user);
        taskService.saveTask(emailTask);
    }

    @Test
    public void controllerLoadsTest() {
        assertThat(tasksController).isNotNull();
    }

    @Test
    public void redirectFromMainPagTest() throws Exception {
        this.mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/all"));
    }

    @Test
    public void showAllTasksTest() throws Exception {
        Document doc = Jsoup.connect(baseURI + "/all").get();
        Elements elements = doc.getElementsByTag("td");

        assertEquals(elements.size(), 2);
        assertEquals(elements.get(0).ownText(), "Send an e-mail");
        assertEquals(elements.get(1).ownText(), "Today");
    }

    @Test
    public void addTaskTest() throws Exception {
        this.mockMvc.perform(get("/add"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("task-info"));
    }

    @Test
    public void saveTaskTest() throws Exception {
        Document doc = Jsoup.connect(baseURI + "/save?description=Call Bob&deadline=week").get();
        Elements elements = doc.getElementsByTag("td");
        // check that there are 4 elements in td tag of html page: 2 tasks and 2 deadlines
        assertEquals(elements.size(), 4);
        assertEquals(elements.get(0).ownText(), "Send an e-mail");
        assertEquals(elements.get(1).ownText(), "Today");
        assertEquals(elements.get(2).ownText(), "Call Bob");
        assertEquals(elements.get(3).ownText(), "Week");
    }

    @Test
    public void showTasksByTodayDeadlineTest() throws IOException {
        Task callTask = new Task(1, "Call Maria", Deadline.week);
        Task dantistTask = new Task(1, "Make an appointment with Dr.Robertson", Deadline.week);
        Task presentTask =  new Task(1, "Buy presents", Deadline.someday);
        taskService.saveTask(callTask);
        taskService.saveTask(dantistTask);
        taskService.saveTask(presentTask);

        Document doc = Jsoup.connect(baseURI + "/selectTasksForToday").get();
        Elements elements = doc.getElementsByTag("td");

        assertEquals(elements.size(), 2);
        assertEquals(elements.get(0).ownText(), "Send an e-mail");
        assertEquals(elements.get(1).ownText(), "Today");
    }

    @Test
    public void showTasksByWeekDeadlineTest() throws IOException {
        Task callTask = new Task(1, "Call Maria", Deadline.week);
        Task dantistTask = new Task(1, "Make an appointment with Dr.Robertson", Deadline.week);
        Task presentTask =  new Task(1, "Buy presents", Deadline.someday);
        taskService.saveTask(callTask);
        taskService.saveTask(dantistTask);
        taskService.saveTask(presentTask);

        Document doc = Jsoup.connect(baseURI + "/selectTasksForWeek").get();
        Elements elements = doc.getElementsByTag("td");
        assertEquals(elements.size(), 4);
        assertEquals(elements.get(0).ownText(), "Call Maria");
        assertEquals(elements.get(1).ownText(), "Week");
        assertEquals(elements.get(2).ownText(), "Make an appointment with Dr.Robertson");
        assertEquals(elements.get(3).ownText(), "Week");
    }

    @Test
    public void showTasksBySomedayDeadlineTest() throws IOException {
        Task callTask = new Task(1, "Call Maria", Deadline.week);
        Task dantistTask = new Task(1, "Make an appointment with Dr.Robertson", Deadline.week);
        Task presentTask =  new Task(1, "Buy presents", Deadline.someday);
        taskService.saveTask(callTask);
        taskService.saveTask(dantistTask);
        taskService.saveTask(presentTask);

        Document doc = Jsoup.connect(baseURI + "/selectTasksForSomeday").get();
        Elements elements = doc.getElementsByTag("td");
        assertEquals(elements.size(), 2);
        assertEquals(elements.get(0).ownText(), "Buy presents");
        assertEquals(elements.get(1).ownText(), "Someday");
    }
    @Test
    public void noTaskFoundTest() throws Exception {
        taskService.deleteAllTasks();
        this.mockMvc.perform(get("/all"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("no-tasks"));
    }

    @Test
    public void noTaskFoundByDeadlineTest() throws Exception {
        taskService.deleteAllTasks();
        this.mockMvc.perform(get("/selectTasksForSomeday"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("no-tasks_by_deadline"));
    }
}