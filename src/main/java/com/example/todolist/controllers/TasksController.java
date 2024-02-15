package com.example.todolist.controllers;

import com.example.todolist.api.v1.dto.DeadlineDto;
import com.example.todolist.api.v1.dto.PaginationDto;
import com.example.todolist.api.v1.dto.TaskRequestDto;
import com.example.todolist.api.v1.dto.TaskResponseDto;
import com.example.todolist.api.v1.rest.TasksApi;
import com.example.todolist.entities.Deadline;
import com.example.todolist.entities.Task;
import com.example.todolist.entities.User;
import com.example.todolist.repositories.TaskRepository;
import com.example.todolist.repositories.UserRepository;
import com.example.todolist.services.task.TaskServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TasksController implements TasksApi {
    @Autowired
    TaskServiceImpl taskServiceImpl;
    @Autowired
    UserRepository userRepository;
    @Autowired
    TaskRepository taskRepository;

    @Override
    public ResponseEntity<TaskResponseDto> tasksPost(TaskRequestDto taskRequestDto) {
        return ResponseEntity.ok(
                taskServiceImpl.save(taskRequestDto)
        );
    }

    @Override
    public ResponseEntity<List<TaskResponseDto>> tasksGet(PaginationDto pagination) {
        return ResponseEntity.ok(
                taskServiceImpl.get(pagination)
        );
    }

    @Override
    public ResponseEntity<TaskResponseDto> tasksIdGet(Long id) {
        return ResponseEntity.ok(
                taskServiceImpl.findByIdDto(id)
        );
    }

    @Override
    public ResponseEntity<List<TaskResponseDto>> tasksDeadlineDeadlineGet(@PathVariable DeadlineDto deadlineDto) {
        return ResponseEntity.ok(
                taskServiceImpl.findByDeadline(deadlineDto)
        );
    }

    @Override
    public ResponseEntity<TaskResponseDto> tasksIdPatch(Long id, TaskRequestDto taskRequestDto) {
        return ResponseEntity.ok(
                taskServiceImpl.edit(id, taskRequestDto)
        );
    }

    @Override
    public ResponseEntity<String> tasksIdDelete(Long id) {
        return ResponseEntity.ok(
                taskServiceImpl.deleteById(id)
        );
    }

    @GetMapping("/test")
    public void tst() {
        User user = new User("Kot");
        user.setId(1L);
        System.out.println("!!!!!!! " + user);
        userRepository.save(user);
    }
}