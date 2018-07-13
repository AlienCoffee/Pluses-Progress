package ru.shemplo.pluses.layout;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import ru.shemplo.pluses.R;
import ru.shemplo.pluses.network.DataProvider;

public class DiaryMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.testing_dummy);
        /*setContentView(R.layout.frame_layout);

        GroupsFragment groupsFragment = new GroupsFragment();
        groupsFragment.setContext(this);


        //TODO: add favourites

        groupsFragment.setData(DataProvider.getGroups());
        getFragmentManager().beginTransaction().add(R.id.main_frame, groupsFragment).commit();*/
    }

    public void something(View view) {
        Log.e("ss", "qq");
    }
}
