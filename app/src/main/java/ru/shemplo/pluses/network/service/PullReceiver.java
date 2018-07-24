package ru.shemplo.pluses.network.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.channels.FileLock;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ru.shemplo.pluses.entity.GroupEntity;
import ru.shemplo.pluses.network.AppConnection;
import ru.shemplo.pluses.network.DataProvider;
import ru.shemplo.pluses.network.message.AppMessage;
import ru.shemplo.pluses.network.message.CommandMessage;
import ru.shemplo.pluses.network.message.ListMessage;
import ru.shemplo.pluses.network.message.Message;
import ru.shemplo.pluses.util.LocalConsumer;

import static ru.shemplo.pluses.network.message.AppMessage.MessageDirection.CTS;

public class PullReceiver extends BroadcastReceiver {

    @Override
    public void onReceive (Context context, Intent intent) {
        Log.i ("PR", "Broadcast event for PR");
        Log.i ("PR", new Timestamp (System.currentTimeMillis ()).toString ());

        SharedPreferences preferences =
                context.getSharedPreferences ("PullData", Context.MODE_PRIVATE);
        int value = Integer.parseInt (preferences.getString ("i", "0"));
        Log.i ("PR", "Current value: " + value);

        preferences.edit ().putString ("i", "" + (value + 1)).apply ();
    }

    public static void pullGroups (final File root) {
        List <Message> commands = new ArrayList <> ();
        commands.add (new CommandMessage (CTS,"select groups"));
        LocalConsumer <Message> consumer = new LocalConsumer <Message> () {

            @Override
            public void consume (Message value) {
                if (value instanceof ListMessage) {
                    ListMessage <Integer> list = (ListMessage <Integer>) value;
                    List <Integer> ids = list.getList ();
                    Random random = new Random ();
                    FileOutputStream fos = null;
                    FileLock lock = null;

                    try {
                        File file = new File (root, "groups.bin");
                        Log.i ("PR", "File: " + root + " " + root.isDirectory ());
                        if (!file.isFile ()) {
                            file.createNewFile ();
                        }

                        fos = new FileOutputStream (file);
                        lock = fos.getChannel ().lock ();
                        ObjectOutputStream bos = new ObjectOutputStream (fos);
                        bos.writeInt (ids.size ());

                        for (int id : ids) {
                            int population = 1 + random.nextInt (50);
                            GroupEntity entity = new GroupEntity (id, population);
                            bos.writeObject (entity);
                        }
                    } catch (IOException ioe) {
                        ioe.printStackTrace ();
                    } finally {
                        if (fos != null) {
                            try {
                                lock.release ();
                                fos.close ();
                            } catch (Exception e) {}
                        }
                    }
                }
            }

        };

        AppConnection.sendRequest (commands, false, consumer);
    }

    public static void pullGroupsInfo (List <Integer> ids) {

    }

}
