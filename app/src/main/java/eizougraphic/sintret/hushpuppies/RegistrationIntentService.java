package eizougraphic.sintret.hushpuppies;

import android.app.IntentService;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import eizougraphic.sintret.hushpuppies.library.AppConfig;
import eizougraphic.sintret.hushpuppies.library.SessionManager;

/**
 * Created by andy on 10/21/2015.
 */
public class RegistrationIntentService extends IntentService {

    private static final String TAG = "RegServicePush";
    private static final String[] TOPICS = {"global"};
    SessionManager session;


    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        try {
            InstanceID instanceID = InstanceID.getInstance(this);
            // R.string.gcm_defaultSenderId (the Sender ID) is typically derived from google-services.json.
            // See https://developers.google.com/cloud-messaging/android/start for details on this file.
            String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

            // TODO: Implement this method to send any registration to your app's servers.
            sendRegistrationToServer(token);
            session = new SessionManager(getApplicationContext());
            session.setGCM(token);

        } catch (Exception e) {
            Log.d(TAG, "Failed to complete token refresh", e);
        }
        // Notify UI that registration has completed, so the progress indicator can be hidden.

    }

    protected void onHandleIntentBackup(Intent intent) {

        // SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        try {
            // [START register_for_gcm]
            // Initially this call goes out to the network to retrieve the token, subsequent calls
            // are local.
            // [START get_token]
            InstanceID instanceID = InstanceID.getInstance(this);
            // R.string.gcm_defaultSenderId (the Sender ID) is typically derived from google-services.json.
            // See https://developers.google.com/cloud-messaging/android/start for details on this file.
            String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            // [END get_token]
            Log.i(TAG, "GCM Registration Token: " + token);

            // TODO: Implement this method to send any registration to your app's servers.
            sendRegistrationToServer(token);

            // Subscribe to topic channels
            //subscribeTopics(token);

            // You should store a boolean that indicates whether the generated token has been
            // sent to your server. If the boolean is false, send the token to your server,
            // otherwise your server should have already received the token.
            //sharedPreferences.edit().putBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, true).apply();
            // [END register_for_gcm]
        } catch (Exception e) {
            Log.d(TAG, "Failed to complete token refresh", e);
            // If an exception happens while fetching the new token or updating our registration data
            // on a third-party server, this ensures that we'll attempt the update at a later time.
            //  sharedPreferences.edit().putBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false).apply();
        }
        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(QuickstartPreferences.REGISTRATION_COMPLETE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    /**
     * Persist registration to third-party servers.
     * <p/>
     * Modify this method to associate the user's GCM registration token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        MainActivity.newRegID = token;
        WebServerRegistrationTask webServer = new WebServerRegistrationTask();
        webServer.execute();
    }


    public class WebServerRegistrationTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            //SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(RegistrationIntentService.this);
            session = new SessionManager(getApplicationContext());

            URL url = null;
            try {
                url = new URL(AppConfig.URL_REGISTER_GCM);
            } catch (MalformedURLException e) {
                e.printStackTrace();

                // session.setGCM();
                // sharedPreferences.edit().putString(Constants.PREF_GCM_REG_ID, "").apply();
            }
            Map<String, String> dataMap = new HashMap<String, String>();
            dataMap.put("gcm_regid", MainActivity.newRegID);
            dataMap.put("name", session.name());
            dataMap.put("email", session.email());


            StringBuilder postBody = new StringBuilder();
            Iterator<Map.Entry<String, String>> iterator = dataMap.entrySet().iterator();

            while (iterator.hasNext()) {
                Map.Entry<String, String> param = (Map.Entry<String, String>) iterator.next();
                postBody.append(param.getKey()).append('=')
                        .append(param.getValue());
                if (iterator.hasNext()) {
                    postBody.append('&');
                }
            }
            String body = postBody.toString();
            byte[] bytes = body.getBytes();

            HttpURLConnection conn = null;
            try {
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.setFixedLengthStreamingMode(bytes.length);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type",
                        "application/x-www-form-urlencoded;charset=UTF-8");

                OutputStream out = conn.getOutputStream();
                out.write(bytes);
                out.close();
                String response = "";
                InputStream is = null;
                try {
                    is = conn.getInputStream();
                    int ch;
                    StringBuffer sb = new StringBuffer();
                    while ((ch = is.read()) != -1) {
                        sb.append((char) ch);
                    }
                    response = sb.toString();

                } catch (IOException e) {
                    throw e;
                } finally {
                    if (is != null) {
                        is.close();
                    }
                }
                int status = conn.getResponseCode();
                if (status == 200) {
                    if (response.equals("1")) {
                        //sharedPreferences.edit().putString(Constants.PREF_GCM_REG_ID, MainActivity.newRegID).apply();
                        Log.d("Status GCM","SUKSESS");
                        session.setGCM(MainActivity.newRegID);
                        Intent registrationComplete = new Intent(AppConfig.SERVER_SUCCESS);
                        LocalBroadcastManager.getInstance(RegistrationIntentService.this).sendBroadcast(registrationComplete);
                    }
                } else {
                    throw new IOException("Request failed with error code "
                            + status);
                }
            } catch (ProtocolException pe) {
                pe.printStackTrace();
                session.setGCM("");
                //sharedPreferences.edit().putString(Constants.PREF_GCM_REG_ID, "").apply();

            } catch (IOException io) {
                io.printStackTrace();
                session.setGCM("");
                //sharedPreferences.edit().putString(Constants.PREF_GCM_REG_ID, "").apply();

            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }

            return null;
        }
    }
}
