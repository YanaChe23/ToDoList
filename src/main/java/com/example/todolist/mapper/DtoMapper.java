package com.example.todolist.mapper;

public interface DtoMapper<E, S, D> {
        E toEntity(S s);
        D toDto(E e);
}
