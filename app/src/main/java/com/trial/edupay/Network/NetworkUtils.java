package com.trial.edupay.Network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.trial.edupay.MainApplication;


/**
 * Created by mallikapriyakhullar on 23/12/17.
 */

public class NetworkUtils {
    public static boolean isNetworkAvailable() {

        ConnectivityManager connectivityManager = (ConnectivityManager) MainApplication
                .getInstance()
                .getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
