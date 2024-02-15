package com.example.todolist.mapper;

import com.example.todolist.api.v1.dto.TaskRequestDto;
import com.example.todolist.api.v1.dto.TaskResponseDto;
import com.example.todolist.entities.Task;
import com.example.todolist.services.user.UserService;
import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.example.todolist.entities.User;


import java.util.Optional;

@Component
public class TaskDtoMapper extends AbstractDtoMapper<Task, TaskRequestDto, TaskResponseDto> {
    @Autowired
    private ModelMapper regularMapper;
    @Autowired
    private UserService userService;
    public TaskDtoMapper() {
        super(Task.class, TaskRequestDto.class, TaskResponseDto.class);
    }

    @PostConstruct
    public void setupMapper() {
        toEntityTypeMapper
                .addMappings(m -> m.skip(Task::setId))
                .implicitMappings();
    }

    @Override
    public void mapSpecificFieldsToEntity(TaskRequestDto source, Task destination) {
//        System.out.println(userService.getAllUsers());
//        Optional.ofNullable(source.getUserId()).ifPresent(
//                id -> destination.setUser(
//                        regularMapper.map(
//                                userService.getUser(id),
//                                User.class)
//                )
//        );
    }

    @Override
    public void mapSpecificFieldsToDto(Task source, TaskResponseDto destination) {
//        Optional.ofNullable(source.getUser()).ifPresent(
//                category -> destination.setUserId(category.getId())
//        );

    }

    private void setMapperConfigs() {
        mapper.getConfiguration().setAmbiguityIgnored(true);
    }
}
