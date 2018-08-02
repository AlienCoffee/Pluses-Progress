package ru.shemplo.pluses.network;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.channels.FileLock;

import ru.shemplo.pluses.entity.TryEntity;

public class DataSupplier {

    private final File ROOT_DIR;

    public DataSupplier (Context context) {
        this.ROOT_DIR = context.getFilesDir ();
    }

    public void insertTry (int studentID, int groupID, int topicID, int taskID, int verdict) {
        File triesFile = new File (ROOT_DIR, "local_student_" + studentID + "_tries.bin");
        if (!triesFile.exists ()) {
            try {
                int tries = 0;
                while (!triesFile.createNewFile ()
                        && tries < 3) {
                    tries += 1;
                }
            } catch (IOException ioe) { ioe.getMessage (); }
        }

        OutputStream os = null;
        FileLock lock = null;
        try {
            os = new FileOutputStream (triesFile, true);
            lock = ((FileOutputStream) os).getChannel ().lock ();

            ObjectOutputStream oos = new ObjectOutputStream (os);
            TryEntity entity = new TryEntity (studentID, groupID, topicID, taskID, verdict);

            oos.writeObject (entity);
            os.flush ();
        } catch (IOException ioe) {
            ioe.printStackTrace ();
        } finally {
            if (os != null) {
                try {
                    lock.release ();
                    os.close ();
                } catch (IOException ioe) { ioe.getMessage (); }
            }
        }
    }

}
