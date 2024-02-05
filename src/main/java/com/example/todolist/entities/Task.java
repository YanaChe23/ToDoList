package com.example.todolist.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="tasks")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "task_generator")
    @SequenceGenerator(name = "task_generator", sequenceName = "task_id_gen", allocationSize = 1)
    private Long id;
    @Column(name="user_id")
    private int userId;
    @Column(name="description")
    private String description;
    @Column(name="deadline")
    @Enumerated(EnumType.STRING)
    private Deadline deadline;
}

