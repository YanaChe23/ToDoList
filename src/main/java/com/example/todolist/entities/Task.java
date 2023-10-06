package com.example.todolist.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="tasks")
@Getter
@Setter
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
    @Column(name="term")
    private int term;
    public Task() {
    }
    public Task(int userId, String description, int term) {
        this.userId = userId;
        this.description = description;
        this.term = term;
    }
}
