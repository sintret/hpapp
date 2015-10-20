package eizougraphic.sintret.hushpuppies;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import eizougraphic.sintret.hushpuppies.library.AppConfig;
import eizougraphic.sintret.hushpuppies.library.JSONParser;
import eizougraphic.sintret.hushpuppies.library.SessionManager;

public class AboutActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private SessionManager session;
    TextView textView1;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewStub stub = (ViewStub) findViewById(R.id.layout_stub);
        stub.setLayoutResource(R.layout.content_about);
        View inflated = stub.inflate();

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }
        JSONParse jsonParse = new JSONParse();
        jsonParse.execute();
        //JSONParse.execute();

        textView1 = (TextView) findViewById(R.id.textView1);

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
     */
    private void logoutUser() {
        session.setLogin(false);

        // Launching the login activity
        Intent intent = new Intent(AboutActivity.this, LoginActivity.class);
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(AboutActivity.this, SettingActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.action_about) {
            return true;
        } else if (id == R.id.action_help) {
            Intent intent = new Intent(AboutActivity.this, HelpActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.action_help) {
            Intent intent = new Intent(AboutActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.action_logout) {
            logoutUser();
        } else if (id == R.id.action_profile) {
            Intent intent = new Intent(AboutActivity.this, ProfileEditActivity.class);
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

        if (id == R.id.nav_home) {
            // Handle the camera action
            Intent intent = new Intent(AboutActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_stamp) {
            // Handle the camera action
            Intent intent = new Intent(AboutActivity.this, StampActivity.class);
            startActivity(intent);
            finish();

        } else if (id == R.id.nav_coupon) {
            // Handle the camera action
            Intent intent = new Intent(AboutActivity.this, CouponActivity.class);
            startActivity(intent);
            finish();

        } else if (id == R.id.nav_people) {
            // Handle the camera action
            Intent intent = new Intent(AboutActivity.this, ProfileActivity.class);
            startActivity(intent);
            finish();

        } else if (id == R.id.nav_point) {
            // Handle the camera action
            Intent intent = new Intent(AboutActivity.this, PointActivity.class);
            startActivity(intent);
            finish();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class JSONParse extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(AboutActivity.this,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            JSONParser jsonParser = new JSONParser();

            String Url = AppConfig.URL_ABOUT;
            Log.d("this url", Url);

            //Getting JSON from url
            JSONObject jsonObject = jsonParser.getJSONFromUrl(Url);
            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            try {

                String description = jsonObject.getString(AppConfig.TAG__ABOUT_DESCRIPTION);
                textView1.setText(Html.fromHtml(description));
                progressDialog.hide();

                Log.d("description :", description + " is true");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }
}
