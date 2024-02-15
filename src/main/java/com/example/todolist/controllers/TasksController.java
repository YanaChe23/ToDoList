package com.example.todolist.controllers;

import com.example.todolist.api.v1.dto.DeadlineDto;
import com.example.todolist.api.v1.dto.PaginationDto;
import com.example.todolist.api.v1.dto.TaskRequestDto;
import com.example.todolist.api.v1.dto.TaskResponseDto;
import com.example.todolist.api.v1.rest.TasksApi;
import com.example.todolist.services.task.TaskServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TasksController implements TasksApi {
    @Autowired
    TaskServiceImpl taskServiceImpl;

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
}