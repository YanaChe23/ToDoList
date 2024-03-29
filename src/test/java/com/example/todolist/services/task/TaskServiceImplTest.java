package com.example.todolist.services.task;

import com.example.todolist.api.v1.dto.*;
import com.example.todolist.entities.Deadline;
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
    private TaskServiceImpl taskService;
    @Autowired
    private UserServiceImpl userService;
    @LocalServerPort
    private Integer port;
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
        UserRequestDto user = new UserRequestDto();
        user.setName("John");
        user.setEmail("john@mail.com");
        user.setPassword("john");
        UserResponseDto savedUser = userService.save(user);

        taskRequestDto = new TaskRequestDto();
        taskRequestDto.setDescription("Call Maria");
        taskRequestDto.setDeadline(DeadlineDto.TODAY);
        taskRequestDto.setUserId(savedUser.getId());

        paginationDto = new PaginationDto();
        paginationDto.setLimit(10);
        paginationDto.setOffset(0);

    }

    @BeforeEach
    void beforeEach() {
        RestAssured.baseURI = "http://localhost:" + port;
        taskService.deleteAll();
    }

    @Test
    public void saveTest() {
        taskService.save(taskRequestDto);
        taskService.get(paginationDto);
        List<TaskResponseDto> listOfTasks = taskService.get(paginationDto);
        assertEquals(listOfTasks.size(), 1);
    }

    @Test
    public void getTest() {
        taskService.save(taskRequestDto);
        List<TaskResponseDto> listOfTasks = taskService.get(paginationDto);
        assertEquals(1, listOfTasks.size());
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
        TaskResponseDto taskToFind = taskService.findByIdDto(newTaskId);
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
        assertEquals(1,
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
        dtoForTaskUpdate.setUserId(1L);

        TaskResponseDto updatedTask = taskService.edit(taskToEditId, dtoForTaskUpdate);
        assertEquals(taskToEditId, updatedTask.getId());
        assertEquals("Call Robert", updatedTask.getDescription());
        assertEquals(Deadline.SOMEDAY.name(), updatedTask.getDeadline());
    }

    @Test
    public void editTaskNotFoundTest() {
       Integer outOfBoundsIndex = taskService.get(paginationDto).size() + 1;
       assertThrows(ItemNotFoundException.class, () -> taskService.edit(outOfBoundsIndex.longValue(),
               taskRequestDto));
    }

    @Test
    public void deleteTest() {
        taskService.save(taskRequestDto);
        List<TaskResponseDto> allTasks = taskService.get(paginationDto);
        int amountOfTasksBeforeDelete = allTasks.size();
        assertTrue(amountOfTasksBeforeDelete > 0);

        assertEquals("Task is deleted",
                taskService.deleteById(allTasks.get(0).getId()));
        assertEquals(amountOfTasksBeforeDelete - 1, taskService.get(paginationDto).size());
    }

    @Test
    public void deleteNoTaskFoundTest() {
        List<TaskResponseDto> allTasks = taskService.get(paginationDto);
        Integer outOfBoundsIndex =  allTasks.size() + 1;
        assertThrows(ItemNotFoundException.class, () -> taskService.deleteById(outOfBoundsIndex.longValue()));
    }

    @Test
    public void deleteAllTest() {
        taskService.save(taskRequestDto);
        assertFalse(taskService.get(paginationDto).isEmpty());
        taskService.deleteAll();
        assertEquals(0, taskService.get(paginationDto).size());
    }
}