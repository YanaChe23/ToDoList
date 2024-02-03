package com.example.todolist.services.task;

import com.example.todolist.api.v1.dto.DeadlineDto;
import com.example.todolist.api.v1.dto.PaginationDto;
import com.example.todolist.api.v1.dto.TaskRequestDto;
import com.example.todolist.api.v1.dto.TaskResponseDto;
import com.example.todolist.entities.Deadline;
import com.example.todolist.entities.Task;

import com.example.todolist.exceptions.FailedToSaveException;
import com.example.todolist.exceptions.ItemNotFoundException;
import com.example.todolist.repositories.TaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.*;

import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TaskServiceImpl implements TaskService {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public TaskResponseDto save(TaskRequestDto taskRequestDto) {
        Task task = modelMapper.map(taskRequestDto, Task.class);
        // TODO remove once auth is added
        task.setUserId(1);
        return modelMapper.map(
                saveToDatabase(task), TaskResponseDto.class
        );
    }

    private Task saveToDatabase(Task task) {
        try {
            return taskRepository.save(task);
        } catch (RuntimeException e) {
            log.error("Failed to save task: " + task + ". Error: " + e.getMessage());
            throw new FailedToSaveException("Internal error. Failed to save task.");
        }
    }

    @Override
    public List<TaskResponseDto> get(PaginationDto paginationDto) {
        if (paginationDto.getLimit() == null || paginationDto.getOffset() == null)
            throw new ItemNotFoundException("Pagination is a required parameter. Please provide pagination.");
        Pageable pageable = PageRequest.of(paginationDto.getOffset(), paginationDto.getLimit());
        return taskRepository.findAll(pageable)
                .stream()
                .map(
                        t -> modelMapper.map(t, TaskResponseDto.class)
                ).toList();
    }

    @Override
    public TaskResponseDto findById(Long id) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        optionalTask.orElseThrow(() -> new ItemNotFoundException("Can't find a task with anb id " + id));
        return modelMapper.map(
                optionalTask.get(), TaskResponseDto.class
        );
    }

    @Override
    public List<TaskResponseDto> findByDeadline(DeadlineDto deadlineDto) {
        return taskRepository.findByDeadline(Deadline.valueOf(deadlineDto.getValue()))
                .stream().
                map(
                    t -> modelMapper.map(t, TaskResponseDto.class)
                ).toList();
    }

    @Override
    public TaskResponseDto edit(Long id, TaskRequestDto taskRequestDto) {
        Task taskToSave = modelMapper.map(
                findById(id), Task.class
        );
        Task editedTask = modelMapper.map(taskRequestDto, Task.class);
        editedTask.setId(taskToSave.getId());
        return modelMapper.map(
                saveToDatabase(editedTask), TaskResponseDto.class
        );
    }

    @Override
    public String deleteById(Long id) {
        findById(id);
        taskRepository.deleteById(id);
        return "Task is deleted.";
    }

    void deleteAll() {
        taskRepository.deleteAll();
    }
}
