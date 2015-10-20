package eizougraphic.sintret.hushpuppies.library;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by andy on 10/8/2015.
 */
public class SessionManager {
    // LogCat tag
    private static String TAG = SessionManager.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;

    SharedPreferences.Editor editor;
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "Hushpuppies";
    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";
    private static final String PREF_GCM_REG_ID = "GCMRegId";

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setLogin() {
        editor.putBoolean(KEY_IS_LOGGEDIN, false);
        editor.commit();

        Log.d(TAG, "User login session modified!");
    }

    public void setGCM(String gcmRegId){
        editor.putString(AppConfig.TAG_GCM_REGID, gcmRegId);
        editor.commit();
        Log.d(TAG, "User login session modified!");

    }

    public void setLogin(boolean isLoggedIn) {

        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);

        // commit changes
        editor.commit();

        Log.d(TAG, "User login session modified!");
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }

    public String name() {
        return pref.getString(AppConfig.TAG_FULLNAME, "DEFAULT");
    }

    public String email() {
        return pref.getString(AppConfig.TAG_EMAIL, "DEFAULT");
    }

    public String uniqueId() {
        return pref.getString(AppConfig.TAG_UNIQUE_ID, "DEFAULT");
    }
    public String stamp() {
        return pref.getString(AppConfig.TAG_STAMP, "DEFAULT");
    }
    public String point() {
        return pref.getString(AppConfig.TAG_POINT, "DEFAULT");
    }
    public String card_number() {
        return pref.getString(AppConfig.TAG_CARD_NUMBER, "DEFAULT");
    }

    public String createdAt() {
        return pref.getString(AppConfig.TAG_CREATED_AT, "DEFAULT");
    }


    public void createLoginSession(String name, String email, String uniqueId, String point, String stamp, String phone, String address,
                                   String coupons, String card_number, String bod, String created_at, String photo ) {
        // Storing login value as TRUE
        editor.putBoolean(KEY_IS_LOGGEDIN, true);

        // Storing name in pref
        editor.putString(AppConfig.TAG_FULLNAME, name);
        editor.putString(AppConfig.TAG_EMAIL, email);
        editor.putString(AppConfig.TAG_UNIQUE_ID, uniqueId);
        editor.putString(AppConfig.TAG_POINT, point);
        editor.putString(AppConfig.TAG_STAMP, stamp);
        editor.putString(AppConfig.TAG_PHONE, phone);
        editor.putString(AppConfig.TAG_ADDRESS, address);
        editor.putString(AppConfig.TAG_COUPONS, coupons);
        editor.putString(AppConfig.TAG_CARD_NUMBER, card_number);
        editor.putString(AppConfig.TAG_BOD, bod);
        editor.putString(AppConfig.TAG_CREATED_AT, created_at);
        editor.putString(AppConfig.TAG_PHOTO, photo);

        // commit changes
        editor.commit();
        Log.d(TAG, "User login session modified!");
    }


}