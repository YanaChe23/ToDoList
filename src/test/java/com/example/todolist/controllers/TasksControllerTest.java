package com.example.todolist.controllers;


import com.example.todolist.api.v1.dto.DeadlineDto;
import com.example.todolist.api.v1.dto.PaginationDto;
import com.example.todolist.api.v1.dto.TaskRequestDto;
import com.example.todolist.api.v1.dto.TaskResponseDto;

import com.example.todolist.exceptions.ItemNotFoundException;
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

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class TasksControllerTest {
    @MockBean
    private TaskServiceImpl taskService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ModelMapper modelMapper;
    private TaskRequestDto emailRequest;
    private TaskRequestDto dogFoodRequest;
    private TaskResponseDto emailResponse;
    private TaskResponseDto dogFoodResponse;
    private PaginationDto paginationDto;
    private List<TaskResponseDto> responseDtos;

    @PostConstruct
    void setUp() {
        emailRequest = new TaskRequestDto();
        emailRequest.setDeadline(DeadlineDto.TODAY);
        emailRequest.setDescription("Send an e-mail");

        dogFoodRequest = new TaskRequestDto();
        dogFoodRequest.setDeadline(DeadlineDto.WEEK);
        dogFoodRequest.setDescription("Buy Dog Food");

        emailResponse = modelMapper.map(emailRequest, TaskResponseDto.class);
        emailResponse.setId(1L);
        emailResponse.setUserId(1L);

        dogFoodResponse = modelMapper.map(dogFoodRequest, TaskResponseDto.class);
        dogFoodResponse.setId(2L);
        dogFoodResponse.setUserId(1L);

        responseDtos = List.of(emailResponse, dogFoodResponse);

        paginationDto = new PaginationDto();
        paginationDto.setLimit(10);
        paginationDto.setOffset(0);

        when(taskService.get(paginationDto)).thenReturn(responseDtos);
    }

    @Test
    public void tasksPostTest() throws Exception {
        when(taskService.save(any(TaskRequestDto.class))).thenReturn(emailResponse);

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emailRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"id\":1," +
                        "\"user_id\":1,"+
                        "\"description\":\"Send an e-mail\"," +
                        "\"deadline\":\"TODAY\"}"));
    }

    @Test
    public void tasksGetTest() throws Exception {
        this.mockMvc.perform(get("/tasks")
                .param("offset", String.valueOf(0))
                .param("limit", String.valueOf(10)))
                .andExpect(status().isOk())
                .andExpect(content().string("[{\"id\":1," +
                        "\"user_id\":1," +
                        "\"description\":\"Send an e-mail\"," +
                        "\"deadline\":\"TODAY\"}," +
                        "{\"id\":2," +
                        "\"user_id\":1," +
                        "\"description\":" +
                        "\"Buy Dog Food\"," +
                        "\"deadline\":\"WEEK\"}]"));
    }

    @Test
    public void tasksGetNoTasksTest() throws Exception {
        when(taskService.get(any(PaginationDto.class))).thenReturn(new ArrayList<>());
        this.mockMvc.perform(get("/tasks")
                        .param("offset", String.valueOf(0))
                        .param("limit", String.valueOf(10)))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }

    @Test
    public void tasksGetNoPaginationTest() throws Exception {
        when(taskService.get(any(PaginationDto.class)))
                .thenThrow(new ItemNotFoundException("Pagination is a required parameter. Please provide pagination."));
        this.mockMvc.perform(get("/tasks"))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string("{\"info\":\"Pagination is a required parameter. " +
                        "Please provide pagination.\"}"));
    }

    @Test
    public void tasksIdGetTest() throws Exception {
        when(taskService.findById(1L)).thenReturn(emailResponse);
        this.mockMvc.perform(get("/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"id\":1," +
                        "\"user_id\":1," +
                        "\"description\":\"Send an e-mail\"," +
                        "\"deadline\":\"TODAY\"}"));
    }

    @Test
    public void tasksIdGetNoTaskFoundTest() throws Exception {
        when(taskService.findById(1L)).thenThrow(new ItemNotFoundException("Can't find a task with anb id 1"));
        this.mockMvc.perform(get("/tasks/1"))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string("{\"info\":\"Can't find a task with anb id 1\"}"));
    }

    @Test
    public void tasksDeadlineDeadlineGetTest() throws Exception {
        when(taskService.findByDeadline(any(DeadlineDto.class))).thenReturn(responseDtos);
        this.mockMvc.perform(get("/tasks/deadline/TODAY"))
                .andExpect(status().isOk())
                .andExpect(content().string("[{\"id\":1," +
                        "\"user_id\":1," +
                        "\"description\":\"Send an e-mail\"," +
                        "\"deadline\":\"TODAY\"}," +
                        "{\"id\":2," +
                        "\"user_id\":1," +
                        "\"description\":\"Buy Dog Food\"," +
                        "\"deadline\":\"WEEK\"}]"));
    }
    @Test
    public void tasksIdPatchTest() throws Exception {
        when(taskService.edit(eq(1L), any(TaskRequestDto.class))).thenReturn(emailResponse);
        this.mockMvc.perform(patch("/tasks/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(emailRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"id\":1," +
                        "\"user_id\":1," +
                        "\"description\":\"Send an e-mail\"," +
                        "\"deadline\":\"TODAY\"}"));
    }

    @Test
    public void tasksIdPatchTaskNotFoundTest() throws Exception {
        when(taskService.edit(eq(1L), any(TaskRequestDto.class)))
                .thenThrow(new ItemNotFoundException("Can't find a task with anb id 1"));
        this.mockMvc.perform(patch("/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emailRequest)))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string("{\"info\":\"Can't find a task with anb id 1\"}"));
    }

    @Test
    public void tasksIdDeleteTest() throws Exception {
        when(taskService.deleteById(1L)).thenReturn("Task is deleted");
        this.mockMvc.perform(delete("/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Task is deleted"));
    }

    @Test
    public void tasksIdDeleteTaskNotFoundTest() throws Exception {
        when(taskService.deleteById(1L)).thenThrow(new ItemNotFoundException("Can't find a task with anb id 1"));
        this.mockMvc.perform(delete("/tasks/1"))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string("{\"info\":\"Can't find a task with anb id 1\"}"));
    }
}