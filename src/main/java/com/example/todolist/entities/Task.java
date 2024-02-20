package com.example.todolist.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="tasks")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "task_generator")
    @SequenceGenerator(name = "task_generator", sequenceName = "task_id_gen", allocationSize = 1)
    private Long id;
    @Column(name="description")
    private String description;
    @Column(name="deadline")
    @Enumerated(EnumType.STRING)
    private Deadline deadline;
    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    public Task(String description, Deadline deadline) {
        this.description = description;
        this.deadline = deadline;
    }

//    @Override
//    public String toString() {
//        return "Task{" +
//                "id=" + id + '\'' +
//                ", user_id=" + user.getId() + '\'' +
//                ", description='" + description + '\'' +
//                ", deadline=" + deadline +
//                '}';
//    }
}

