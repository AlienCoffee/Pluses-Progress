package ru.shemplo.pluses.entity;


public class StudentEntity implements MyEntity {

    private final String name;
//    private final int ID;

    public StudentEntity (String name) {
        this.name = name;
    }

//    public StudentEntity (int id) {
//        this.ID = id;
//      }


    public String toString () {
        return getFirstName ();
    }

    public String getFirstName () {
        return name;
    }

}
