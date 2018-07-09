package tk.pluses.plusesprogress.entities;


import java.util.List;
import java.util.Random;

public class GroupEntity {
    private final String name, teacher;
    private int size;
    //TODO: any links to students?

    //TODO: remove this
    static private int tmp = 1;
    public GroupEntity() {
        name = "G" + tmp;
        tmp++;
        teacher = "Alexey Rusakov";
        size = 42;
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
