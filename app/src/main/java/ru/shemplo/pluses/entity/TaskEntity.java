package ru.shemplo.pluses.entity;


public class TaskEntity implements MyEntity {

    public final int TOPIC_ID, ID;
    public final String TITLE;

    public TaskEntity (int id, String title, int topicID) {
        this.TOPIC_ID = topicID; this.ID = id;
        this.TITLE = title;
    }

}
