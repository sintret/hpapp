package eizougraphic.sintret.hushpuppies;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import eizougraphic.sintret.hushpuppies.library.AppConfig;
import eizougraphic.sintret.hushpuppies.library.JSONParser;
import eizougraphic.sintret.hushpuppies.library.SessionManager;
import eizougraphic.sintret.hushpuppies.sql.SQLiteHandler;

/**
 * Created by andy on 10/10/2015.
 */
public class LoginActivity  extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;
    private boolean checkAccess = false;

    public String memail;
    public String mpassword;

    JSONArray user = null;
    JSONParse jsonParse = new JSONParse();

    @InjectView(R.id.input_email)
    EditText _emailText;
    @InjectView(R.id.input_password)
    EditText _passwordText;
    @InjectView(R.id.btn_login)
    Button _loginButton;
    @InjectView(R.id.link_signup)
    TextView _signupLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);
        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Session manager
        session = new SessionManager(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        // TODO: Implement your own authentication logic here.
        jsonParse.email = email;
        jsonParse.password = password;
        jsonParse.execute();

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        if (session.isLoggedIn()) {
                            // User is already logged in. Take him to main activity
                            onLoginSuccess();
                        } else {
                            progressDialog.dismiss();
                            onLoginFailed();
                        }
                    }
                }, 3000);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(false);
        // Launching the MAIN activity
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }


    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 20) {
            _passwordText.setError("between 4 and 20 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

    public class JSONParse extends AsyncTask<String, String, JSONObject> {

        public String email;
        public String password;
        public boolean validate = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            JSONParser jsonParser = new JSONParser();

            String Url = AppConfig.URL_LOGIN + "?email=" + email + "&password=" + password;
            Log.d("this url", Url);

            //Getting JSON from url
            JSONObject jsonObject = jsonParser.getJSONFromUrl(Url);
            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            try {

                String id = jsonObject.getString(AppConfig.TAG_ID);
                String name = jsonObject.getString(AppConfig.TAG_FULLNAME);
                String email =jsonObject.getString(AppConfig.TAG_EMAIL);
                String uniqueId = jsonObject.getString(AppConfig.TAG_UNIQUE_ID);
                String point = jsonObject.getString(AppConfig.TAG_POINT);
                String stamp = jsonObject.getString(AppConfig.TAG_STAMP);
                String phone = jsonObject.getString(AppConfig.TAG_PHONE);
                String address = jsonObject.getString(AppConfig.TAG_UNIQUE_ID);
                String coupons = jsonObject.getString(AppConfig.TAG_UNIQUE_ID);
                String card_number = jsonObject.getString(AppConfig.TAG_UNIQUE_ID);
                String bod = jsonObject.getString(AppConfig.TAG_UNIQUE_ID);
                String created_at = jsonObject.getString(AppConfig.TAG_UNIQUE_ID);
                String photo = jsonObject.getString(AppConfig.TAG_PHOTO);
                Boolean error = jsonObject.getBoolean(AppConfig.TAG_ERROR);
                Log.d("email user",email+" is true");
                Log.d("status",error+" is " + error);

                if (error == true) {
                    //Jika ada error
                    session.setLogin(false);
                } else {
                    //Jika Oke
                    session.createLoginSession(name,email,uniqueId,point,stamp,phone,address,coupons,card_number,bod,created_at,photo);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}