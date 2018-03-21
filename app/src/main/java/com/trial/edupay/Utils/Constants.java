package com.trial.edupay.Utils;

import com.trial.edupay.Controller.SharedPref;

/**
 * Created by mallikapriyakhullar on 23/12/17.
 */

public class Constants {
    public static final String API = "https://www.edupay.com/";
    public static final String USER_ENDPOINT = "/users";
    public static String getAuthKey() {
        return SharedPref.getInstance().getString(SharedPref.PFAuthKey);
    }    public static String getBearerKey() {
        return SharedPref.getInstance().getString(SharedPref.PFBearerKey);
    }
}
