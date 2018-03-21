package com.trial.edupay.Network;

import com.trial.edupay.Model.Message;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

/**
 * Created by mallikapriyakhullar on 23/12/17.
 */

public interface NotificationApi {


    @GET ("/api/v1/users/me/messages")
    Call<ArrayList<Message>> getNotifications(@Header("Authorization") String authorization);

    @GET("/api/v1/users/me/students/{studentId}/messages/{messageId}")
    Call<Message> getNotificationById(
            @Header("Authorization") String authorization,
            @Path("studentId") String studentId,
            @Path("messageId") String messageId
    );


    @GET("/api/v1/users/me/students/{studentId}/messages/{messageId}/read")
    Call<ResponseBody>  informNotificationSeen(
            @Header("Authorization") String authorization,
            @Path("studentId") String studentId,
            @Path("messageId") String messageId
    );
}
