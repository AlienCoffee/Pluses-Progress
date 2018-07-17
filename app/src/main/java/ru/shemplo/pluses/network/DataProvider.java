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
import ru.shemplo.pluses.entity.TaskEntity;
import ru.shemplo.pluses.entity.TopicEntity;

public class DataProvider {

    // This class will provide all available information
    // It's possible that some data is not loaded and
    // it will be replaced with NULL value or default stub

    /////////////////////////////////////////////////////
    // ----------------------------------------------- //
    // Here is section of methods that provides access //
    // (Recommendation: call only these methods if you //
    //         are not sure in purposes of this class) //
    // ----------------------------------------------- //
    /////////////////////////////////////////////////////

    public static List<GroupEntity> getGroups_dep () {
        List<GroupEntity> groups = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 30; i++) {
            int pop = random.nextInt(50), id = i + 1;
            groups.add(new GroupEntity(id, pop));
        }
    }

    private final File ROOT_DIR;

    public DataProvider (Context context) {
        this.ROOT_DIR = context.getFilesDir ();
    }

    //TODO: constructor of StudentEntity?
    public static List<TopicEntity> getTopics() {
        List<TopicEntity> topics = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 15; i++) {
            List<TaskEntity> tasks = new ArrayList<>();
            for (int j = 0; j < i; j++) {
                tasks.add(new TaskEntity(j + "", random.nextBoolean()));
            }
            topics.add(new TopicEntity("Topic" + i, tasks));
        }
        return topics;
    }


    // Next methods is for the support
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
