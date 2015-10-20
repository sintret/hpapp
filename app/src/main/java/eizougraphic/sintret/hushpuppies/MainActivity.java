package eizougraphic.sintret.hushpuppies;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.ViewStub;
import android.widget.TextView;

import java.util.HashMap;

import eizougraphic.sintret.hushpuppies.library.SessionManager;


public class MainActivity extends BaseActivity {
    TextView textView1;
    TextView textView2;
    TextView textView3;
    TextView textView4;

    private SessionManager session;


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


    }

}