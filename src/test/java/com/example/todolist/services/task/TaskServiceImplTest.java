package com.example.todolist.services.task;

import com.example.todolist.api.v1.dto.DeadlineDto;
import com.example.todolist.api.v1.dto.PaginationDto;
import com.example.todolist.api.v1.dto.TaskRequestDto;
import com.example.todolist.api.v1.dto.TaskResponseDto;
import com.example.todolist.entities.Deadline;
import com.example.todolist.entities.Task;
import com.example.todolist.entities.User;
import com.example.todolist.exceptions.ItemNotFoundException;
import com.example.todolist.services.user.UserServiceImpl;
import io.restassured.RestAssured;
import jakarta.annotation.PostConstruct;
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
    private User user;
    private TaskRequestDto taskRequestDto;
    private PaginationDto paginationDto;

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

    @PostConstruct
    public void postConstruct() {
        user = new User(1, "Kot");
    }

    @BeforeEach
    void beforeEach() {
        RestAssured.baseURI = "http://localhost:" + port;
        taskService.deleteAll(); ;
        userService.deleteAllUsers();
        userService.saveUser(user);
        taskRequestDto = new TaskRequestDto();
        taskRequestDto.setDescription("Call Maria");
        taskRequestDto.setDeadline(DeadlineDto.TODAY);

        taskService.save(taskRequestDto);
        paginationDto = new PaginationDto();
        paginationDto.setLimit(10);
        paginationDto.setOffset(0);
    }

    @Test
    public void saveTest() {
        taskService.save(taskRequestDto);
        taskService.get(paginationDto);
        List<TaskResponseDto> listOfTasks = taskService.get(paginationDto);
        assertEquals(listOfTasks.size(), 2);
    }

    @Test
    public void getTest() {
        taskService.save(taskRequestDto);
        List<TaskResponseDto> listOfTasks = taskService.get(paginationDto);
        assertEquals(2, listOfTasks.size());
    }

    @Test
    public void getNoTasksTest() {
        taskService.deleteAll();
        List<TaskResponseDto> listOfTasks = taskService.get(paginationDto);
        assertEquals(0, listOfTasks.size());
    }

    @Test
    public void getNoPaginationTest() {
        assertThrows(
                ItemNotFoundException.class,
                () -> taskService.get(new PaginationDto())
        );
    }

    @Test
    public void getByIdTest() {
        Long newTaskId = taskService.save(taskRequestDto).getId();
        TaskResponseDto taskToFind = taskService.findById(newTaskId);
        assertNotNull(taskToFind);
    }

    @Test
    public void getByIdNoTaskFoundTest() {
        Integer outOfBoundIndex = taskService.get(paginationDto).size() + 1;
        assertThrows(ItemNotFoundException.class, ()
                -> taskService.findById(outOfBoundIndex.longValue()));
    }

    @Test
    public void getTaskByDeadlineTest() {
        taskService.save(taskRequestDto);
        assertEquals(2,
                taskService.findByDeadline(DeadlineDto.TODAY)
                        .size()
        );
    }

    @Test
    public void editTest() {
        TaskResponseDto taskToEdit = taskService.save(taskRequestDto);
        Long taskToEditId = taskToEdit.getId();

        TaskRequestDto dtoForTaskUpdate = new TaskRequestDto();
        dtoForTaskUpdate.setDescription("Call Robert");
        dtoForTaskUpdate.setDeadline(DeadlineDto.SOMEDAY);

        TaskResponseDto updatedTask = taskService.edit(taskToEditId, dtoForTaskUpdate);
        assertEquals(taskToEditId, updatedTask.getId());
        assertEquals("Call Robert", updatedTask.getDescription());
        assertEquals(Deadline.SOMEDAY.name(), updatedTask.getDeadline());
    }

    @Test
    public void editTaskNotFoundTest() {
       Integer outOfBoundsIndex = taskService.findAll().size() + 1;
       assertThrows(ItemNotFoundException.class, () -> taskService.edit(outOfBoundsIndex.longValue(),
               new TaskRequestDto()));
    }

    @Test
    public void deleteTest() {
        List<Task> allTasks = taskService.findAll();
        int amountOfTasksBeforeDelete = allTasks.size();
        assertTrue(amountOfTasksBeforeDelete > 0);

        assertEquals("Task is deleted.",
                taskService.deleteById(allTasks.get(0).getId()));
        assertEquals(amountOfTasksBeforeDelete - 1, taskService.findAll().size());
    }

    @Test
    public void deleteNoTaskFoundTest() {
        List<Task> allTasks = taskService.findAll();
        Integer outOfBoundsIndex =  allTasks.size() + 1;
        assertThrows(ItemNotFoundException.class, () -> taskService.deleteById(outOfBoundsIndex.longValue()));
    }

    @Test
    public void deleteAllTest() {
        assertFalse(taskService.findAll().isEmpty());
        taskService.deleteAll();
        assertEquals(0, taskService.findAll().size());
    }

    @Test
    public void findAllTest() {
        taskService.deleteAll();
        taskService.save(taskRequestDto);
        taskService.save(taskRequestDto);
        assertEquals(2, taskService.findAll().size());
    }
}