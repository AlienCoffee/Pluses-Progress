package ru.shemplo.pluses.util;


import java.io.IOException;
import java.io.OutputStream;

import ru.shemplo.pluses.network.message.AppMessage;

public interface AnswerConsumer {

    public void consume (OutputStream os, AppMessage answer) throws IOException;

}
