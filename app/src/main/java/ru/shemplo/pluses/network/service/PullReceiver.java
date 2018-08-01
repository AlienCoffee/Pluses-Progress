package ru.shemplo.pluses.network.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.sql.Timestamp;

public class PullReceiver extends BroadcastReceiver {

    @Override
    public void onReceive (Context context, Intent intent) {
        Log.i ("PR", "Broadcast event for PR");
        Log.i ("PR", new Timestamp (System.currentTimeMillis ()).toString ());
    }

}
