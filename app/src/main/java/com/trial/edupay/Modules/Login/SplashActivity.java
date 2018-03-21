package com.trial.edupay.Modules.Login;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.trial.edupay.Controller.DataSourceControl;
import com.trial.edupay.Controller.LoginApiController;
import com.trial.edupay.Model.User;
import com.trial.edupay.Modules.Profile.AddStudentActivity;
import com.trial.edupay.R;
import com.trial.edupay.Modules.Base.ContainerActivity;
import com.trial.edupay.Utils.SystemUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    void attemptLogin(String mobile) {

        LoginApiController.getUserWithNumber(mobile, new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user = response.body();

                if (user != null) { //user exists

                    if (user.students != null && !user.students.isEmpty()) redirectToApp(); //user exists, with a student
                    else redirectToChildAdd(); //user exists, no student

                } else { redirectToLogin(); } //user doesn't exist
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                //API fail
                redirectToLogin();
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        switch (DataSourceControl.getUserState()) {
            case LOGGED_IN:
                redirectToApp();
                break;
            case CHILD_PENDIN:
                redirectToChildAdd();
                break;

            //for the rest, we'll first try auto login.. if that doesn't work, we'll take him to manual log in page
            case UNSIGNED:

                if (isPermissionGranted()) {
                    String devicePhoneNumber = SystemUtils.getUserPhoneNumber();
                    if (devicePhoneNumber != null && SystemUtils.isValidPhone(devicePhoneNumber)) {
                        // phone number available on the device
                        attemptLogin(devicePhoneNumber);
                        break;
                    } else {
                        //phone number unavailable on the device
                        redirectToLogin();
                        break;
                    }
                } else {
                    // permission to get phone number not granted
                    redirectToLogin();
                }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    String devicePhoneNumber = SystemUtils.getUserPhoneNumber();
                    if (devicePhoneNumber != null && SystemUtils.isValidPhone(devicePhoneNumber)) {
                        attemptLogin(devicePhoneNumber);
                        break;
                    } else {
                        redirectToLogin();
                        break;
                    }

                } else {
                    redirectToLogin();
                }
            }
        }
    }

    public boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.READ_SMS)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG", "Permission is granted");
                return true;
            } else {
                Log.v("TAG", "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_SMS}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG", "Permission is granted");
            return true;
        }
    }


    void redirectToApp() {
        DataSourceControl.setUserState(DataSourceControl.UserState.LOGGED_IN);
        startActivity(new Intent(SplashActivity.this, ContainerActivity.class));
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    void redirectToChildAdd() {
        DataSourceControl.setUserState(DataSourceControl.UserState.CHILD_PENDIN);
        startActivity(new Intent(SplashActivity.this,AddStudentActivity.class).putExtra("isLoggedIn", false));
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    void redirectToLogin() {
        DataSourceControl.setUserState(DataSourceControl.UserState.UNSIGNED);
        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
