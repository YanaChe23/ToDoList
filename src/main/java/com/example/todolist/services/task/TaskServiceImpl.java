package com.example.todolist.services.task;

import com.example.todolist.dtos.TaskDTO;
import com.example.todolist.entities.Deadline;
import com.example.todolist.entities.Task;

import com.example.todolist.exceptions.InternalErrorException;
import com.example.todolist.exceptions.task.TaskNotFoundException;
import com.example.todolist.repositories.TaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

import org.springframework.stereotype.Service;

import java.lang.reflect.Field;

@Service
@Slf4j
public class TaskServiceImpl implements TaskService {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Task saveTask(TaskDTO taskDTO) {
        Task task = modelMapper.map(taskDTO, Task.class);
        // TODO remove once auth is added
        task.setUserId(1);
        return taskRepository.save(task);
    }

    @Override
    public List<Task> getAllTask() {
        return taskRepository.findAll();
    }

    @Override
    public Task getTask(int id) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        optionalTask.orElseThrow(() -> new TaskNotFoundException(id));
        return optionalTask.get();
    }

    @Override
    public Task editTask(TaskDTO taskUpdatesDto, int id) {
        Task taskToEdit = getTask(id);
        Task editedTask = transferDtoValuesToEntity(taskToEdit, taskUpdatesDto);
        return taskRepository.save(editedTask);
    }

    private Task transferDtoValuesToEntity(Task taskEntity, TaskDTO taskDTO) {
        Field[] entityFields = taskEntity.getClass().getDeclaredFields();

        for (Field entityField : entityFields) {
            Field dtoFieldIfExists = null;
            try {
                dtoFieldIfExists = taskDTO.getClass().getDeclaredField(entityField.getName());
                 if (dtoFieldIfExists.getType().equals(entityField.getType())) {
                     entityField.setAccessible(true);
                     dtoFieldIfExists.setAccessible(true);
                     entityField.set(taskEntity, dtoFieldIfExists.get(taskDTO));
                 }
            } catch (NoSuchFieldException ignored) {
                // it's ok - it means that there are fields that don't match, the app should just skip it
                // for example, it can be task id field in entity - we don't pass this info in DTO
                // thrown by .getDeclaredField()
            } catch (IllegalAccessException e) {
                log.error("Failed to get DTO " + entityField.getName() + " field value: "  + e );
                throw new InternalErrorException();
            } finally {
                if (entityField != null) {
                    entityField.setAccessible(false);
                }
                if (dtoFieldIfExists != null) {
                    dtoFieldIfExists.setAccessible(false);
                }
            }
        }
        return taskEntity;
    }

    @Override
    public String deleteTask(int id) {
        if (!taskRepository.existsById(id)) throw new TaskNotFoundException(id);
        taskRepository.deleteById(id);
        return "Task with id=" + id + " is deleted.";
    }

    @Override
    public void deleteAllTasks() {
        taskRepository.deleteAll();
    }
//
//    @Override
//    public List<Task> findTasksByDeadline(Deadline deadline) {
//        return taskRepository.findByDeadline(deadline);
//    }

}
