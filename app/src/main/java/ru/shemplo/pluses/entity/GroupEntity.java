package ru.shemplo.pluses.entity;

import java.io.Serializable;
import java.sql.Timestamp;

public class GroupEntity implements Serializable, MyEntity {

    private static final long serialVersionUID = 0L;

    private final String name, teacher;
    private int size;

    public final String TITLE, COMMENT, CREATED;
    private final int headteacherID;
    public final int ID;

    public GroupEntity (int id, String title, String comment, String created,
                        int headteacher, boolean active) {
        this.TITLE = title; this.COMMENT = comment;
        this.headteacherID = headteacher;
        this.CREATED = created;
        this.ID = id;

        teacher = "Alexey Rusakov Pro";
        name = this.TITLE;
    }

    //TODO: constructors
    static private int tmp = 1;
    public GroupEntity(int id, int population) {
        teacher = "Alexey Rusakov";
        this.size = population;
        this.name = "G" + id;
      
        this.TITLE = this.name; this.COMMENT = "";
        this.headteacherID = -1;
        this.CREATED = "now";
        this.ID = id;
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
