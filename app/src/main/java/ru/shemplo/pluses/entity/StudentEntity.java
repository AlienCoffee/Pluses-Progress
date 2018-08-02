package ru.shemplo.pluses.entity;


public class StudentEntity implements MyEntity {

    private final String FIRST_NAME, LAST_NAME;
    public final int ID;

    public StudentEntity (int id, String firstName, String lastName) {
        this.FIRST_NAME = firstName;
        this.LAST_NAME = lastName;
        this.ID = id;
    }

    public String toString () {
        return getFirstName ();
    }

    public String getFirstName () {
        return FIRST_NAME;
    }

    public String getLastName () {
        return LAST_NAME;
    }

}
