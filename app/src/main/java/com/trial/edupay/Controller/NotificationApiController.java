package com.trial.edupay.Controller;


import com.trial.edupay.Model.Message;
import com.trial.edupay.Network.NotificationApi;
import com.trial.edupay.Network.RetroClient;
import com.trial.edupay.Utils.Constants;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Callback;

/**
 * Created by mallikapriyakhullar on 05/01/18.
 */

public class NotificationApiController {

    public static NotificationApi getNotificationApi() {
        return RetroClient.getNotificationApi();
    }


    /**
     * Asks the server for all notifications
     * @param cb
     */
    public static void getNotifications(Callback<ArrayList<Message>> cb) {
        getNotificationApi()
            .getNotifications(Constants.getBearerKey())
            .enqueue(cb);
    }

    public static void getNotificationById(String studentId, String messageId, Callback<Message> cb) {
        getNotificationApi()
            .getNotificationById(Constants.getBearerKey(), studentId, messageId)
            .enqueue(cb);
    }

    public static void informNotificationSeen(String studentId, String messageId, Callback<ResponseBody> cb) {
        getNotificationApi()
                .informNotificationSeen(Constants.getBearerKey(), studentId, messageId)
                .enqueue(cb);
    }
}

