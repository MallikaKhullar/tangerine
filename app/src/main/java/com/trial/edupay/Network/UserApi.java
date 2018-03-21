package com.trial.edupay.Network;

import com.trial.edupay.Model.AuthToken;
import com.trial.edupay.Model.Fee;
import com.trial.edupay.Model.Transaction;
import com.trial.edupay.Model.User;
import com.trial.edupay.Utils.Constants;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by mallikapriyakhullar on 23/12/17.
 */


public interface UserApi {


    @FormUrlEncoded
    @POST (Constants.USER_ENDPOINT + "/otp")
    Call<String> triggerOtp(@Field("mobile") String mobile);

    @FormUrlEncoded
    @POST (Constants.USER_ENDPOINT + "/user") //TODO - implement after talking to Ashish
    Call<User> userExist(@Field("mobile") String mobile);

    @FormUrlEncoded
    @POST (Constants.USER_ENDPOINT + "/verifyOtp")
    Call<AuthToken> getAuthWithOtp(@Field ("mobile") String mobile, @Field ("otp") String otp);

    @GET ("/api/v1/users/me/feeReminders")
    Call<ArrayList<Fee>> getFeeReminders(@Header("Authorization") String authorization);

    @GET("/api/v1/users/me/transactions")
    Call<ArrayList<Transaction>> getMyTransactions(@Header("Authorization") String authorization);

    @GET("/api/v1/users/me")
    Call<User> getUser(@Header("Authorization") String authorization);


    @FormUrlEncoded
    @POST ("/api/v1/users/me")
    Call<User> saveUser (
            @Header ("Authorization") String authorization,
            @Field ("name") String name,
            @Field ("email") String email,
            @Field ("occupation") String occupation,
            @Field ("address") String address,
            @Field ("mobile") String mobile
    );

    @FormUrlEncoded
    @POST("/api/v1/users/me/addFcmId")
    Call<ResponseBody> sendFcmRegistrationId(
            @Header("Authorization") String authorization,
            @Field("fcmId") String id
    );



//
//    @FormUrlEncoded
//    @POST (drillDown + "/otp")
//    void getOtp (
//            @Field ("mobile") String mobile,
//            Callback<String> cb
//    );
//
//
//    @FormUrlEncoded
//    @POST (drillDown + "/me")
//    void saveUser (
//            @Header ("Authorization") String authorization,
//            @Field ("name") String name,
//            @Field ("email") String email,
//            @Field ("dob") String dob,
//            @Field ("occupation") String occupation,
//            @Field ("address") String address,
//            Callback<Response> cb
//    );
//
//    @FormUrlEncoded
//    @POST (drillDown + "/me/addFcmId")
//    void sendFcmRegistrationId (
//            @Header ("Authorization") String authorization,
//            @Field ("fcmId") String id,
//            Callback<Response> cb
//    );
//
//    @FormUrlEncoded
//    @POST (drillDown + "/me/removeFcmId")
//    void unregisterFCM (
//            @Header ("Authorization") String authorization,
//            @Field ("fcmId") String id,
//            Callback<Response> cb
//    );
//
////    @GET (drillDown + "/me")
//    void getUser (
//            @Header("Authorization") String authorization,
//            Callback<User> cb
//    );

//    @GET (drillDown + "/me/feeReminders")
//    void getFeeReminders (
//            @Header ("Authorization") String authorization,
//            Callback<ArrayList<Fee>> cb
//    );
//
//    @GET (drillDown + "/me/transactions")
//    void getMyTransactions (
//            @Header ("Authorization") String authorization,
//            Callback<ArrayList<Transaction>> cb
//    );
//    @GET (drillDown + "/me/transactions")
//    void getMyTransactions1 (
//            @Header ("Authorization") String authorization,
//            Callback<ArrayList<Transaction1>> cb
//    );
//
//
//    @POST (drillDown + "/appPayment")
//    void savePaymentDetails (
//            @Body PaymentDetail paymentDetail,
//            Callback<Response> cb
//    );
//@FormUrlEncoded
//@POST ("/api/v1/convenienceFee")
//void getConvenienceFee (
//        @Header ("Authorization") String authorization,
//        @Field ("amount") int amount,
//        Callback<ConvenienceFee> cb
//);



}
