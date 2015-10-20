package eizougraphic.sintret.hushpuppies;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import eizougraphic.sintret.hushpuppies.library.AppConfig;
import eizougraphic.sintret.hushpuppies.library.SessionManager;
import eizougraphic.sintret.hushpuppies.task.SignupTask;

/**
 * Created by andy on 10/10/2015.
 */
public class SignupActivity  extends AppCompatActivity {
    private static final String TAG = "SignupActivity";
    private static final String TAG_USER = "user";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_EMAIL = "email";
    private static final String TAG_UNIQUE_ID = "unique_id";
    private static final String TAG_ERROR = "error";

    //handling session
    private SessionManager session;

    public JSONObject jsonObject = null;

    @InjectView(R.id.fullname) EditText _nameText;
    @InjectView(R.id.dob) EditText _dobText;
    @InjectView(R.id.phone) EditText _phoneText;

    @InjectView(R.id.email) EditText _emailText;
    @InjectView(R.id.password) EditText _passwordText;
    @InjectView(R.id.password_repeat) EditText _passwordRepeatText;

    @InjectView(R.id.btn_signup)
    Button _signupButton;
    @InjectView(R.id.link_login)
    TextView _loginLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.inject(this);

        // Session manager
        session = new SessionManager(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(SignupActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        /*final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();*/

        String name = _nameText.getText().toString();
        String dob = _dobText.getText().toString();
        String phone = _phoneText.getText().toString();

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        String password_repeat = _passwordRepeatText.getText().toString();

        // TODO: Implement your own signup logic here.
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("name", name);
        data.put("dob", dob);
        data.put("phone", phone);
        data.put("email", email);
        data.put("password_repeat", password_repeat);
        data.put("password", password);

        //call asyncTask for signup
        SignupTask signup = new SignupTask(SignupActivity.this, AppConfig.URL_REGISTER, data,_signupButton, progressDialog, session);
        signup.execute();
    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        // setResult(RESULT_OK, null);
        // Launching the MAIN activity
        Intent intent = new Intent(SignupActivity.this, MainActivity.class);
        startActivity(intent);

        finish();
    }

    public void onSignupFailed2() {
        Toast.makeText(getBaseContext(), "Email exist", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Signup failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }

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
}
