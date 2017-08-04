package tk.pluses.plusesprogress;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import tk.pluses.plusesprogress.fragments.FragmentAuth;

public class IndexPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private NavigationView navigation;
    private DrawerLayout drawer;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_index_page);

        drawer = (DrawerLayout) findViewById (R.id.menu_drawer_layout);

        navigation = (NavigationView) findViewById (R.id.menu_navigation_view);
        navigation.setNavigationItemSelectedListener (this);
    }

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
