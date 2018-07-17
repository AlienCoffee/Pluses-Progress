package ru.shemplo.pluses.entity;


import java.io.Serializable;

public class GroupEntity implements Serializable {
    private final String name, teacher;
    private int size;
    //TODO: any links to students?

    //TODO: remove this
    static private int tmp = 1;
    public GroupEntity(int id, int population) {
        teacher = "Alexey Rusakov";
        this.size = population;
        this.name = "G" + id;
    }

    //TODO: add constructors;

    public String getName() {
        return name;
    }

    public String getTeacher() {
        return teacher;
    }

    public int getSize() {
        return size;
    }
}
