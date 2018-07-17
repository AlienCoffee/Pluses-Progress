package ru.shemplo.pluses.entity;


import java.util.ArrayList;
import java.util.List;

public class TaskEntity {

    private final String name; // Must be short
    private boolean isSolved = false;
    //TODO: fields

    //TODO: remove
    private int x;
    public TaskEntity() {
        isSolved = false;
        name = (x++) + "";
    }

    public TaskEntity(String name) {
        isSolved = false;
        this.name = name;
    }

    public TaskEntity(String name, boolean isSolved) {
        this.isSolved = isSolved;
        this.name = name;
    }


    public boolean getSolved() {
        return isSolved;
    }

    public String getName() {
        return name;
    }



}
