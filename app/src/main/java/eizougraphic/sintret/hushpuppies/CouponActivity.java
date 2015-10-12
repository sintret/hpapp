package eizougraphic.sintret.hushpuppies;

import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;

import eizougraphic.sintret.hushpuppies.library.SessionManager;

/**
 * Created by andy on 10/11/2015.
 */
public class CouponActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        super.onCreateDrawer();

        ViewStub stub = (ViewStub) findViewById(R.id.layout_stub);
        stub.setLayoutResource(R.layout.content_coupon);
        View inflated = stub.inflate();
        /* your logic here */


    }
}