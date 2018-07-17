package ru.shemplo.pluses.network;

import android.content.Context;
import android.provider.ContactsContract;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.net.ssl.SSLContextSpi;

import ru.shemplo.pluses.entity.GroupEntity;
import ru.shemplo.pluses.util.BytesManip;

public class DataProvider {

    private final File ROOT_DIR;

    public DataProvider (Context context) {
        this.ROOT_DIR = context.getFilesDir ();
    }

    public List <GroupEntity> getGroups () {
        File groupsFile = new File (ROOT_DIR, "groups.bin");
        List <GroupEntity> out = new ArrayList <> ();

        if (!groupsFile.exists ()
                || !groupsFile.canRead ()) {
            return out;
        }

        FileInputStream fis = null;
        FileLock lock = null;
        try {
            fis = new FileInputStream (groupsFile);
            lock = fis.getChannel ().lock ();

            ObjectInputStream bis = new ObjectInputStream (fis);
            int size = bis.readInt ();
            Object tmp = null;

            for (int i = 0; i < size; i++) {
                tmp = bis.readObject ();
                if (tmp instanceof  GroupEntity) {
                    out.add ((GroupEntity) tmp);
                }
            }
        } catch (IOException | ClassNotFoundException ioe) {
            // Nothing to do (just handle and ignore)
        } finally {
            try {
                lock.release ();
                fis.close ();
            } catch (Exception e) {}
        }

        return new ArrayList <> ();
    }

}
