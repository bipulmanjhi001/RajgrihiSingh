package in.rajgrihisingh.pref;

import android.content.Context;
import android.content.SharedPreferences;

public class ProfilePrefManager {

    private static final String SHARED_PREF_NAME = "Profilesinghpref";
    private static final String KEY_NAME = "s_name";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_DESC = "description";
    private static final String KEY_SUPERVISOR = "supervisor_name";

    private static ProfilePrefManager mInstance;
    private static Context mCtx;

    private ProfilePrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized ProfilePrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new ProfilePrefManager(context);
        }
        return mInstance;
    }

    public void userProfile(ProfileUser profileUser) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_NAME, profileUser.getS_name());
        editor.putString(KEY_ADDRESS, profileUser.getAddress());
        editor.putString(KEY_DESC, profileUser.getDescription());
        editor.putString(KEY_SUPERVISOR, profileUser.getSupervisor_name());
        editor.putString(KEY_DESC, profileUser.getDescription());
        editor.apply();
    }

    public User getUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new User(sharedPreferences.getString(KEY_NAME, null),
                sharedPreferences.getString(KEY_ADDRESS, null),
                sharedPreferences.getString(KEY_DESC, null),
                sharedPreferences.getString(KEY_SUPERVISOR, null),
                sharedPreferences.getString(KEY_DESC, null));
    }

}