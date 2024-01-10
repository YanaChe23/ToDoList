package com.example.todolist.dtos;

import com.example.todolist.entities.Deadline;
import lombok.ToString;


@ToString
public class TaskDTO {
    public String description;
    public Deadline deadline;

    public TaskDTO(String description,  Deadline deadline) {
        this.description = description;
        this.deadline = deadline;
    }
//
    public TaskDTO() {

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Deadline getDeadline() {
        return deadline;
    }

    public void setDeadline(Deadline deadline) {
        this.deadline = deadline;
    }
}

