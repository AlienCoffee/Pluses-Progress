package ru.shemplo.pluses.entity;


public class StudentEntity implements MyEntity {

    private final String FIRST_NAME, LAST_NAME;
    public final int ID;

    public StudentEntity (int id, String firstName) {
        this.FIRST_NAME = firstName;
        this.LAST_NAME = "Ivanov";//TODO: add last name support
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
