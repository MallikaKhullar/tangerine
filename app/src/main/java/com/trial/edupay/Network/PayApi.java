package com.trial.edupay.Network;

import com.trial.edupay.Model.ConvenienceFee;
import com.trial.edupay.Utils.Constants;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by mallikapriyakhullar on 23/12/17.
 */

public interface PayApi {

    final String drillDown = Constants.API_VERSION +  "/users";


    @FormUrlEncoded
    @POST("/api/v1/convenienceFee")
    Call<ConvenienceFee> getConvenienceFee(
            @Header("Authorization") String authorization,
            @Field("amount") int amount
    );

//    @POST (drillDown + "/appPayment")
//    void savePaymentDetails (
//            @Body PaymentDetail paymentDetail,
//            Callback<Response> cb
//    );
//
//    @FormUrlEncoded
//    @POST (drillDown + "/convenienceFee")
//    void getConvenienceFee (
//            @Header("Authorization") String authorization,
//            @Field ("amount") int amount,
//            Callback<ConvenienceFee> cb
//    );
//
//    @GET (drillDown + "/students")
//    void getFeeDetailsFromLink (
//            @Query ("shortCode") String shortCode,
//            @Query ("studentId") String studentId,
//            Callback<ArrayList<Fee>> cb
//    );
//
//    @POST ("/pay/cart")
//    void addToCart (
//            @Body CartItem cartItem,
//            Callback<CartItem> cb
//    );
//
//    @POST ("/pay/paymentGatewayKey")
//    void getPGKey (
//            @Body PGKeyParams pgKeyParams,
//            Callback<String> cb
//    );
}
