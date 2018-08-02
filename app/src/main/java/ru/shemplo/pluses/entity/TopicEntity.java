package ru.shemplo.pluses.entity;


import android.util.Log;

import ru.shemplo.pluses.layout.DiaryMainActivity;
import ru.shemplo.pluses.network.DataProvider;

public class TopicEntity implements MyEntity {

    private final String TITLE;
    private int ID;

    public TopicEntity (int id, String title) {
        this.ID = id;
        this.TITLE = title;
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
