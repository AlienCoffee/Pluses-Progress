package ru.shemplo.pluses.network.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.IOException;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.Objects;

public class PullReceiver extends BroadcastReceiver {

    @Override
    public void onReceive (Context context, Intent intent) {
        Log.i ("PR", "Broadcast event for PR");
        Log.i ("PR", new Timestamp (System.currentTimeMillis ()).toString ());

        SharedPreferences preferences =
                context.getSharedPreferences ("PullData", Context.MODE_PRIVATE);
        int value = Integer.parseInt (preferences.getString ("i", "0"));
        Log.i ("PR", "Current value: " + value);

        preferences.edit ().putString ("i", "" + (value + 1)).apply ();
    }
}
