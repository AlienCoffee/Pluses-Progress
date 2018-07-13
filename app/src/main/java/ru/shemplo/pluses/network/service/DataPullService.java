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

    @SuppressWarnings ("unused")
    public static boolean isRunning (Context context) {
        ActivityManager manager = (ActivityManager)
                context.getSystemService (ACTIVITY_SERVICE);
        List <ActivityManager.RunningServiceInfo> services =
                manager.getRunningServices (256);
        for (int i = 0; i < services.size (); i++) {
            ActivityManager.RunningServiceInfo info = services.get (i);
            Log.i("Service", "Process " + info.process + " with component "
                    + info.service.getClassName());
        }
        return true;
    }

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
                /*
                try {
                    Thread.sleep (30 * 1000);
                    Log.i ("DPS", "I was started and running after closed application");

                    Intent call = new Intent ();
                    call.setAction ("PR_ACTION");
                    call.addCategory (Intent.CATEGORY_DEFAULT);
                    sendBroadcast (call);
                } catch (InterruptedException ie){
                    Log.i ("DPS", "I was interrupted");
                }
                */

                while (true) {
                    try {
                        // Do one time each 1.5 minutes
                        Thread.sleep (90 * 1000);
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
        // stopSelf ();

        return Service.START_STICKY;
    }

    @Override
    public void onDestroy () {
        thread.interrupt ();
        super.onDestroy ();
    }
}
