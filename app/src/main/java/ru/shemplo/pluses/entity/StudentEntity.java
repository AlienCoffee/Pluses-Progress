package ru.shemplo.pluses.entity;


public class StudentEntity implements MyEntity {

    private final String name;

    public StudentEntity (String name) {
        this.name = name;
    }

    public String toString () {
        return getFirstName ();
    }

    public String getFirstName () {
        return name;
    }

}
