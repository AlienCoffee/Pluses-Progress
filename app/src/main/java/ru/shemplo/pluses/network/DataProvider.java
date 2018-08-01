package ru.shemplo.pluses.network;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ru.shemplo.pluses.entity.GroupEntity;
import ru.shemplo.pluses.entity.StudentEntity;
import ru.shemplo.pluses.entity.TaskEntity;
import ru.shemplo.pluses.entity.TopicEntity;
import ru.shemplo.pluses.network.message.AppMessage;
import ru.shemplo.pluses.network.message.ControlMessage;
import ru.shemplo.pluses.network.message.ListMessage;
import ru.shemplo.pluses.network.service.DataPullService;
import ru.shemplo.pluses.struct.Pair;
import ru.shemplo.pluses.util.AnswerConsumer;
import ru.shemplo.pluses.util.BytesManip;

public class DataProvider {

    private final File ROOT_DIR;

    public DataProvider (Context context) {
        this.ROOT_DIR = context.getFilesDir ();
    }

    private List <Integer> readFromFile (File file, final int one) {
        List <Integer> out = new ArrayList <> ();

        InputStream is = null;
        try {
            prepareFile (file, one, 0);

            is = new FileInputStream (file);
            if (is.available () < 4) {
                return out;
            }

            byte [] buffer = new byte [4];
            is.read (buffer, 0, buffer.length);
            int size = BytesManip.B2I (buffer);

            for (int i = 0; i < size; i++) {
                is.read (buffer, 0, buffer.length);
                out.add (BytesManip.B2I (buffer));
            }
        } catch (IOException ioe) {} finally {
            if (is != null) {
                try {
                    is.close ();
                } catch (IOException ioe2) {}
            }
        }

        return out;
    }

    private <T> List <T> readFromFiles (String prefix, File dir, List <Integer> ids) {
        List <T> out = new ArrayList <> ();

        for (Integer id : ids) {
            File file = new File (dir, prefix + "_" + id + ".bin");
            InputStream is = null;

            try {
                prepareFile (file, id, 0);

                is = new FileInputStream (file);
                if (is.available () == 0) { continue; }
                try {
                    ObjectInputStream ois = new ObjectInputStream (is);
                    out.add ((T) ois.readObject ());
                } catch (ClassNotFoundException cnfe) {
                    cnfe.printStackTrace ();
                }
            } catch (IOException ioe) {
                if (is != null) {
                    try {
                        is.close ();
                        file.delete ();
                    } catch (IOException ioe2) {}
                }
            }
        }

        return out;
    }

