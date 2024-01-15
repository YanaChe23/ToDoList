package com.example.todolist.dtos;

import com.example.todolist.entities.Deadline;
import lombok.AllArgsConstructor;
import lombok.ToString;


@ToString
@AllArgsConstructor
public class TaskDTO {
    private String description;
    private Deadline deadline;
}

