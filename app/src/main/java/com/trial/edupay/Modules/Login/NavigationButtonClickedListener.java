package com.trial.edupay.Modules.Login;

/**
 * Created by mallikapriyakhullar on 05/01/18.
 */

public interface NavigationButtonClickedListener {
    void onNext(String mobile);
    void onPrev();
    void onLogin(String otp);
}
