package ru.shemplo.pluses.network;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ru.shemplo.pluses.entity.GroupEntity;
import ru.shemplo.pluses.entity.StudentEntity;
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

    public static List<GroupEntity> getGroups() {
        List<GroupEntity> groups = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 30; i++) {
            int pop = random.nextInt(50), id = i + 1;
            groups.add(new GroupEntity(id, pop));
        }

        return groups;
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

    public static List<StudentEntity> getStudents() {
        List<StudentEntity> students = new ArrayList<>();
        Random random = new Random();
        for(int i = 0; i < 16; i++) {
            students.add(new StudentEntity("Ilya Ivanov " + random.nextInt(228)));
        }
        return students;
    }


    // Next methods is for the support

    public static void onNewDataReceived () {

    }

}
