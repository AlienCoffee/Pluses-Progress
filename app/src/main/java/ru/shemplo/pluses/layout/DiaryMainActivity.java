package ru.shemplo.pluses.layout;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import ru.shemplo.pluses.R;
import ru.shemplo.pluses.network.DataProvider;
import ru.shemplo.pluses.network.service.DataPullService;

public class DiaryMainActivity extends AppCompatActivity {

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = this;

        startService (new Intent (context, DataPullService.class));
        setContentView(R.layout.testing_dummy);

        /*
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
        */

        DataProvider provider = new DataProvider (this);
        setContentView(R.layout.frame_layout);

        GroupsFragment groupsFragment = new GroupsFragment();
        groupsFragment.setContext(this);
        groupsFragment.setData(provider.getGroups());

        getFragmentManager().beginTransaction()
                .add(R.id.main_frame, groupsFragment)
                .commit();
    }

    public void something(View view) {
        Log.e("ss", "qq");
    }
}
