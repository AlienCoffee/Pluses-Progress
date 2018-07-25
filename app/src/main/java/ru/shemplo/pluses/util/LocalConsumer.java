package ru.shemplo.pluses.util;

import android.os.Message;

import java.util.Set;

public interface LocalConsumer <T> {

    public void consume (T value);

}
