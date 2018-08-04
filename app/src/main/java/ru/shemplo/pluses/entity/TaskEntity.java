package ru.shemplo.pluses.entity;


public class TaskEntity implements MyEntity {

    private static final long serialVersionUID = 0L;

    public final int TOPIC_ID, ID;
    public final String TITLE;

    private boolean isSolved = false;

    public TaskEntity (int id, String title, int topicID) {
        this.TOPIC_ID = topicID; this.ID = id;
        this.TITLE = title;
    }

    public void setState (boolean isSolved) {
        this.isSolved = isSolved;
    }

    public boolean isSolved () {
        return isSolved;
    }

}
