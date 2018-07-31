package ru.shemplo.pluses.network.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.channels.FileLock;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import ru.shemplo.pluses.entity.GroupEntity;
import ru.shemplo.pluses.entity.StudentEntity;
import ru.shemplo.pluses.network.AppConnection;
import ru.shemplo.pluses.network.message.CommandMessage;
import ru.shemplo.pluses.network.message.ControlMessage;
import ru.shemplo.pluses.network.message.ListMessage;
import ru.shemplo.pluses.network.message.Message;
import ru.shemplo.pluses.struct.Pair;
import ru.shemplo.pluses.util.BytesManip;
import ru.shemplo.pluses.util.LocalConsumer;

import static ru.shemplo.pluses.network.message.AppMessage.MessageDirection.CTS;

public class PullReceiver extends BroadcastReceiver {

    @Override
    public void onReceive (Context context, Intent intent) {
        Log.i ("PR", "Broadcast event for PR");
        Log.i ("PR", new Timestamp (System.currentTimeMillis ()).toString ());
    }

    public static void pullGroups (final File root) {
        final List <Message> commands = new ArrayList <> ();
        commands.add (new CommandMessage (CTS,"select groups"));
        LocalConsumer <Message> consumer = new LocalConsumer <Message> () {

            @Override
            public void consume (Message value) {
                if (value instanceof ListMessage) {
                    List <Integer> ids = ((ListMessage <Integer>) value).getList ();
                    PullReceiver.pullGroupsInfo (root, ids);
                    FileOutputStream fos = null;
                    FileLock lock = null;

                    try {
                        File file = new File (root, "groups.bin");
                        //Log.i ("PR", "Writing to file " + file);
                        if (!file.isFile ()) {
                            file.createNewFile ();
                        }

                        fos = new FileOutputStream (file);
                        lock = fos.getChannel ().lock ();

                        fos.write (BytesManip.I2B (ids.size ())); // size of list of groups
                        for (int id : ids) { fos.write (BytesManip.I2B (id)); }
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
                } else {
                    Log.i ("PR", "" + value);
                }
            }

        };

        AppConnection.sendRequest (commands, false, consumer, true);
    }

    public static void pullGroupsInfo (final File root, final List <Integer> ids) {
        final List <Message> commands = new ArrayList <> ();
        for (Integer id : ids) {
            String command = "select info -about group -id " + id;
            commands.add (new CommandMessage (null, CTS, command, id));
            Log.i ("PR", command + " " + id);
        }

        LocalConsumer <Message> consumer = new LocalConsumer <Message> () {

            @Override
            public void consume (Message value) {
                if (value instanceof ListMessage) {
                    ListMessage <String> message = (ListMessage <String>) value;

                    CommandMessage command = (CommandMessage) message.getReplyMessage ();
                    List <String> info = message.getList ();
                    int id = command.getValue ();

                    File groupFile = new File (root, "group_" + id + ".bin");
                    FileOutputStream fos = null;
                    FileLock lock = null;

                    Log.i ("PR", info + " " + groupFile);
                    try {
                        if (!groupFile.exists ()) {
                            //Log.i ("PR", "Create file " + groupFile);
                            groupFile.createNewFile ();
                        }

                        fos = new FileOutputStream (groupFile, false);
                        lock = fos.getChannel ().lock ();

                        String title = info.get (1);
                        String comment = info.get (2);
                        String created = info.get (3);

                        int headteacher = -1;
                        try {
                            headteacher = Integer.parseInt (info.get (4));
                        } catch (NumberFormatException nfe) {}
                        boolean active = "1".equals (info.get (5).trim ());

                        ObjectOutputStream oos = new ObjectOutputStream (fos);
                        oos.writeObject (new GroupEntity (
                                id, title, comment, created, headteacher, active));

                        fos.flush ();
                    } catch (IOException ioe) {
                        // Just handle -> nothing to do
                        ioe.printStackTrace ();
                    } finally {
                        if (fos != null) {
                            try {
                                lock.release ();
                                fos.close ();
                            } catch (Exception e) {}
                        }
                    }
                } else {
                    ControlMessage control = (ControlMessage) value;
                    Log.i ("PR", control.getComment ());
                }
            }

        };

        AppConnection.sendRequest (commands, false, consumer, true);
    }

    public static void pullStudents (final File root, int groupID) {
        final List <Message> commands = new ArrayList <> ();
        commands.add (new CommandMessage (null, CTS,
                "select students -id " + groupID, groupID));
        LocalConsumer <Message> consumer = new LocalConsumer <Message> () {

            @Override
            public void consume (Message value) {
                if (value instanceof ListMessage) {
                    ListMessage <Pair <Integer, Integer>> message =
                            (ListMessage <Pair <Integer, Integer>>) value;

                    CommandMessage command = (CommandMessage) message.getReplyMessage ();
                    List <Pair <Integer, Integer>> ids = message.getList ();
                    int groupID = command.getValue ();
                    FileOutputStream fos = null;
                    FileLock lock = null;

                    try {
                        File file = new File (root, "group_" + groupID + "_students.bin");
                        //Log.i ("PR", "Writing to file " + file);
                        if (!file.isFile ()) {
                            file.createNewFile ();
                        }

                        fos = new FileOutputStream (file, false);
                        lock = fos.getChannel ().lock ();

                        List <Integer> current = new ArrayList <> ();
                        for (Pair <Integer, Integer> id : ids) {
                            if (id.S == 0) { current.add (id.F); }
                        }

                        fos.write (BytesManip.I2B (current.size ()));
                        for (Integer id : current) {
                            fos.write (BytesManip.I2B (id));
                        }

                        fos.flush ();

                        PullReceiver.pullStudentsInfo (root, current);
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
                } else {
                    Log.i ("PR", "" + value);
                }
            }

        };

        AppConnection.sendRequest (commands, false, consumer, true);
    }

    public static void pullStudentsInfo (final File root, final List <Integer> ids) {
        final List <Message> commands = new ArrayList <> ();
        for (Integer id : ids) {
            String command = "select info -about student -id " + id;
            commands.add (new CommandMessage (null, CTS, command, id));
            Log.i ("PR", command + " " + id);
        }

        LocalConsumer <Message> consumer = new LocalConsumer <Message> () {

            @Override
            public void consume (Message value) {
                if (value instanceof ListMessage) {
                    ListMessage <String> message = (ListMessage <String>) value;

                    CommandMessage command = (CommandMessage) message.getReplyMessage ();
                    List <String> info = message.getList ();
                    int id = command.getValue ();

                    File groupFile = new File (root, "student_" + id + ".bin");
                    FileOutputStream fos = null;
                    FileLock lock = null;

                    Log.i ("PR", info + " " + groupFile);
                    try {
                        if (!groupFile.exists ()) {
                            //Log.i ("PR", "Create file " + groupFile);
                            groupFile.createNewFile ();
                        }

                        fos = new FileOutputStream (groupFile, false);
                        lock = fos.getChannel ().lock ();



                        ObjectOutputStream oos = new ObjectOutputStream (fos);
                        oos.writeObject (new StudentEntity ("" + id));

                        fos.flush ();
                    } catch (IOException ioe) {
                        // Just handle -> nothing to do
                        ioe.printStackTrace ();
                    } finally {
                        if (fos != null) {
                            try {
                                lock.release ();
                                fos.close ();
                            } catch (Exception e) {}
                        }
                    }
                } else {
                    ControlMessage control = (ControlMessage) value;
                    Log.i ("PR", control.getComment ());
                }
            }

        };

        AppConnection.sendRequest (commands, false, consumer, true);
    }

}
