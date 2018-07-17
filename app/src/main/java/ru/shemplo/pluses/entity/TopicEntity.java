package ru.shemplo.pluses.entity;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TopicEntity {
    private final String name;
    private final List<TaskEntity> tasks;
    //TODO: fields

    //TODO: add constructor
    private int x = 1;
    public TopicEntity() {
        tasks = new ArrayList<>();
        for (int i = 0; i < x; i++) {
            tasks.add(new TaskEntity());
        }
        name = "Chapter" + (x++);
    }

    public TopicEntity(String name, List<TaskEntity> tasks) {
        this.name = name;
        this.tasks = tasks;
    }

    public String getName() {
        return name;
    }

    public List<TaskEntity> getTasks() {
        return tasks;
    }
}
