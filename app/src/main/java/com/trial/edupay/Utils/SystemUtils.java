package com.trial.edupay.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.telephony.TelephonyManager;

import com.trial.edupay.MainApplication;

/**
 * Created by mallikapriyakhullar on 24/12/17.
 */

public class SystemUtils {

    @SuppressLint("MissingPermission")
    public static String getUserPhoneNumber() { //TODO: explain this to them
        TelephonyManager tMgr = (TelephonyManager) MainApplication.getAppContext().getSystemService(Context.TELEPHONY_SERVICE);
        return tMgr.getLine1Number();
//        return "8123814032";
    }

    public static boolean isValidPhone(CharSequence phone) {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }
}
