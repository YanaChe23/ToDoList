package com.example.todolist.controllers;

import com.example.todolist.dtos.TaskDTO;
import com.example.todolist.entities.Task;
import com.example.todolist.exceptions.task.TaskNotFoundException;
import com.example.todolist.repositories.TaskRepository;
import com.example.todolist.services.task.TaskServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.example.todolist.entities.Deadline;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class TasksControllerTest {
    @MockBean
    private TaskServiceImpl taskService;
    @MockBean
    private TaskRepository taskRepository;
    @Autowired
    private TasksController tasksController;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ModelMapper modelMapper;
    private Task emailTask;
    private Task dogFoodTask;

    @PostConstruct
    void setUp() {
        emailTask = new Task(1, "Send an e-mail", Deadline.today);
        dogFoodTask = new Task(1, "Buy Dog Food", Deadline.week);
        List<Task> tasks = List.of(emailTask, dogFoodTask);
        when(taskService.getAllTask()).thenReturn(tasks);
    }

    @Test
    public void controllerLoadsTest() {
        assertThat(tasksController).isNotNull();
    }

    @Test
    public void saveTaskTest() throws Exception {
        TaskDTO taskDTO = new TaskDTO("Send an e-mail", Deadline.today);
        Task savedTask = modelMapper.map(taskDTO, Task.class);

        when(taskService.saveTask(any(TaskDTO.class))).thenReturn(savedTask);

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("{" +
                        "\"id\":0,\"" +
                        "userId\":0," +
                        "\"description\":\"Send an e-mail\"," +
                        "\"deadline\":\"today\"}"));
    }

    @Test
    public void getAllTasksTest() throws Exception {
        this.mockMvc.perform(get("/tasks"))
            .andExpect(status().isOk())
                .andExpect(content().string("[" +
                        "{\"id\":0," +
                        "\"userId\":1," +
                        "\"description\":\"Send an e-mail\"," +
                        "\"deadline\":\"today\"}" +

                        ",{\"id\":0," +
                        "\"userId\":1," +
                        "\"description\":\"Buy Dog Food\"," +
                        "\"deadline\":\"week\"}]"));
    }

    @Test
    public void getAllTasksWhenNoTasksTest() throws Exception {
        when(taskService.getAllTask()).thenReturn(new ArrayList<>());
        this.mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }

    @Test
    public void getTaskTest() throws Exception {
        when(taskService.getTask(1)).thenReturn(emailTask);
        this.mockMvc.perform(get("/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"id\":0," +
                        "\"userId\":1," +
                        "\"description\":\"Send an e-mail\"," +
                        "\"deadline\":\"today\"}"));
    }

    @Test
    public void getTaskFailedTest() throws Exception {
        when(taskService.getTask(1)).thenThrow(new TaskNotFoundException(1));
        this.mockMvc.perform(get("/tasks/1"))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string("{\"info\":\"Could not find a task with id 1.\"}"));
    }

    @Test
    public void editTaskTest() throws Exception {
        TaskDTO taskDTO = new TaskDTO("Send an e-mail to Maria", Deadline.someday);
        Task editedTask = new Task(1, "Send an e-mail to Maria", Deadline.someday);

        when(taskService.editTask(any(TaskDTO.class), eq(1))).thenReturn(editedTask);

        this.mockMvc.perform(patch("/tasks/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        objectMapper.writeValueAsString(taskDTO))
        )
                .andExpect(status().isOk())
                .andExpect(content().string("{\"id\":0," +
                        "\"userId\":1," +
                        "\"description\":\"Send an e-mail to Maria\"," +
                        "\"deadline\":\"someday\"}"));
    }

    @Test
    public void editTaskFailedTest() throws Exception {
        when(taskService.editTask(any(TaskDTO.class), eq(1))).thenThrow(new TaskNotFoundException(1));

        this.mockMvc.perform(patch("/tasks/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        objectMapper.writeValueAsString(new TaskDTO()))
        )
                .andExpect(status().is4xxClientError())
                .andExpect(content().string("{\"info\":\"Could not find a task with id 1.\"}"));
    }

    @Test
    public void deleteTaskTest() throws Exception {
        when(taskService.deleteTask(1)).thenReturn("Task with id=1 is deleted.");
        this.mockMvc.perform(delete("/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Task with id=1 is deleted."));
    }

    @Test
    public void deleteTaskFailedTest() throws Exception {
        when(taskService.deleteTask(1)).thenThrow(new TaskNotFoundException(1));
        this.mockMvc.perform(delete("/tasks/1"))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string("{\"info\":\"Could not find a task with id 1.\"}"));
    }
}