    private void prepareFile (File file, final int one, final int two) throws  IOException {
        if (file.exists () && file.canRead ()) {
            long modified = file.lastModified (), time = 1000 * 60 * 2;
            if (System.currentTimeMillis () - modified < time) { return; }
        }

        if (!file.canRead ()) {
            int tries = 0;
            while (!file.delete ()
                    && tries < 3) {
                tries += 1;
            }
        }
        if (!file.exists ()) {
            int tries = 0;
            while (!file.createNewFile ()
                    && tries < 3) {
                tries += 1;
            }
        }

        String [] patterns = {
            "^groups.bin$",
            "^group_\\d+.bin$",
            "^group_\\d+_students.bin$",
            "^student_\\d+.bin$",
            "^student_\\d+_topics.bin$",
            "^topic_\\d+.bin$",
            "^topic_\\d+_tasks.bin$"
        };

        int index = 0;
        for (String pattern : patterns) {
            Pattern p = Pattern.compile (pattern);
            Matcher m = p.matcher (file.getName ().trim ());

            if (m.find ()) { break; }
            index += 1;
        }

        switch (index) {
            case 0:
                DataPullService.addTask ("select groups", file, new AnswerConsumer () {
                    @Override
                    public void consume (OutputStream os, AppMessage answer) throws IOException {
                        if (answer instanceof ListMessage) {
                            List <Integer> ids = ((ListMessage <Integer>) answer).getList ();
                            os.write (BytesManip.I2B (ids.size ()));
                            for (int id : ids) {
                                os.write (BytesManip.I2B (id));
                            }

                            os.flush ();
                        } else {
                            Log.e ("DP", "Error: " + answer);
                        }
                    }
                });
                break;
            case 1:
                DataPullService.addTask ("select info -about group -id " + one, file,
                    new AnswerConsumer () {
                        @Override
                        public void consume (OutputStream os, AppMessage answer) throws IOException {
                            if (answer instanceof ListMessage) {
                                List <String> info = ((ListMessage <String>) answer).getList ();

                                String title   = info.get (1);
                                String comment = info.get (2);
                                String created = info.get (3);

                                int headteacher = -1;
                                try {
                                    headteacher = Integer.parseInt (info.get (4));
                                } catch (NumberFormatException nfe) { nfe.getMessage (); }
                                boolean active = "1".equals (info.get (5).trim ());

                                ObjectOutputStream oos = new ObjectOutputStream (os);
                                oos.writeObject (new GroupEntity (
                                        one, title, comment, created, headteacher, active));
                                os.flush ();
                            } else {
                                Log.e ("DP", "Error: " + answer);
                            }
                        }
                    });
                break;
            case 2:
            case 4:
                String command = "select " + (index == 2 ? "students" : "topics") + " -id " + one;
                DataPullService.addTask (command, file, new AnswerConsumer () {
                    @Override
                    public void consume (OutputStream os, AppMessage answer)
                            throws IOException {
                        if (answer instanceof ListMessage) {
                            List <Pair <Integer, Integer>> ids
                                = ((ListMessage <Pair <Integer, Integer>>) answer).getList ();

                            List <Integer> current = new ArrayList <> ();
                            for (Pair <Integer, Integer> id : ids) {
                                if (id.S == 0) { current.add (id.F); }
                            }

                            os.write (BytesManip.I2B (current.size ()));
                            for (Integer id : current) {
                                os.write (BytesManip.I2B (id));
                            }

                            os.flush ();
                        } else if (answer instanceof ControlMessage) {
                            ControlMessage control = (ControlMessage) answer;
                            Log.e ("DP", control.getComment ());
                        }
                    }
                });
                break;
            case 3:
                DataPullService.addTask ("select info -about student -id " + one, file,
                    new AnswerConsumer () {
                        @Override
                        public void consume (OutputStream os, AppMessage answer) throws IOException {
                            if (answer instanceof ListMessage) {
                                List <String> info = ((ListMessage <String>) answer).getList ();

                                String firstName = info.get (1);

                                ObjectOutputStream oos = new ObjectOutputStream (os);
                                oos.writeObject (new StudentEntity (one, firstName));
                                os.flush ();
                            } else {
                                Log.e ("DP", "Error: " + answer);
                            }
                        }
                    });
                break;
            case 5:
                DataPullService.addTask ("select info -about topic -id " + one, file,
                        new AnswerConsumer () {
                            @Override
                            public void consume (OutputStream os, AppMessage answer) throws IOException {
                                if (answer instanceof ListMessage) {
                                    List <String> info = ((ListMessage <String>) answer).getList ();

                                    String title = info.get (1);

                                    ObjectOutputStream oos = new ObjectOutputStream (os);
                                    oos.writeObject (new TopicEntity (one, title));
                                    os.flush ();
                                } else {
                                    Log.e ("DP", "Error: " + answer);
                                }
                            }
                        });
                break;
            case 6:
                DataPullService.addTask ("select tasks -topic " + one, file,
                    new AnswerConsumer () {
                        @Override
                        public void consume (OutputStream os, AppMessage answer)
                                throws IOException {
                            if (answer instanceof ListMessage) {
                                ListMessage <Pair <Integer, String>> list
                                    = (ListMessage <Pair <Integer, String>>) answer;
                                List <Pair <Integer, String>> tasks = list.getList ();
                                ObjectOutputStream oos = new ObjectOutputStream (os);

                                oos.writeInt (tasks.size ());
                                for (Pair <Integer, String> task : tasks) {
                                    oos.writeObject (task);
                                }

                                os.flush ();
                            } else if (answer instanceof ControlMessage) {
                                ControlMessage control = (ControlMessage) answer;
                                Log.e ("DP", control.getComment ());
                            }
                        }
                });
                break;
        }
    }

    // Next methods is for the support
    public List <GroupEntity> getGroups () {
        File groupsFile = new File (ROOT_DIR, "groups.bin");
        List <Integer> ids = readFromFile (groupsFile, 0);
        return readFromFiles ("group", ROOT_DIR, ids);
    }

    public List <StudentEntity> getStudents (int groupID) {
        File studentsFile = new File (ROOT_DIR, "group_" + groupID + "_students.bin");
        List <Integer> ids = readFromFile (studentsFile, groupID);
        return readFromFiles ("student", ROOT_DIR, ids);
    }

    public List <TopicEntity> getTopics (int studentID) {
        File topicsFile = new File (ROOT_DIR, "student_" + studentID + "_topics.bin");
        List <Integer> ids = readFromFile (topicsFile, studentID);
        return readFromFiles ("topic", ROOT_DIR, ids);
    }

    public List <TaskEntity> getTasks (int topicID) {
        File tasksFile = new File (ROOT_DIR, "topic_" + topicID + "_tasks.bin");
        List <TaskEntity> out = new ArrayList <> ();
        InputStream is = null;

        try {
            prepareFile (tasksFile, topicID, 0);
            is = new FileInputStream (tasksFile);

            ObjectInputStream ois = new ObjectInputStream (is);
            int size = ois.readInt ();

            for (int i = 0; i < size; i++) {
                try {
                    Object tmp = ois.readObject ();
                    Pair <Integer, String> task = (Pair <Integer, String>) tmp;
                    out.add (new TaskEntity (task.F, task.S, topicID));
                } catch (ClassNotFoundException cnfe) {
                    cnfe.getMessage ();
                }
            }
        } catch (IOException ioe) {
            if (is != null) {
                try {
                    is.close ();
                } catch (IOException ioe2) { ioe2.getMessage (); }
            }
        }

        return out;
    }

}
