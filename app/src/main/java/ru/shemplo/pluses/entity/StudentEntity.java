package ru.shemplo.pluses.entity;


import java.util.ArrayList;
import java.util.List;

public class StudentEntity implements MyEntity {
    private final String name;

    public StudentEntity(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
