package com.example.todolist.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="tasks")
//@Getter
//@Setter
@ToString
public class Task {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "task_generator")
    @SequenceGenerator(name = "task_generator", sequenceName = "task_id_gen", allocationSize = 1)
    private int id;
    @Column(name="user_id")
    private int userId;
    @Column(name="description")
    private String description;
    @Column(name="deadline")
    @Enumerated(EnumType.STRING)
    private Deadline deadline;
    public Task() {
    }
    public Task(int userId, String description, Deadline deadline) {
        this.userId = userId;
        this.description = description;
        this.deadline = deadline;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

