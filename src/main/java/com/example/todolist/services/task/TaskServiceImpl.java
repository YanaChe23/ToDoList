package com.example.todolist.services.task;

import com.example.todolist.api.v1.dto.DeadlineDto;
import com.example.todolist.api.v1.dto.PaginationDto;
import com.example.todolist.api.v1.dto.TaskRequestDto;
import com.example.todolist.api.v1.dto.TaskResponseDto;
import com.example.todolist.entities.Deadline;
import com.example.todolist.entities.Task;

import com.example.todolist.exceptions.FailedToSaveException;
import com.example.todolist.exceptions.ItemNotFoundException;
import com.example.todolist.exceptions.UserIdRequiredException;
import com.example.todolist.mapper.TaskDtoMapper;
import com.example.todolist.repositories.TaskRepository;
import lombok.extern.slf4j.Slf4j;
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
    private TaskDtoMapper taskDtoMapper;


    @Override
    public TaskResponseDto save(TaskRequestDto taskRequestDto) {
        Task task = taskDtoMapper.toEntity(taskRequestDto);
        return taskDtoMapper.toDto(saveToDatabase(task));
    }

    private Task saveToDatabase(Task task) {
        try {
            return taskRepository.save(task);
        } catch (RuntimeException e) {
            log.error("Failed to save task: " + task + ". Error: " + e.getMessage());
            throw new FailedToSaveException("Internal error. Failed to save a task.");
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
                        t -> taskDtoMapper.toDto(t)
                ).toList();
    }

    @Override
    public TaskResponseDto findByIdDto(Long id) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        optionalTask.orElseThrow(() -> new ItemNotFoundException("Can't find a task with an id " + id));
        return taskDtoMapper.toDto(optionalTask.get());
    }

    public Task findById(Long id) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        optionalTask.orElseThrow(() -> new ItemNotFoundException("Can't find a task with an id " + id));
        return
                optionalTask.get();

    }

    @Override
    public List<TaskResponseDto> findByDeadline(DeadlineDto deadlineDto) {
        List<String> allowedDeadlines = Deadline.getNames();
        String dtoDeadlineValue = deadlineDto.getValue();

        if (!allowedDeadlines.contains(dtoDeadlineValue))
            throw new ItemNotFoundException("Failed to find a deadline value " + dtoDeadlineValue);
        return taskRepository.findByDeadline(Deadline.valueOf(dtoDeadlineValue))
                .stream()
                .map(
                        t -> taskDtoMapper.toDto(t)
                )
                .toList();
    }

    @Override
    public TaskResponseDto edit(Long id, TaskRequestDto taskRequestDto) {
        if (taskRequestDto.getUserId() == null) throw new UserIdRequiredException();
        Task taskToSave = findById(id);
        Task editedTask = taskDtoMapper.toEntity(taskRequestDto);
        editedTask.setId(taskToSave.getId());
        return taskDtoMapper.toDto(
                saveToDatabase(editedTask)
        );
    }

    @Override
    public String deleteById(Long id) {
        findById(id);
        taskRepository.deleteById(id);
        return "Task is deleted";
    }

    void deleteAll() {
        taskRepository.deleteAll();
    }

//    @Override
//    public TaskResponseDto save(TaskRequestDto taskRequestDto) {
//        if (taskRequestDto.getUserId() == null) throw new UserIdRequiredException();
//        Task task = taskDtoMapper.toEntity(taskRequestDto);
//        return taskDtoMapper.toDto(
//                saveToDatabase(task)
//        );
//    }
//
//    private Task saveToDatabase(Task task) {
//        try {
//            return taskRepository.save(task);
//        } catch (RuntimeException e) {
//            log.error("Failed to save task: " + task + ". Error: " + e.getMessage());
//            throw new FailedToSaveException("Internal error. Failed to save a task.");
//        }
//    }
//
//    @Override
//    public List<TaskResponseDto> get(PaginationDto paginationDto) {
//        if (paginationDto.getLimit() == null || paginationDto.getOffset() == null)
//            throw new ItemNotFoundException("Pagination is a required parameter. Please provide pagination.");
//        Pageable pageable = PageRequest.of(paginationDto.getOffset(), paginationDto.getLimit());
//        return taskRepository.findAll(pageable)
//                .stream()
//                .map(
//                        t -> taskDtoMapper.toDto(t))
//                .toList();
//    }
//
//    @Override
//    public TaskResponseDto findByIdDto(Long id) {
//        Task task = findById(id);
//        return taskDtoMapper.toDto(task);
//    }
//
//    @Override
//    public Task findById(Long id) {
//        Optional<Task> optionalTask = taskRepository.findById(id);
//        optionalTask.orElseThrow(() -> new ItemNotFoundException("Can't find a task with an id " + id));
//        return optionalTask.get();
//    }
//
//    @Override
//    public List<TaskResponseDto> findByDeadline(DeadlineDto deadlineDto) {
//        List<String> allowedDeadlines = Deadline.getNames();
//        String dtoDeadlineValue = deadlineDto.getValue();
//
//        if (!allowedDeadlines.contains(dtoDeadlineValue))
//            throw new ItemNotFoundException("Failed to find a deadline value " + dtoDeadlineValue);
//        return taskRepository.findByDeadline(Deadline.valueOf(dtoDeadlineValue))
//                .stream()
//                .map(
//                    t -> taskDtoMapper.toDto(t))
//                .toList();
//    }
//
//    @Override
//    public TaskResponseDto edit(Long id, TaskRequestDto taskRequestDto) {
//        if (taskRequestDto.getUserId() == null) throw new UserIdRequiredException();
//        Task taskToSave = findById(id);
//        Task editedTask = taskDtoMapper.toEntity(taskRequestDto);
//        editedTask.setId(taskToSave.getId());
//        return taskDtoMapper.toDto(
//                saveToDatabase(editedTask)
//        );
//    }
//
//    @Override
//    public String deleteById(Long id) {
//        findById(id);
//        taskRepository.deleteById(id);
//        return "Task is deleted";
//    }
//
//    void deleteAll() {
//        taskRepository.deleteAll();
//    }
}
