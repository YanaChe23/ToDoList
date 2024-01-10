package com.example.todolist.services.task;

import com.example.todolist.dtos.TaskDTO;
import com.example.todolist.entities.Deadline;
import com.example.todolist.entities.Task;

import com.example.todolist.exceptions.task.TaskNotFoundException;
import com.example.todolist.repositories.TaskRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
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
    public String deleteTask(int id) {
        if (!taskRepository.existsById(id)) throw new TaskNotFoundException(id);
        taskRepository.deleteById(id);
        return "Task with id=" + id + " is deleted.";
    }

    //    @Override
//    public Task editTask(Task task) {
//        Task taskToEdit = getTask(task.getId());
//        taskToEdit.setDescription(task.getDescription());
//        taskToEdit.setDeadline(task.getDeadline());
//        return saveTask(task);
//    }

    @Override
    public void deleteAllTasks() {
        taskRepository.deleteAll();
    }

    @Override
    public List<Task> findAllTasks() {
        return taskRepository.findAll();
    }

    @Override
    public List<Task> findTasksByDeadline(Deadline deadline) {
        return taskRepository.findByDeadline(deadline);
    }


}
