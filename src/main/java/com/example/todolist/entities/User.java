package com.example.todolist.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="users")
@Data
public class User {
    @Id
    @Column(name="id")
    private int id;
    @Column(name="name")
    private String name;
}