package ru.shemplo.pluses.network;

import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

import ru.shemplo.pluses.network.message.AppMessage;
import ru.shemplo.pluses.network.message.Message;
import ru.shemplo.pluses.network.message.PPMessage;
import ru.shemplo.pluses.util.BytesManip;

import static ru.shemplo.pluses.network.message.AppMessage.MessageDirection.CTS;

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
                Socket socket = null;
                try {
                    socket = new Socket ("shemplo.ru", 1999);
                    final OutputStream OS = socket.getOutputStream ();
                    final InputStream IS = socket.getInputStream ();
                    int reserved = -1;

                    while (isAlive) {
                        Log.i ("AppConnection", INPUT.size ()
                                + " " + OUTPUT.size () +  " " + IS.available ());
                        boolean hasTasks = false;
                        if (IS.available () >= 4 && reserved == -1) {
                            byte [] buffer = new byte [4];
                            int read = IS.read (buffer, 0, buffer.length);
                            if (read == -1) {
                                isAlive = false;
                                break;
                            }

                            reserved = BytesManip.B2I (buffer);
                            Log.i ("AppConnection", "Reserved: " + reserved);
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
                        }

                        if (!hasTasks) {
                            Log.i ("AppConnection", "Nothing to do");
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
        THREAD.interrupt ();
    }

}
