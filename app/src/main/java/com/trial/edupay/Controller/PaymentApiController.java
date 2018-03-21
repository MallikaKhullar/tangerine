package com.trial.edupay.Controller;

import android.util.Log;

import com.trial.edupay.Model.ConvenienceFee;
import com.trial.edupay.Network.RetroClient;
import com.trial.edupay.Utils.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mallikapriyakhullar on 10/01/18.
 */

public class PaymentApiController {

    public static void getConvenienceFee(int amount, final Callback<ConvenienceFee> cb) {
        RetroClient
                .getPayApi()
                .getConvenienceFee(Constants.getBearerKey(), amount)
                .enqueue(new Callback<ConvenienceFee>() {
                    @Override
                    public void onResponse(Call<ConvenienceFee> call, Response<ConvenienceFee> response) {
                        Log.d("RESPONSE: TRANSACTION", response.isSuccessful() + response.raw().toString());
                        if (response.isSuccessful()) {
                            cb.onResponse(call, response);
                        }
                    }

                    @Override
                    public void onFailure(Call<ConvenienceFee> call, Throwable t) {
                        Log.d("RESPONSE: TRANSACTION", t.getLocalizedMessage());
                        cb.onFailure(call, t);
                    }
                });
    }
}

