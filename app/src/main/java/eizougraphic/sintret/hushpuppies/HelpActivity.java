package eizougraphic.sintret.hushpuppies;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import eizougraphic.sintret.hushpuppies.library.AppConfig;
import eizougraphic.sintret.hushpuppies.library.JSONParser;

/**
 * Created by andy on 10/11/2015.
 */

public class HelpActivity extends BaseActivity {

    TextView textView1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        super.onCreateDrawer();

        ViewStub stub = (ViewStub) findViewById(R.id.layout_stub);
        stub.setLayoutResource(R.layout.content_help);
        View inflated = stub.inflate();
        /* your logic here */
        JSONParse jsonParse = new JSONParse();
        jsonParse.execute();

        textView1 = (TextView) findViewById(R.id.textView1);

    }

    public class JSONParse extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(HelpActivity.this,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            JSONParser jsonParser = new JSONParser();

            String Url = AppConfig.URL_HELP;
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