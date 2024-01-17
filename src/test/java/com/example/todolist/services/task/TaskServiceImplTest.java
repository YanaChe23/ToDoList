package com.example.todolist.services.task;

import com.example.todolist.dtos.TaskDTO;
import com.example.todolist.entities.Deadline;
import com.example.todolist.entities.Task;
import com.example.todolist.entities.User;
import com.example.todolist.exceptions.task.TaskNotFoundException;
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
        User user = new User(1, "Dior");
        userService.saveUser(user);
        TaskDTO taskDTO = new TaskDTO("Send an e-mail", Deadline.today);
        taskService.saveTask(taskDTO);
    }

    @Test
    public void saveTaskTest() {
        TaskDTO taskDTO = new TaskDTO("Send an e-mail", Deadline.today);
        taskService.saveTask(taskDTO);

        List<Task> listOfTasks = taskService.getAllTask();
        assertEquals(listOfTasks.size(), 2);
    }

    @Test
    public void getAllTasksTest() {
        TaskDTO calling = new TaskDTO("Call Maria", Deadline.week);
        TaskDTO mediation = new TaskDTO("Meditate", Deadline.today);
        taskService.saveTask(calling);
        taskService.saveTask(mediation);

        List<Task> tasks = taskService.getAllTask();
        assertEquals(tasks.size(), 3);
    }

    @Test
    public void getTaskSuccessTest() {
        List<Task> listOfTasks = taskService.getAllTask();
        int currentIdOfTask = listOfTasks.get(0).getId();
        Task task = taskService.getTask(currentIdOfTask);
        assertEquals(task.getDescription(), "Send an e-mail");
    }

    @Test
    public void getTaskFailTest() {
        int outOfBoundIndex = taskService.getAllTask().size() + 1;

        assertThrows(TaskNotFoundException.class, ()
                -> taskService.getTask(outOfBoundIndex));
    }

    @Test
    public void editTaskTest() {
        int taskToUpdateIndex = taskService.getAllTask().get(0).getId();
        assertEquals(taskService.getTask(taskToUpdateIndex).getDescription(), "Send an e-mail");

        TaskDTO taskDTO = new TaskDTO("Send an e-mail to Alex", Deadline.today);
        taskService.editTask(taskDTO, taskToUpdateIndex);
        assertEquals(taskService.getTask(taskToUpdateIndex).getDescription(), "Send an e-mail to Alex");
    }

    @Test
    public void deleteTaskSuccessTest() {
        List<Task> allTasks = taskService.getAllTask();
        int amountOfTasksBeforeDelete = allTasks.size();
        int taskToDeleteIndex = allTasks.get(0).getId();

        String result = taskService.deleteTask(taskToDeleteIndex);
        int amountOfTasksAfterDelete = taskService.getAllTask().size();

        assertEquals(amountOfTasksBeforeDelete - 1, amountOfTasksAfterDelete);
        assertEquals("Task with id=" + taskToDeleteIndex + " is deleted.", result);
    }

    @Test
    public void deleteTaskNoTaskFoundTest() {
        List<Task> allTasks = taskService.getAllTask();
        int amountOfTasksBeforeDelete = allTasks.size();
        int taskToDeleteIndex = amountOfTasksBeforeDelete + 1;

        assertThrows(TaskNotFoundException.class, ()->{
            taskService.deleteTask(taskToDeleteIndex);
        });

        int amountOfTasksAfterDelete = taskService.getAllTask().size();
        assertEquals(amountOfTasksBeforeDelete, amountOfTasksAfterDelete);
    }

    @Test
    public void deleteAllTasksTest() {
        taskService.deleteAllTasks();
        List<Task> remainingTasks = taskService.getAllTask();
        assertEquals(0, remainingTasks.size());
    }


//    @Test
//    public void findTasksByDeadlineTest() {
//        TaskDTO callTask = new TaskDTO("Call Bob", Deadline.week);
//        TaskDTO dentistTask = new TaskDTO( "Make an appointment with Dr.Robertson", Deadline.week);
//        TaskDTO presentTask =  new TaskDTO("Buy presents", Deadline.someday);
//        taskService.saveTask(callTask);
//        taskService.saveTask(dentistTask);
//        taskService.saveTask(presentTask);
//        assertEquals(taskService.findTasksByDeadline(Deadline.today).size(), 1);
//        assertEquals(taskService.findTasksByDeadline(Deadline.week).size(), 2);
//        assertEquals(taskService.findTasksByDeadline(Deadline.someday).size(), 1);
//    }

    @Test
    void testTaskNotFoundExceptionMessage() {
        TaskNotFoundException exception = assertThrows(
                TaskNotFoundException.class,
                () -> { throw new TaskNotFoundException(1); }
        );
        assertEquals("Could not find a task with id 1.",  exception.getMessage());
    }
}