package ru.shemplo.pluses.layout;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import ru.shemplo.pluses.entity.GroupEntity;
import ru.shemplo.pluses.R;
import ru.shemplo.pluses.network.AppConnection;
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

        final Intent intentDataPullStart = new Intent (this, DataPullService.class);
        final Intent intentDataPullStop = new Intent (this, DataPullService.class);
        final SharedPreferences preferences =
                getSharedPreferences ("PullData", Context.MODE_PRIVATE);
        this.context = this;

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
                for (GroupEntity entity : provider.getGroups ()) {
                    System.out.println (entity.getName ());
                }
                /*
                Log.i ("DMA", "Sending command message");
                AppConnection connection = new AppConnection (false);
                Log.i ("DMA", "Connection " + connection + " " + connection.isAlive ());
                connection.sendMessage (new CommandMessage (AppMessage.MessageDirection.CTS, "dsf"));
                */
            }

        });

        /*
        setContentView(R.layout.frame_layout);

        GroupsFragment groupsFragment = new GroupsFragment();
        groupsFragment.setContext(this);


        //TODO: add favourites

        groupsFragment.setData(DataProvider.getGroups());
        getFragmentManager().beginTransaction().add(R.id.main_frame, groupsFragment).commit();
        */
    }

    public void something(View view) {
        Log.e("ss", "qq");
    }
}
