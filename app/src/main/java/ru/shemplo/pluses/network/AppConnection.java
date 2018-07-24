package ru.shemplo.pluses.network;

import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.channels.FileLock;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;

import ru.shemplo.pluses.entity.GroupEntity;
import ru.shemplo.pluses.network.message.AppMessage;
import ru.shemplo.pluses.network.message.CommandMessage;
import ru.shemplo.pluses.network.message.ListMessage;
import ru.shemplo.pluses.network.message.Message;
import ru.shemplo.pluses.network.message.PPMessage;
import ru.shemplo.pluses.util.BytesManip;
import ru.shemplo.pluses.util.LocalConsumer;

import static ru.shemplo.pluses.network.message.AppMessage.MessageDirection.CTS;
import static ru.shemplo.pluses.network.message.AppMessage.MessageDirection.STC;

public class AppConnection {

    private boolean isAlive = true;

    private final ConcurrentLinkedQueue <Message> INPUT, OUTPUT;
    private final Thread THREAD;

    public AppConnection (final boolean keepAlive) {
        this.OUTPUT = new ConcurrentLinkedQueue <> ();
        this.INPUT = new ConcurrentLinkedQueue <> ();

        this.THREAD = new Thread (new Runnable () {

            @Override
            public void run () {
                // Log.i ("AC", "Thread started");

                Socket socket = null;
                try {
                    socket = new Socket ("shemplo.ru", 1999);
                    final OutputStream OS = socket.getOutputStream ();
                    final InputStream IS = socket.getInputStream ();
                    int reserved = -1;

                    while (isAlive && socket.isConnected ()) {
                        boolean hasTasks = false;
                        if (IS.available () >= 4 && reserved == -1) {
                            byte [] buffer = new byte [4];
                            int read = IS.read (buffer, 0, buffer.length);
                            if (read == -1) {
                                isAlive = false;
                                break;
                            }

                            reserved = BytesManip.B2I (buffer);
                            Log.i ("AC", "Reserved: " + reserved);
                            hasTasks = true;
                        } else if (reserved != -1 && IS.available () >= reserved) {
                            byte [] buffer = new byte [reserved];
                            int read = IS.read (buffer, 0, buffer.length);
                            if (read == -1) {
                                isAlive = false;
                                break;
                            }

                            try {
                                // Assumed that full object transported
                                InputStream is = new ByteArrayInputStream (buffer);
                                ObjectInputStream ois = new ObjectInputStream (is);
                                Object tmp = ois.readObject ();

                                if (tmp instanceof PPMessage) {
                                    PPMessage ping = (PPMessage) tmp;
                                    if (PPMessage.Ping.PING.equals (ping.VALUE) && keepAlive) {
                                        PPMessage pong = new PPMessage (PPMessage.Ping.PONG);
                                        sendMessage (OS, pong);
                                    } else if (PPMessage.Ping.BUY.equals (ping.VALUE)) {
                                        isAlive = false;
                                    }
                                } else if (tmp instanceof Message) {
                                    Message message = (Message) tmp;
                                    INPUT.add (message);
                                }
                            } catch (ClassNotFoundException cnfe) {
                                // It happens -> just ignore it
                            }

                            reserved = -1;
                            hasTasks = true;
                        }

                        Message out = OUTPUT.poll ();
                        if (out != null) {
                            sendMessage (OS, out);
                            hasTasks = true;

                            // This is costyl but I don't know how to fix it
                            if (out instanceof CommandMessage) {
                                CommandMessage command = (CommandMessage) out;
                                if ("exit".equals (command.getCommand ().trim ())) {
                                    isAlive = false;
                                    break;
                                }
                            }
                        }

                        if (!hasTasks) {
                            Thread.sleep (500);
                        }
                    }
                } catch (IOException | InterruptedException ie) {
                    Log.i ("AC", "Failed to connect");
                    if (socket != null) {
                        try {
                            socket.close ();
                        } catch (IOException ioe) {
                            ioe.printStackTrace ();
                        }
                    }
                    isAlive = false;
                }

                // Log.i ("AC", "Thread stopped");
            }

        }, "AppConnection-Thread");
        THREAD.start ();
    }

    private static void sendMessage (OutputStream os, Message message) throws IOException {
        if (message == null) {
            return; // Message is empty or has invalid direction
        } else if (message instanceof AppMessage) {
            AppMessage app = (AppMessage) message;
            if (!CTS.equals (app.getDirection ())) {
                return;  // Message is empty or has invalid direction
            }
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream ();
        ObjectOutputStream oos = new ObjectOutputStream (baos);
        oos.writeObject (message);
        oos.flush ();

        // Fetching serialized object to bytes array
        byte [] data   = baos.toByteArray ();
        byte [] length = BytesManip.I2B (data.length);

        os.write (length);
        os.write (data);
        os.flush ();

        // Log.i ("AC", "Message sent");
    }

    public boolean isAlive () {
        return isAlive;
    }

    public void sendMessage (Message message) {
        if (!isAlive ()) { return; }
        OUTPUT.add (message);
    }

    public int getInputMessages () {
        return INPUT.size ();
    }

    public Message pollMessage () {
        return INPUT.poll ();
    }

    public void rollbackMessage (Message message) {
        this.INPUT.add (message);
    }

    public void close () throws Exception {
        sendMessage (new CommandMessage (CTS, "exit"));
    }

    public static void sendRequest (final List <Message> messages, final boolean keepAlive,
                                    final LocalConsumer <Message> callback) {
        Thread thread = new Thread (new Runnable () {

            @Override
            public void run () {
                AppConnection connection = new AppConnection (keepAlive);
                ConcurrentMap <Integer, Message> map = new ConcurrentHashMap <> ();

                for (Message message : messages) { map.put (message.getID (), message); }
                for (Message message : messages) { connection.sendMessage (message); }

                while (connection.isAlive ()) {
                    Message answer = connection.pollMessage ();
                    if (answer == null) {
                        try {
                            Thread.sleep (250); continue;
                        } catch (InterruptedException ie) { return; }
                    }

                    if (answer instanceof  AppMessage) {
                        AppMessage appMessage = (AppMessage) answer;
                        Message reply = appMessage.getReplyMessage ();

                        // Answer to unknown for this connection (session) message
                        if (reply != null && !map.containsKey (reply.getID ())) {
                            continue;
                        } else if (reply != null) {
                            map.remove (reply.getID ());
                        }
                    }

                    callback.consume (answer);
                    if (map.isEmpty ()) {
                        try {
                            connection.close ();
                        } catch (Exception e) {}
                    }
                }
            }
        }, "Send-Request-Thread");
        thread.start ();
    }

}
