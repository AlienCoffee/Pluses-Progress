package ru.shemplo.pluses.layout;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import ru.shemplo.pluses.R;

public class DiaryMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.testing_dummy);

        /*
        GroupsFragment groupsFragment = new GroupsFragment();
        groupsFragment.setContext(this);

        List<GroupEntity> groups;
        groups = new ArrayList<>();//TODO: remove
        for (int i = 0; i < 30; i++) {
            groups.add(new GroupEntity());
        }
        //TODO: add favourites

        groupsFragment.setData(groups);
        getFragmentManager().beginTransaction().add(R.id.group_recycler_view, groupsFragment).commit();
        */
    }

    public void something(View view) {
        Log.e("ss", "qq");
    }
}
