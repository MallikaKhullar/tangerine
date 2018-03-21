package com.trial.edupay.Controller;

import com.trial.edupay.Model.AuthToken;
import com.trial.edupay.Model.User;
import com.trial.edupay.Network.RetroClient;
import com.trial.edupay.Network.UserApi;
import com.trial.edupay.Utils.Constants;


import okhttp3.ResponseBody;
import retrofit2.Callback;

/**
 * Created by mallikapriyakhullar on 23/12/17.
 */

public class LoginApiController {

    public static UserApi getUserApi() {
        return RetroClient.getUserApi();
    }

    /**
     * Hits the server with the mobile and entered OTP, receives the auth token
     *
     * @param mobile
     * @param otp
     * @param cb
     */
    public static void getAuthWithOtp(String mobile, String otp, final Callback<AuthToken> cb) {
        getUserApi()
                .getAuthWithOtp(mobile, otp)
                .enqueue(cb);
    }

    /**
     * Hits the server to fetch the OTP that was generated for this mobile phone
     *
     * @param mobile
     * @param cb
     */
    public static void triggerOtp(String mobile, final Callback<String> cb) {
        getUserApi()
                .triggerOtp(mobile)
                .enqueue(cb);
    }


    /**
     * Used for auto login - asks the server for user found under a certain mobile num.
     *
     * @param mobile
     * @param cb
     */
    public static void getUserWithNumber(String mobile, final Callback<User> cb) { //TODO: Use when API is made
        getUserApi()
                .userExist(mobile)
                .enqueue(cb);
    }

    public static void getMe(final Callback<User> cb) {
        getUserApi()
                .getUser(Constants.getBearerKey())
                .enqueue(cb);
    }

    public static void saveUser(final Callback<User> cb, String... args) {
        getUserApi()
                .saveUser(Constants.getBearerKey(), args[0], args[1], args[2], args[3], args[4])
                .enqueue(cb);
    }

    public static void sendFcmRegistrationId(String id, Callback<ResponseBody> cb) {
        getUserApi()
                .sendFcmRegistrationId(Constants.getBearerKey(), id)
                .enqueue(cb);
    }
}
