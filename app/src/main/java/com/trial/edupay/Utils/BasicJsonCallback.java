package com.trial.edupay.Utils;

import org.json.JSONObject;

/**
 * Created by mallikapriyakhullar on 24/12/17.
 */

public interface BasicJsonCallback {
    public void onSuccess(JSONObject response);
    public void onError();
}
