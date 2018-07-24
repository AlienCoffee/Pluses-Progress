package ru.shemplo.pluses.network;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.channels.FileLock;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import ru.shemplo.pluses.entity.GroupEntity;
import ru.shemplo.pluses.network.service.PullReceiver;

public class DataProvider {

    private final File ROOT_DIR;

    public DataProvider (Context context) {
        this.ROOT_DIR = context.getFilesDir ();
    }


    // Next methods is for the support
    public List <GroupEntity> getGroups () {
        File groupsFile = new File (ROOT_DIR, "groups.bin");
        List <GroupEntity> out = new ArrayList <> ();

        if (!groupsFile.exists () || !groupsFile.canRead ()) {
            PullReceiver.pullGroups (ROOT_DIR);
            return out;
        } else {
            //                                                 1 hour
            long modified = groupsFile.lastModified (), time = 1000 * 60 * 60;
            if (System.currentTimeMillis () - modified > time) {
                PullReceiver.pullGroups (ROOT_DIR);
            }
        }

        FileInputStream fis = null;
        try {
            fis = new FileInputStream (groupsFile);
            ObjectInputStream bis = new ObjectInputStream (fis);
            int size = bis.readInt ();
            Object tmp = null;

            Log.i ("DP", "Size: " + size + " (size: " + groupsFile.length () + ")");
            for (int i = 0; i < size; i++) {
                tmp = bis.readObject ();
                if (tmp instanceof  GroupEntity) {
                    out.add ((GroupEntity) tmp);
                }
            }
        } catch (IOException | ClassNotFoundException ioe) {
            // Nothing to do (just handle and ignore)
            ioe.printStackTrace ();
        } finally {
            try {
                fis.close ();
            } catch (Exception e) {}
        }

        return out;
    }

}
