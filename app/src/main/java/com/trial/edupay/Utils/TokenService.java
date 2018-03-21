package com.trial.edupay.Utils;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.trial.edupay.Controller.SharedPref;

/**
 * Created by mallikapriyakhullar on 09/01/18.
 */

public class TokenService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.w("Firebase", refreshedToken);
        SharedPref.getInstance().setString(SharedPref.PFFcmId, refreshedToken);
    }
}