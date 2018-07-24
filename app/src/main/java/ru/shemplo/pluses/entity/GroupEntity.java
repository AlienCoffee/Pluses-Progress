package ru.shemplo.pluses.entity;


public class GroupEntity implements MyEntity{
    private final String name, teacher;
    private final int size;
    //TODO: any links to students?

    //TODO: constructors
    static private int tmp = 1;
    public GroupEntity(int id, int population) {
        teacher = "Alexey Rusakov";
        this.size = population;
        this.name = "G" + id;
    }

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
