package eizougraphic.sintret.hushpuppies;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.util.HashMap;

import eizougraphic.sintret.hushpuppies.library.AppConfig;
import eizougraphic.sintret.hushpuppies.library.SessionManager;


public class MainActivity extends BaseActivity {
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private SessionManager session;
    public static String newRegID;
    private static final String TAG = "MainActivity";

    TextView textView1;
    TextView textView2;
    TextView textView3;
    TextView textView4;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        super.onCreateDrawer();

        ViewStub stub = (ViewStub) findViewById(R.id.layout_stub);
        stub.setLayoutResource(R.layout.content_main);
        View inflated = stub.inflate();
        /* your logic here */

        session = new SessionManager(getApplicationContext());


        textView1 = (TextView) findViewById(R.id.textView1);
        textView1.setText(session.name());
        textView2 = (TextView) findViewById(R.id.textView2);
        textView2.setText(session.card_number());
        textView3 = (TextView) findViewById(R.id.textView3);
        textView3.setText(session.point()+"/12");
        textView4 = (TextView) findViewById(R.id.textView4);
        textView4.setText(session.stamp());

 /* For GCM SERVICE */

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);
                String sentToken = sharedPreferences
                        .getString(AppConfig.TAG_GCM_REGID, "");
                System.out.println("SERVER_SUCCESS")
                ;
                if (sentToken.equals("")) {
                    Toast.makeText(MainActivity.this, "Failed to save on server", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Succesfully saved on server", Toast.LENGTH_SHORT).show();
                }
            }
        };

        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }
    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

}