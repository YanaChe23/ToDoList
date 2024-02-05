package com.example.todolist.repositories;

import com.example.todolist.entities.Deadline;
import com.example.todolist.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByDeadline(Deadline deadline);
}
