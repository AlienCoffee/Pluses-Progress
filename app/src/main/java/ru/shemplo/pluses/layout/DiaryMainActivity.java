package ru.shemplo.pluses.layout;


import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Stack;

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

        getFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            int depth = 0;
            Stack<CharSequence> stack = new Stack<>();

            @Override
            public void onBackStackChanged() {
                Fragment fragment = getFragmentManager().findFragmentById(R.id.main_frame);
                if (fragment instanceof GroupsFragment) {
                    student = -1;
                    group = -1;
                } else if (fragment instanceof StudentsFragment) {
                    student = -1;
                }

                int newDepth = getFragmentManager().getBackStackEntryCount();

                if (depth < newDepth) {
                    stack.push(getHeading());
                } else if (depth > newDepth){
                    stack.pop();
                    setHeading(stack.peek());
                }
                depth = newDepth;
            }
        });

        switchFragment(R.id.group_recycler_view, 0, "Diary");


        toolbar.findViewById(R.id.toolbar_back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getFragmentManager().getBackStackEntryCount() > 1) {
                    getFragmentManager().popBackStack();
                }
            }
        });

        //setSupportActionBar(toolbar);

        findViewById(R.id.toolbar_update_button).setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment fragment = getFragmentManager().findFragmentById(R.id.main_frame);
                    Toast toast = Toast.makeText (DiaryMainActivity.page,
                        "Update button clicked", Toast.LENGTH_SHORT);
                    toast.show ();
                    if (fragment == null) {
                        Log.e("ERROR", "Cannot update, fragment is null");
                        return;
                    }
                    if (group == -1) {
                        Log.i("DMA", "Update groups");
                        ((GroupsFragment) fragment).updateData();
                    } else if (student == -1) {
                        Log.i("DMA", "Update students");
                        ((StudentsFragment) fragment).updateData(group);
                    } else {
                        Log.i("DMA", "Update topics");
                        ((TopicsFragment) fragment).updateData(student);
                    }
                }

            });
    }

    public void switchFragment(int fragment, int id, CharSequence heading) {
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

        setHeading(heading);
    }

    private void setHeading(CharSequence s) {
        ((TextView)toolbar.findViewById(R.id.toolbar_text)).setText(s);
    }

    private CharSequence getHeading() {
        return ((TextView)toolbar.findViewById(R.id.toolbar_text)).getText();
    }

}
