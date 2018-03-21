package com.trial.edupay.Model;

import android.util.Log;

import com.trial.edupay.Utils.DateTimeAdapter;
import com.google.gson.GsonBuilder;

import org.joda.time.DateTime;

/**
 * Created by mallikapriyakhullar on 09/01/18.
 */

public class BaseEntity {

    public DateTime createdAt;

    public String toJson() {
        return new GsonBuilder().registerTypeAdapter(DateTime.class, new DateTimeAdapter()).create().toJson(this);
    }

    public static <T> T fromJson(String jsonData, Class<T> classOfT) {
        if (jsonData == null)
            return null;

        try {
            return new GsonBuilder().registerTypeAdapter(DateTime.class, new DateTimeAdapter()).create().fromJson(jsonData, classOfT);
        } catch (Exception ex) {
            Log.e("Contact Parse", "Error parsing contact", ex);
            return null;
        }
    }
}
