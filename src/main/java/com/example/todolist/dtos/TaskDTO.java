package com.example.todolist.dtos;

import com.example.todolist.entities.Deadline;
import lombok.*;


@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TaskDTO {
    public String description;
    public Deadline deadline;
}