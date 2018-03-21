package com.trial.edupay.Utils;

import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.trial.edupay.MainApplication;

/**
 * Created by mallikapriyakhullar on 23/12/17.
 */

public class ToastHandler {
    public static void showToast(final String text){
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainApplication.getInstance(), text, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
