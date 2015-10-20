package eizougraphic.sintret.hushpuppies;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import eizougraphic.sintret.hushpuppies.library.SessionManager;

/**
 * Created by andy on 10/12/2015.
 */
public class BaseActivity  extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private SessionManager session;
    public Toolbar toolbar;
    ProgressDialog progressDialog;


    protected void onCreateDrawer() {
        setContentView(R.layout.activity_main);
        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        // Fetching user details from SQLite

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "No Promo at this moment...", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     * */
    private void logoutUser() {
        session.setLogin(false);

        // Launching the login activity
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Intent intent;

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
             intent = new Intent(this, SettingActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.action_about) {
             intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.action_help) {
             intent = new Intent(this, HelpActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.action_logout) {
            logoutUser();
        } else if (id == R.id.action_profile) {
             intent = new Intent(this, ProfileEditActivity.class);
            startActivity(intent);
            finish();

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent;

        if (id == R.id.nav_home) {
            // Handle the camera action
           intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_stamp) {
            // Handle the camera action
            intent = new Intent(this, StampActivity.class);
            startActivity(intent);
            finish();

        } else if (id == R.id.nav_coupon) {
            // Handle the camera action
             intent = new Intent(this, CouponActivity.class);
            startActivity(intent);
            finish();

        } else if (id == R.id.nav_people) {
            // Handle the camera action
             intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
            finish();

        } else if (id == R.id.nav_point) {
            // Handle the camera action
             intent = new Intent(this, PointActivity.class);
            startActivity(intent);
            finish();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
