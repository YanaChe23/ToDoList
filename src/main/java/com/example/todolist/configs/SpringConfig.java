package com.example.todolist.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {

    // to convert DTO to entity and back
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

   // to convert between Java objects and JSON
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
