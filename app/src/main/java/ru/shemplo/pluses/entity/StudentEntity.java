package ru.shemplo.pluses.entity;


public class StudentEntity implements MyEntity {

    private final String FIRST_NAME;
    public final int ID;

    public StudentEntity (int id, String firstName) {
        this.FIRST_NAME = firstName;
        this.ID = id;
    }

    public String toString () {
        return getFirstName ();
    }

    public String getFirstName () {
        return FIRST_NAME;
    }

}
