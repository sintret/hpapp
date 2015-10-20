package eizougraphic.sintret.hushpuppies;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.ViewStub;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

/**
 * Created by andy on 10/20/2015.
 */
public class ScannerActivity  extends BaseActivity {

    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        super.onCreateDrawer();


        ViewStub stub = (ViewStub) findViewById(R.id.layout_stub);
        stub.setLayoutResource(R.layout.content_scanner);
        View inflated = stub.inflate();
        textView = (TextView) findViewById(R.id.textView);
        /* your logic here */

        IntentIntegrator integrator = new IntentIntegrator(ScannerActivity.this);
        integrator.addExtra("SCAN_WIDTH", 640);
        integrator.addExtra("SCAN_HEIGHT", 480);
        integrator.addExtra("SCAN_MODE", "QR_CODE_MODE,PRODUCT_MODE");
        //customize the prompt message before scanning
        integrator.addExtra("PROMPT_MESSAGE", "Scanner Start!");
        integrator.initiateScan(IntentIntegrator.PRODUCT_CODE_TYPES);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (result != null) {
            String contents = result.getContents();
            if (contents != null) {
                textView.setText(result.toString());
                //showDialog(R.string.result_succeeded, result.toString());
            } else {
                textView.setText("Scanner Failed!");
                //showDialog(R.string.result_failed, getString(R.string.result_failed_why));
            }
        }
    }
}