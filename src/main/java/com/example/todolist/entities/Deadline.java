package com.example.todolist.entities;

public enum Deadline {
        today("Today"),
        week("Week"),
        someday("Someday");
        public final String capitalized;
        Deadline(String capitalized) {
            this.capitalized = capitalized;
        }
}
