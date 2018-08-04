package ru.shemplo.pluses.layout;


import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import ru.shemplo.pluses.R;
import ru.shemplo.pluses.network.DataProvider;
import ru.shemplo.pluses.network.service.DataPullService;

public class DiaryMainActivity extends AppCompatActivity {

    public static DiaryMainActivity page;

    public static int group = -1, student = -1;

    private ToolbarManager toolbarManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DiaryMainActivity.page = this;

        startService(new Intent(this, DataPullService.class));

        setContentView(R.layout.frame_layout);
        toolbarManager = new ToolbarManager(this);

        getFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                Fragment fragment = getFragmentManager().findFragmentById(R.id.main_frame);
                if (fragment instanceof GroupsFragment) {
                    student = -1;
                    group = -1;
                } else if (fragment instanceof StudentsFragment) {
                    student = -1;
                }

                toolbarManager.setDepth(getFragmentManager().getBackStackEntryCount());
            }
        });

        findViewById(R.id.progress_bar).setVisibility(View.VISIBLE);
        DataProvider provider = new DataProvider(this);
        GroupsFragment groupsFragment = new GroupsFragment();
        groupsFragment.setContext(this);
        groupsFragment.setData(provider.getGroups());
        getFragmentManager().beginTransaction()
                .replace(R.id.main_frame, groupsFragment)
                .commit();
        findViewById(R.id.progress_bar).setVisibility(View.GONE);

    }

    public void switchFragment(int fragment, int id, CharSequence heading) {
        findViewById(R.id.progress_bar).setVisibility(View.VISIBLE);
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
                studentsFragment.setData(provider.getStudents(id));
                newFragment = studentsFragment;
                group = id;
            }
            break;
            case R.id.topic_recycler_view: {
                TopicsFragment topicsFragment = new TopicsFragment();
                topicsFragment.setContext(this);
                topicsFragment.setData(provider.getTopics(id));
                newFragment = topicsFragment;
                student = id;
            }
            break;

            default:
                Log.e("ERROR", "unexpected fragment in switchFragment");
                newFragment = null;
        }

        getFragmentManager().beginTransaction()
                .addToBackStack(null)
                .replace(R.id.main_frame, newFragment)
                .commit();

        toolbarManager.push(heading);

        findViewById(R.id.progress_bar).setVisibility(View.GONE);
    }

}
