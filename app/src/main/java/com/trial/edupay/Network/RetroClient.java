package com.trial.edupay.Network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.trial.edupay.Utils.Constants;
import com.trial.edupay.Utils.DateTimeAdapter;

import org.joda.time.DateTime;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mallikapriyakhullar on 23/12/17.
 */

public class RetroClient {

    private static final String ROOT_URL = Constants.API;

    private static Retrofit getRetrofitInstance() {
        Gson gson =
                new GsonBuilder()
                .registerTypeAdapter(DateTime.class, new DateTimeAdapter())
                .create();

        return
                new Retrofit.Builder()
                .baseUrl(ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    /**
     * Used to fetch UserAPI, used to reach the server for USER DATA
     * @return instance of UserAPI
     */
    public static UserApi getUserApi() {
        return getRetrofitInstance().create(UserApi.class);
    }

    /**
     * Used to fetch NotificationApi, used to reach the server for NOTIFICATION DATA
     * @return instance of NotificationApi
     */
    public static NotificationApi getNotificationApi() {
        return getRetrofitInstance().create(NotificationApi.class);
    }


    public static StudentApi getStudentApi() {
        return getRetrofitInstance().create(StudentApi.class);
    }

    public static OrganizationApi getOrganizationApi() {
        return getRetrofitInstance().create(OrganizationApi.class);
    }


    public static PayApi getPayApi() {
        return getRetrofitInstance().create(PayApi.class);
    }
}