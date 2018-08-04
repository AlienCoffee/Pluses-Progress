package ru.shemplo.pluses.entity;


public class TopicEntity implements MyEntity {

    private static final long serialVersionUID = 0L;

    private final String TITLE;
    public final int ID;

    public TopicEntity (int id, String title) {
        this.TITLE = title;
        this.ID = id;
    }

    public String toString () {
        return getTitle ();
    }

    public String getTitle () {
        return TITLE;
    }

    public int getID () {
        return ID;
    }

}
