package ru.shemplo.pluses.network.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.FileLock;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import ru.shemplo.pluses.network.AppConnection;
import ru.shemplo.pluses.network.message.AppMessage;
import ru.shemplo.pluses.network.message.CommandMessage;
import ru.shemplo.pluses.network.message.Message;
import ru.shemplo.pluses.struct.Trio;
import ru.shemplo.pluses.util.AnswerConsumer;

public class DataPullService extends Service {

    private static final ConcurrentLinkedQueue <Trio <String, File, AnswerConsumer>>
            TASKS = new ConcurrentLinkedQueue <> ();

    public static void addTask (String command, File write, AnswerConsumer onAnswered) {
        TASKS.add (Trio.mt (command, write, onAnswered));
    }

    @Nullable
    @Override
    public IBinder onBind (Intent intent) {
        return null;
    }

    private static volatile boolean isAlive = false;
    private static volatile Thread thread = null;

    private static Map <Integer, Trio <String, File, AnswerConsumer>>
            needCompute = new HashMap <> ();
    private static AppConnection connection;

    @Override
    public int onStartCommand (Intent intent, int flags, int startId) {
        Log.i ("DPS", "Thread pool: " + isAlive + " (" + flags + ", " + startId + ")");
        if (!isAlive || thread == null) {
            isAlive = true;

            thread = new Thread (new Runnable () {

                @Override
                public void run () {
                    while (true) {
                        try {
                            Message answer = null;
                            while (connection != null
                                    && (answer = connection.pollMessage ()) != null) {
                                if (answer instanceof AppMessage) {
                                    AppMessage appAnswer = (AppMessage) answer;
                                    if (appAnswer.getReplyMessage () != null) {
                                        int id = appAnswer.getReplyMessage ().getID ();
                                        if (!needCompute.containsKey (id)) {
                                            continue;
                                        }

                                        Trio <String, File, AnswerConsumer>
                                                trio = needCompute.remove (id);
                                        File file = trio.S;

                                        // This task doesn't want to write anything
                                        if (file == null) { continue; }

                                        // This task wants to write something
                                        if (!file.exists ()) {
                                            try {
                                                file.createNewFile ();
                                            } catch (IOException ioe) {
                                                ioe.printStackTrace ();
                                            }
                                        }

                                        OutputStream os = null;
                                        FileLock lock = null;
                                        try {
                                            os = new FileOutputStream (file, false);
                                            lock = ((FileOutputStream) os).getChannel ().lock ();
                                            trio.T.consume (os, appAnswer);
                                        } catch (IOException ioe) {
                                            ioe.printStackTrace ();
                                        } finally {
                                            if (os != null) {
                                                try {
                                                    if (lock != null) {
                                                        lock.release ();
                                                    }
                                                    os.close ();
                                                } catch (IOException ioe) {
                                                    ioe.printStackTrace ();
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            Trio <String, File, AnswerConsumer> task = TASKS.poll ();
                            if (task == null) {
                                Thread.sleep (500);
                                continue;
                            }

                            Message message = new CommandMessage
                                    (AppMessage.MessageDirection.CTS, task.F);
                            needCompute.put (message.getID (), task);

                            if (connection == null || !connection.isAlive ()) {
                                connection = new AppConnection (false);
                            }

                            if (connection.isAlive ()) {
                                connection.sendMessage (message);
                            } else {
                                TASKS.add (task); // Rollback task
                                Thread.sleep (500);
                            }
                        } catch (InterruptedException ie) {
                            Log.i ("DPS-T", "I was interrupted");
                            isAlive = false;
                            return;
                        }
                    }
                }

            }, "DataPullService-Thread");
            thread.start ();
        }

        return Service.START_STICKY;
    }

}
