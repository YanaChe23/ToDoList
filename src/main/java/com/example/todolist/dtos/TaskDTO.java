package com.example.todolist.dtos;

import com.example.todolist.entities.Deadline;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@ToString
@AllArgsConstructor
@Getter
@Setter
public class TaskDTO {
    public String description;
    public Deadline deadline;
}