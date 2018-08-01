package ru.shemplo.pluses.layout;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import ru.shemplo.pluses.R;
import ru.shemplo.pluses.network.DataProvider;
import ru.shemplo.pluses.network.service.DataPullService;

public class DiaryMainActivity extends AppCompatActivity {

    public static DiaryMainActivity page;

    public static int group = -1, student = -1;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DiaryMainActivity.page = this;

        startService(new Intent(this, DataPullService.class));

        setContentView(R.layout.frame_layout);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        findViewById(R.id.toolbar_update_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("tmp", "click");
            }
        });

        switchFragment(R.id.group_recycler_view, 0);
    }

    public void switchFragment(int fragment, int id) {
        DataProvider provider = new DataProvider(this);
        Fragment newFragment;
        switch (fragment) {
            case R.id.group_recycler_view: {
                GroupsFragment groupsFragment = new GroupsFragment();
                groupsFragment.setContext(this);
                groupsFragment.setData(provider.getGroups());
                newFragment = groupsFragment;
            }
            break;
            case R.id.student_recycler_view: {
                StudentsFragment studentsFragment = new StudentsFragment();
                studentsFragment.setContext(this);
                studentsFragment.setData(provider.getStudents(group));
                newFragment = studentsFragment;
            }
            break;
            case R.id.topic_recycler_view: {
                TopicsFragment topicsFragment = new TopicsFragment();
                topicsFragment.setContext(this);
                topicsFragment.setData(provider.getTopics(student));
                newFragment = topicsFragment;
            }
            break;
            default:
                Log.e("ERROR", "undefined fragment in switchFragment");
                newFragment = null;
        }
        getFragmentManager().beginTransaction()
                .addToBackStack(null)
                .replace(R.id.main_frame, newFragment)
                .commit();
    }

}
