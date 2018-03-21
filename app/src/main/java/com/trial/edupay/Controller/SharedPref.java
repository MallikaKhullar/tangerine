package com.trial.edupay.Controller;


import android.content.Context;
import android.content.SharedPreferences;


import com.trial.edupay.MainApplication;

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;


/**
 * Created by mallikapriyakhullar on 24/12/17.
 */

public class SharedPref {

    private final static String PREF_TRACKERS = "PREF_EDU";
    private SharedPreferences preferences;

    private static SharedPref instance;

    private SharedPreferences getSharedPreferencesFor(String prefName, Context context) {
        return context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
    }

    private SharedPref(Context context) {
        preferences = getSharedPreferencesFor(PREF_TRACKERS, context);
    }

    public static SharedPref getInstance() {
        if(instance == null) instance = new SharedPref(MainApplication.getInstance().getApplicationContext());
        return instance;
    }


    public static final String AppPreference = "PREFERENCES";
    public static final String PFAuthKey = "PFAuthKey";
    public static final String PFBearerKey = "PFBearerKey";
    public static final String PFUser = "PFUser";
    public static final String PFFcmId ="PFFcmId";
    public static final String PFCurrentFee = "PFCurrentFee";
    public static final String PFMobile = "PFMobile";
    public static final String PFEmail = "PFEmail";
    public static final String PFCartItem ="PFCartItem";
    public static final String PFGroupNotifications = "PFGroupNotifications";
    public static final String PFNewStudent = "PFNewStudent";




    public void updateAuth(String mobile, String token){
//        preferenceService.setString(PreferenceService.PFAuthKey, getString(R.string.text_token_prefix) + authToken.token);
//        preferenceService.setString(PreferenceService.PFMobile,mobile);
    }



    public String getString(String key) {
        return preferences.getString(key, null);
    }

    public String getString(String key, String def) {
        return preferences.getString(key, def);
    }

    public boolean getBoolean(String key) {
        return preferences.getBoolean(key, false);
    }

    public int getInt(String key) {
        return preferences.getInt(key, 0);
    }

    public boolean setString(String key, String value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        return editor.commit();
    }

    public boolean setBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        return editor.commit();
    }

    public boolean setInt(String key, int value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        return editor.commit();
    }

    public boolean setDatetime(String key, DateTime value) {
        SharedPreferences.Editor editor = preferences.edit();
        DateTimeFormatter fmt = ISODateTimeFormat.dateTime();
        editor.putString(key, fmt.print(value));
        return editor.commit();
    }

    public boolean setLocalDatetime(String key, LocalDateTime value) {
        SharedPreferences.Editor editor = preferences.edit();
        DateTimeFormatter fmt = ISODateTimeFormat.dateTime();
        editor.putString(key, fmt.print(value));
        return editor.commit();
    }

    public void clearAll() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }
}
