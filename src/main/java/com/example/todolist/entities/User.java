package com.example.todolist.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_generator")
    @SequenceGenerator(name = "user_generator", sequenceName = "user_id_gen", allocationSize = 1)
    private Long id;
    @Column(name="name")
    private String name;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "user")
    List<Task> tasks;

    public User(String name) {
        this.name = name;
    }

    public void addTask(Task ... tasksToAdd) {
        if (tasks == null) tasks = new ArrayList<>();
        for(Task task : tasksToAdd) {
            task.setUser(this);
            tasks.add(task);
        }
    }
}