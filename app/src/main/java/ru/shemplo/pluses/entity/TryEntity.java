package ru.shemplo.pluses.entity;

import java.io.Serializable;

public class TryEntity implements Serializable {

    public final int STUDENT, GROUP, TOPIC, TASK, VERDICT;

    public TryEntity (int studentID, int groupID, int topicID, int taskID, int verdict) {
        this.STUDENT = studentID; this.GROUP = groupID;
        this.TASK = taskID; this.VERDICT = verdict;
        this.TOPIC = topicID;
    }

    public String toString () {
        return "<" + TOPIC + ", " + TASK + ", " + (VERDICT == 1) + ">";
    }

}
