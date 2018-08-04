package ru.shemplo.pluses.network;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ru.shemplo.pluses.entity.TryEntity;

public class DataSupplier {

    private final DataProvider PROVIDER;
    private final File ROOT_DIR;

    public DataSupplier (Context context) {
        this.PROVIDER = new DataProvider (context);
        this.ROOT_DIR = context.getFilesDir ();
    }

    public void insertTry (int studentID, int groupID, int topicID, int taskID, int verdict) {
        File triesFile = new File (ROOT_DIR, "local_student_" + studentID + "_tries.bin");
        List <TryEntity> localTries = PROVIDER.readManyFromFile (triesFile, 0, 0);

        List <TryEntity> rest = new ArrayList <> ();
        Set <String> exists = new HashSet <> ();

        TryEntity entity = new TryEntity (studentID, groupID, topicID, taskID, verdict);
        exists.add (entity.TOPIC + " " + entity.TASK);
        rest.add (entity);

        for (TryEntity attempt : localTries) {
            if (!exists.contains (attempt.TOPIC + " " + attempt.TASK)) {
                exists.add (attempt.TOPIC + " " + attempt.TASK);
                rest.add (attempt);
            }
        }

        FileOutputStream fos = null;
        FileLock lock = null;

        try {
            fos = new FileOutputStream (triesFile);
            lock = fos.getChannel ().lock ();

            ObjectOutputStream oos = new ObjectOutputStream (fos);
            oos.writeInt (rest.size ());
            for (int i = 0; i < rest.size (); i++) {
                oos.writeObject (rest.get (i));
            }

            fos.flush ();
        } catch (IOException ioe) {
            ioe.printStackTrace ();
        } finally {
            if (fos != null) {
                try {
                    if (lock != null) {
                        lock.release ();
                    }
                    fos.close ();
                } catch (IOException ioe) {
                    ioe.printStackTrace ();
                }
            }
        }
    }

}
