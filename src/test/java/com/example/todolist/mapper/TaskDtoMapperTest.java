package com.example.todolist.mapper;

import com.example.todolist.api.v1.dto.DeadlineDto;
import com.example.todolist.api.v1.dto.TaskRequestDto;
import com.example.todolist.api.v1.dto.TaskResponseDto;
import com.example.todolist.entities.Deadline;
import com.example.todolist.entities.Task;
import com.example.todolist.entities.User;
import com.example.todolist.services.task.TaskServiceImpl;
import com.example.todolist.services.user.UserService;
import com.example.todolist.services.user.UserServiceImpl;
import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class TaskDtoMapperTest {

    @Autowired
    private TaskDtoMapper taskDtoMapper;
    @MockBean
    private UserServiceImpl userService;
    private User user;
    private TaskResponseDto taskResponseDto;
    private TaskRequestDto taskRequestDto;


    @PostConstruct
    private void postConstruct() {
        user = new User();
        user.setId(1L);
        user.setName("Alex");

        Task task = new Task();
        task.setId(1L);
        task.setDeadline(Deadline.TODAY);
        task.setUser(user);
        task.setDescription("Call Maria");

        taskResponseDto = new TaskResponseDto();
        taskResponseDto.setId(1L);
        taskResponseDto.setUserId(1L);
        taskResponseDto.setDeadline("TODAY");
        taskResponseDto.setDescription("Call Maria");

        taskRequestDto = new TaskRequestDto();
        taskRequestDto.setUserId(1L);
        taskRequestDto.setDeadline(DeadlineDto.TODAY);
        taskRequestDto.setDescription("Call Maria");

        when(userService.getUser(1L)).thenReturn(user);
    }

    @Test
    void mapSpecificFieldsToEntity() {
        Task mappedTask = taskDtoMapper.toEntity(taskRequestDto);
        System.out.println(mappedTask);

        assertEquals(taskRequestDto.getDescription(), mappedTask.getDescription());
        assertEquals(taskRequestDto.getUserId(), mappedTask.getUser().getId());
        assertEquals(taskRequestDto.getDeadline().getValue(), mappedTask.getDeadline().name());
    }

    @Test
    void mapSpecificFieldsToDto() {
    }
}