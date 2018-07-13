package ru.shemplo.pluses.network.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;

public class DataPullService extends Service {

    @Nullable
    @Override
    public IBinder onBind (Intent intent) {
        return null;
    }

    private Thread thread;

    @Override
    public int onStartCommand (Intent intent, int flags, int startId) {
        thread = new Thread (new Runnable () {

            @Override
            public void run () {
            while (true) {
                try {
                    // Do one time each 1.5 minutes
                    Thread.sleep (10 * 1000);
                    Log.i ("DPS-Thread", "I'm alive");

                    Intent call = new Intent ();
                    call.setAction ("PR_ACTION");
                    call.addCategory (Intent.CATEGORY_DEFAULT);
                    sendBroadcast (call);
                } catch (InterruptedException ie) {
                    Log.i ("DPS-Thread", "I was interrupted");
                    return;
                }
            }
            }

        }, "DataPullService-Thread");
        thread.start ();
        stopSelf ();

        return Service.START_STICKY;
    }

    @Override
    public void onDestroy () {
        thread.interrupt ();
        super.onDestroy ();
    }
}
