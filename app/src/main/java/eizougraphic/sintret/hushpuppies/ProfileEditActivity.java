package eizougraphic.sintret.hushpuppies;

import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;

/**
 * Created by andy on 10/18/2015.
 */

public class ProfileEditActivity extends BaseActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        super.onCreateDrawer();

        ViewStub stub = (ViewStub) findViewById(R.id.layout_stub);
        stub.setLayoutResource(R.layout.content_edit_profile);
        View inflated = stub.inflate();
        /* your logic here */


    }
}