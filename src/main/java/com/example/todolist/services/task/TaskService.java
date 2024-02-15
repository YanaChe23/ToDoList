package com.example.todolist.services.task;

import com.example.todolist.api.v1.dto.DeadlineDto;
import com.example.todolist.api.v1.dto.PaginationDto;
import com.example.todolist.api.v1.dto.TaskRequestDto;
import com.example.todolist.api.v1.dto.TaskResponseDto;
import com.example.todolist.entities.Task;

import java.util.List;

public interface TaskService {
    TaskResponseDto save(TaskRequestDto taskRequestDto);
    List<TaskResponseDto> get(PaginationDto paginationDto);
    Task findById(Long id);
    TaskResponseDto findByIdDto(Long id);

    List<TaskResponseDto> findByDeadline(DeadlineDto deadlineDto);
    TaskResponseDto edit(Long id, TaskRequestDto taskRequestDto);
    String deleteById(Long id);
}
