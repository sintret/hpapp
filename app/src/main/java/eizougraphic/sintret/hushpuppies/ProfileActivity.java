package eizougraphic.sintret.hushpuppies;

/**
 * Created by andy on 10/11/2015.
 */

import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;

/**
 * Created by andy on 10/12/2015.
 */

public class ProfileActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        super.onCreateDrawer();

        ViewStub stub = (ViewStub) findViewById(R.id.layout_stub);
        stub.setLayoutResource(R.layout.content_profile);
        View inflated = stub.inflate();
        /* your logic here */


    }
}