package ru.shemplo.pluses.entity;

public class TryEntity {

    public final int STUDENT, GROUP, TOPIC, TASK, VERDICT;

    public TryEntity (int studentID, int groupID, int topicID, int taskID, int verdict) {
        this.STUDENT = studentID; this.GROUP = groupID;
        this.TASK = taskID; this.VERDICT = verdict;
        this.TOPIC = topicID;
    }

}
