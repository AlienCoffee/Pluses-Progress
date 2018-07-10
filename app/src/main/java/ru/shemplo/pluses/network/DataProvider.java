package ru.shemplo.pluses.network;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ru.shemplo.pluses.entity.GroupEntity;

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

    public static List <GroupEntity> getGroups () {
        List <GroupEntity> groups = new ArrayList <> ();
        Random random = new Random ();
        for (int i = 0; i < 30; i++) {
            int pop = random.nextInt (50), id = i + 1;
            groups.add (new GroupEntity (id, pop));
        }

        return groups;
    }

    // Next methods is for the support

    public static void onNewDataReceived () {

    }

}
