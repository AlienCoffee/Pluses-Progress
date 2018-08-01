package ru.shemplo.pluses.entity;


public class TopicEntity implements MyEntity {

    private final String TITLE;

    public TopicEntity (int id, String title) {
        this.TITLE = title;
    }

    public String toString () {
        return getTitle ();
    }

    public String getTitle () {
        return TITLE;
    }

}
