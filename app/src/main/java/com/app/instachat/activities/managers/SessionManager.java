package com.app.instachat.activities.managers;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.app.instachat.activities.constants.IConstants;



public class SessionManager {
    // Shared preferences file name
    private static final String PREF_NAME = "InstaChat";
    private static final String KEY_ON_OFF_NOTIFICATION = "onOffNotification";
    private static final String KEY_ONBOARDING = "isOnBoardingDone";
    private final SharedPreferences pref;

    //============== START

    private static SessionManager mInstance;

    public static SessionManager get() {
        return mInstance;
    }

    public static void init(Context ctx) {
        if (mInstance == null) mInstance = new SessionManager(ctx);
    }

    //============== END

    public SessionManager(final Context context) {
        pref = context.getSharedPreferences(context.getPackageName() + PREF_NAME, 0);
    }

    public void setOnOffNotification(final boolean value) {
        final Editor editor = pref.edit();
        editor.putBoolean(KEY_ON_OFF_NOTIFICATION, value);
        editor.apply();
    }

    public boolean isNotificationOn() {
        return pref.getBoolean(KEY_ON_OFF_NOTIFICATION, IConstants.TRUE);
    }

    public void setOnBoardingDone(final boolean value) {
        final Editor editor = pref.edit();
        editor.putBoolean(KEY_ONBOARDING, value);
        editor.apply();
    }

    public boolean isOnBoardingDone() {
        return pref.getBoolean(KEY_ONBOARDING, IConstants.FALSE);
    }

    public void clearAll() {
        final Editor editor = pref.edit();
        editor.clear();
        editor.apply();
    }
}
