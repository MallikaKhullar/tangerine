package com.trial.edupay;

import android.app.Application;
import android.content.Context;

/**
 * Created by mallikapriyakhullar on 23/12/17.
 */


public class MainApplication extends Application {

    private static Context context;
    private static MainApplication mInstance;

    public void onCreate() {
        super.onCreate();
        MainApplication.context = getApplicationContext();
        mInstance = this;
        //Fabric.with(this, new Crashlytics());
    }

    public static synchronized MainApplication getInstance() {
        return mInstance;
    }

    public static Context getAppContext() {
        return MainApplication.context;
    }
}