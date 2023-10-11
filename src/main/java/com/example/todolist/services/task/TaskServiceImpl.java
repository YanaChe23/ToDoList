package com.example.todolist.services.task;

import com.example.todolist.entities.Task;

import com.example.todolist.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class TaskServiceImpl implements TaskService {
    @Autowired
    private TaskRepository taskRepository;
    @Override
    public List<Task> getAllTask() {
        return taskRepository.findAll();
    }

    @Override
    public void saveTask(Task task) {
        taskRepository.save(task);
    }

    @Override
    public Task getTask(int id) {
        Optional<Task> task = taskRepository.findById(id);
        if (task.isPresent()) return task.get();
        return null;
    }

    @Override
    public void deleteTask(int id) {

    }

    @Override
    public void deleteAllTasks() {
        taskRepository.deleteAll();
    }

    @Override
    public List<Task> findAllTasks() {
        return taskRepository.findAll();
    }
}
