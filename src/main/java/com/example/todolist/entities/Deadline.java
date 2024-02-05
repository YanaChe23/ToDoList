package com.example.todolist.entities;

import java.util.Arrays;
import java.util.List;

public enum Deadline {
        TODAY,
        WEEK,
        SOMEDAY;

        public static List<String> getNames() {
                return Arrays.stream(Deadline.values())
                        .map(Enum::name)
                        .toList();
        }
}