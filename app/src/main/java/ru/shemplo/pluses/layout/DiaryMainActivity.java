package ru.shemplo.pluses.layout;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import ru.shemplo.pluses.R;
import ru.shemplo.pluses.entity.GroupEntity;
import ru.shemplo.pluses.entity.StudentEntity;
import ru.shemplo.pluses.network.DataProvider;
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
                */
        this.context = this;

        startService (new Intent (context, DataPullService.class));
        setContentView(R.layout.testing_dummy);

        Button button = (Button) findViewById (R.id.switch_frame_button);
        button.setOnClickListener (new View.OnClickListener () {

            @Override
            public void onClick (View v) {
                DataProvider provider = new DataProvider (context);
                StringBuilder sb = new StringBuilder ();

                for (GroupEntity group : provider.getGroups ()) {
                    Log.i ("DMA", "Group " + group.TITLE);
                    for (StudentEntity student : provider.getStudents (group.ID)) {
                        sb.append ("'");
                        sb.append (student.getName ());
                        sb.append ("' ");
                    }

                    Log.i ("DMA", sb.toString ().trim ());
                    sb = new StringBuilder ();
                }
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
