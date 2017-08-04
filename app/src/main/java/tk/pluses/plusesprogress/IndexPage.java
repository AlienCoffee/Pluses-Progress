package tk.pluses.plusesprogress;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import tk.pluses.plusesprogress.fragments.FragmentAuth;
import tk.pluses.plusesprogress.io.RequestForm;
import tk.pluses.plusesprogress.io.RequestIO;
import tk.pluses.plusesprogress.io.RequestResult;
import tk.pluses.plusesprogress.user.UserEntity;

public class IndexPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
                                                            LoaderManager.LoaderCallbacks <RequestResult> {

    private NavigationView navigation;
    private DrawerLayout drawer;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_index_page);

        SharedPreferences preferences = getSharedPreferences (UserEntity.CONFIG_FILE,
                                                                Context.MODE_PRIVATE);
        UserEntity.setPreferences (preferences);

        if (!preferences.contains ("device.code")) {
            SharedPreferences.Editor editor = preferences.edit ();
            editor.putString ("device.code", getDeviceCode ());
            editor.apply ();
        }

        String code = preferences.getString ("device.code", "def");
        if (!preferences.getBoolean ("device.registered", false)) {
            RequestForm form = new RequestForm ("http://pluses.tk/api.other.registerDevice");
            form.addParam ("token", UserEntity.getProperty ("token"));
            Log.i ("Device", "Device code: " + code);
            Log.i ("Device", UserEntity.getProperty ("token"));
            form.addParam ("hash", code);
            form.addParam ("name", "...");
            form.addParam ("system", "Android");

            Bundle args = new Bundle ();
            args.putSerializable ("form", form);
            getSupportLoaderManager ().restartLoader (0, args, this);
        }

        drawer = (DrawerLayout) findViewById (R.id.menu_drawer_layout);

        navigation = (NavigationView) findViewById (R.id.menu_navigation_view);
        navigation.setNavigationItemSelectedListener (this);
    }

    private String getDeviceCode () {
        @SuppressLint ("HardwareIds")
        String code = Settings.Secure.getString (getBaseContext ().getContentResolver (),
                                                    Settings.Secure.ANDROID_ID);
        try {
            MessageDigest digest = MessageDigest.getInstance ("MD5");

            // Adding extra salt to code
            code = "salt1" + code + "salt2";
            digest.update (code.getBytes ());

            // Converting back to string
            BigInteger text = new BigInteger (1, digest.digest ());
            code = text.toString (16);
        } catch (NoSuchAlgorithmException nsae) {
            Log.e ("Device", nsae.getMessage ());
            code = "def";
        }

        return code;
    }

    @Override
    public Loader <RequestResult> onCreateLoader (int id, Bundle args) {
        return new RequestIO (this.getBaseContext (), (RequestForm) args.get ("form"));
    }

    @Override
    public void onLoadFinished (Loader <RequestResult> loader, RequestResult data) {
        Log.i ("Device", data.TYPE + " (code: " + data.CODE + ")");
        if (data.TYPE.equals ("Success") && data.CODE == 1000) {
            SharedPreferences preferences = getSharedPreferences (UserEntity.CONFIG_FILE,
                                                                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit ();
            editor.putBoolean ("device.registered", true);
            editor.apply ();

            Log.i ("Device", "Device registered");
        }
    }

    @Override
    public void onLoaderReset (Loader <RequestResult> loader) {}

    @Override
    public boolean onNavigationItemSelected (@NonNull MenuItem item) {
        Fragment fragment = null;
        Class frClass = null;
        Intent intent = null;

        Log.i ("Index", "click");
        switch (item.getItemId ()) {
            case (R.id.navigation_item_auth):
                frClass = FragmentAuth.class;
                break;
        }

        try {
            fragment = (Fragment) frClass.newInstance ();
        } catch (Exception e) {
            e.printStackTrace ();
            return false;
        }

        FragmentManager manager = getSupportFragmentManager ();
        manager.beginTransaction ().replace (R.id.menu_frame_layout, fragment).commit ();

        item.setChecked (true);
        setTitle (item.getTitle ());

        drawer.closeDrawer (GravityCompat.START);
        return true;
    }

}
