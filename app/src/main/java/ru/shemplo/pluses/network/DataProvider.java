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
import ru.shemplo.pluses.entity.TaskEntity;
import ru.shemplo.pluses.entity.TopicEntity;
import ru.shemplo.pluses.network.service.PullReceiver;
import ru.shemplo.pluses.util.BytesManip;

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
            //                                                 5 minutes
            long modified = groupsFile.lastModified (), time = 1000 * 60 * 5;
            if (System.currentTimeMillis () - modified > time) {
                PullReceiver.pullGroups (ROOT_DIR);
            }
        }

        FileInputStream fis = null;
        try {
            fis = new FileInputStream (groupsFile);
            byte [] buffer = new byte [4];
            fis.read (buffer, 0, buffer.length);
            int size = BytesManip.B2I (buffer);

            Log.i ("DP", "Size: " + size + " (size: " + groupsFile.length () + ")");
            List <Integer> ids = new ArrayList <> ();
            boolean needPullData = false;
            for (int i = 0; i < size; i++) {
                fis.read (buffer, 0, buffer.length);
                int id = BytesManip.B2I (buffer);
                ids.add (id);

                File groupFile = new File (ROOT_DIR, "group_" + id + ".bin");
                if (!groupFile.exists () || !groupFile.canRead ()) {
                    Log.i ("DP", "File " + groupFile + " not found");
                    needPullData = true;
                    continue;
                }

                FileInputStream dfis = new FileInputStream (groupFile);
                try {
                    ObjectInputStream ois = new ObjectInputStream (dfis);
                    Object object = ois.readObject ();
                    if (object instanceof GroupEntity) {
                        GroupEntity entity = (GroupEntity) object;
                        out.add (entity);
                    }
                } catch (ClassNotFoundException cnfe) {
                    cnfe.printStackTrace ();
                    needPullData = true;
                }

                dfis.close ();
            }

            if (needPullData) {
                PullReceiver.pullGroupsInfo (ROOT_DIR, ids);
            }
        } catch (IOException ioe) {
            // Nothing to do (just handle and ignore)
            ioe.printStackTrace ();
        } finally {
            try {
                fis.close ();
            } catch (Exception e) {}
        }

        return out;
    }

    public List <TopicEntity> getTopics () {
        List <TopicEntity> topics = new ArrayList <> ();
        List <TaskEntity> tasks = new ArrayList <> ();
        tasks.add (new TaskEntity ("1"));
        tasks.add (new TaskEntity ("2"));
        tasks.add (new TaskEntity ("3"));
        tasks.add (new TaskEntity ("4"));
        tasks.add (new TaskEntity ("5"));
        tasks.add (new TaskEntity ("6"));
        tasks.add (new TaskEntity ("7"));
        tasks.add (new TaskEntity ("8"));
        tasks.add (new TaskEntity ("9"));
        topics.add (new TopicEntity ("1", tasks));

        tasks = tasks.subList (0, tasks.size () - 1);
        topics.add (new TopicEntity ("2", tasks));

        tasks = tasks.subList (0, tasks.size () - 1);
        topics.add (new TopicEntity ("3", tasks));
        return topics;
    }

}
