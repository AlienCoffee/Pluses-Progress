package ru.shemplo.pluses.layout;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import ru.shemplo.pluses.entity.GroupEntity;
import ru.shemplo.pluses.R;
import ru.shemplo.pluses.network.AppConnection;
import ru.shemplo.pluses.adapter.TaskAdapter;
import ru.shemplo.pluses.network.DataProvider;
import ru.shemplo.pluses.network.message.AppMessage;
import ru.shemplo.pluses.network.message.CommandMessage;
import ru.shemplo.pluses.network.message.ControlMessage;
import ru.shemplo.pluses.network.service.DataPullService;

public class DiaryMainActivity extends AppCompatActivity {

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
/*
        final Intent intentDataPullStart = new Intent (this, DataPullService.class);
        final Intent intentDataPullStop = new Intent (this, DataPullService.class);
        final SharedPreferences preferences =
                getSharedPreferences ("PullData", Context.MODE_PRIVATE);
        this.context = this;
        */

        setContentView(R.layout.testing_dummy);
        Button button = (Button) findViewById (R.id.button);
        button.setOnClickListener (new View.OnClickListener () {

            @Override
            public void onClick (View v) {
                Log.i ("DMA", "Click on start button");
                startService (intentDataPullStart);
            }

        });

        button = (Button) findViewById (R.id.button_stop);
        button.setOnClickListener (new View.OnClickListener () {

            @Override
            public void onClick (View v) {
                Log.i ("DMA", "Click on stop button");
                stopService (intentDataPullStop);
            }

        });

        button = (Button) findViewById (R.id.button_info);
        button.setOnClickListener (new View.OnClickListener () {

            @Override
            public void onClick (View v) {
                int value = Integer.parseInt (preferences.getString ("i", "0"));
                Log.i ("DMA", "Current value: " + value);
            }

        });

        button = (Button) findViewById (R.id.button_send);
        button.setOnClickListener (new View.OnClickListener () {

            @Override
            public void onClick (View v) {
                DataProvider provider = new DataProvider (context);
                StringBuilder sb = new StringBuilder ();

                for (GroupEntity entity : provider.getGroups ()) {
                    sb.append ("'");
                    sb.append (entity.TITLE);
                    sb.append ("' ");
                }

                Log.i ("DMA", sb.toString ().trim ());
            }

        });

        /*
        setContentView(R.layout.frame_layout);

        GroupsFragment groupsFragment = new GroupsFragment();
        groupsFragment.setContext(this);
        groupsFragment.setData(DataProvider.getGroups());

        TopicsFragment topicsFragment = new TopicsFragment();
        topicsFragment.setContext(this);
        topicsFragment.setData(DataProvider.getTopics());
        //TODO: add favourites
        StudentsFragment studentsFragment = new StudentsFragment();
        studentsFragment.setContext(this);
        studentsFragment.setData(DataProvider.getStudents());

        setContentView(R.layout.frame_layout);
        //getFragmentManager().beginTransaction().add(R.id.main_frame, groupsFragment).commit();
        //getFragmentManager().beginTransaction().add(R.id.main_frame, topicsFragment).commit();
        getFragmentManager().beginTransaction().add(R.id.main_frame, studentsFragment).commit();

/*        setContentView(R.layout.topic_item);
        RecyclerView test = (RecyclerView) findViewById(R.id.task_recycler_view);
        test.setLayoutManager(new GridLayoutManager(this, 8));
        test.setAdapter(new TaskAdapter(DataProvider.getTopics().get(10).getTasks()));*/


    }

    public void something(View view) {
        Log.e("ss", "qq");
    }
}
