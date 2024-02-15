package com.example.todolist.mapper;

import jakarta.annotation.PostConstruct;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public abstract class AbstractDtoMapper<E, S, D> implements DtoMapper<E, S, D> {
    @Autowired
    ModelMapper mapper;
    private final Class<E> e;
    private final Class<S> s;
    private final Class<D> d;
    TypeMap<S, E> toEntityTypeMapper;
    TypeMap<E, D> toDtoTypeMapper;

    AbstractDtoMapper(Class<E> e, Class<S> s, Class<D> d) {
        this.e = e;
        this.s = s;
        this.d = d;
    }

    @PostConstruct
    void test() {
        toEntityTypeMapper = mapper.createTypeMap(s, e)
                .setPostConverter(toEntityConverter());

        toDtoTypeMapper = mapper.createTypeMap(e, d)
                .setPostConverter(toDtoConverter());
    }
    @Override
    public E toEntity(S s) {
        return Objects.isNull(s) ? null : mapper.map(s, e);
    }

    @Override
    public D toDto(E e) {
        return Objects.isNull(e) ? null : mapper.map(e, d);
    }

    public Converter<S, E> toEntityConverter() {
        return context -> {
            S source = context.getSource();
            E destination = context.getDestination();
            mapSpecificFieldsToEntity(source, destination);
            return context.getDestination();
        };
    }

    public Converter<E, D> toDtoConverter() {
        return context -> {
            E source = context.getSource();
            D destination = context.getDestination();
            mapSpecificFieldsToDto(source, destination);
            return context.getDestination();
        };
    }

    abstract public void mapSpecificFieldsToEntity(S s, E e);
    abstract public void mapSpecificFieldsToDto(E e, D d);
}
