package com.trial.edupay.Modules.Login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.trial.edupay.Controller.DataSourceControl;
import com.trial.edupay.Controller.LoginApiController;
import com.trial.edupay.Controller.SharedPref;
import com.trial.edupay.Model.AuthToken;
import com.trial.edupay.Model.User;
import com.trial.edupay.Modules.Profile.AddStudentActivity;
import com.trial.edupay.R;
import com.trial.edupay.Modules.Base.ContainerActivity;
import com.trial.edupay.Utils.BasicCallback;
import com.trial.edupay.Utils.ToastHandler;
import com.trial.edupay.Utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.view_pager)
    NonSwipeableViewPager viewPager;
    DataController dataController;
    UiController uiController;
    String enteredMobile, enteredOtp;

    void setupPager() {
        viewPager.setAdapter(new LoginViewAdapter(this, new NavigationButtonClickedListener() {
            @Override public void onNext(String mobile) {
                enteredMobile = mobile;
                viewPager.setCurrentItem(1);
            }

            @Override public void onPrev() {viewPager.setCurrentItem(0);}

            @Override public void onLogin(String otp) {
                enteredOtp = otp;
                startLoginProcess(otp);
            }
        }));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        dataController = new DataController();
        uiController = new UiController();
        setupPager();
    }


    void startLoginProcess(String otp) {
        LoginApiController.getAuthWithOtp(enteredMobile, otp, new Callback<AuthToken>() {

            @Override public void onResponse(Call<AuthToken> call, Response<AuthToken> response) {

                //TODO - null checks
                if(response == null || response.body() == null) {
                    uiController.showError();
                    return;
                }

                dataController
                        .storeAuth(response)
                        .fetchUser(new BasicCallback() {
                            @Override public void onSuccess() {
                                User user = Utils.getUser();
                                if(user.students == null || user.students.isEmpty())
                                    uiController.redirectToStudentAdd();
                                uiController.mainScreenRedirect();
                            }
                            @Override public void onError() {uiController.showError();}
                        });
            }

            @Override public void onFailure(Call<AuthToken> call, Throwable t) {uiController.showError();}
        });
    }


    class UiController {
        void mainScreenRedirect() {
            DataSourceControl.setUserState(DataSourceControl.UserState.LOGGED_IN);
            startActivity(new Intent(LoginActivity.this, ContainerActivity.class));
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }

        void redirectToStudentAdd(){
            DataSourceControl.setUserState(DataSourceControl.UserState.CHILD_PENDIN);
            startActivity(new Intent(LoginActivity.this,AddStudentActivity.class).putExtra("isLoggedIn", false));
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }

        void showError() {
            //couldn't fetch user with this phone number or couldn't fetch auth token
            //TODO - handle
            ToastHandler.showToast("Sorry, incorrect OTP");
        }
    }

    class DataController {
        DataController storeAuth(Response<AuthToken> response) {
            SharedPref.getInstance().setString(SharedPref.PFAuthKey, response.body().token);
            SharedPref.getInstance().setString(SharedPref.PFBearerKey, "Bearer " + response.body().token);
            SharedPref.getInstance().setString(SharedPref.PFMobile, enteredMobile);
            return this;
        }

        DataController storeUser(User user) {
            Utils.saveUser(user);
            SharedPref.getInstance().setString(SharedPref.PFEmail, user.email);
            return this;
        }

        DataController fetchUser(final BasicCallback callback) {

            //TODO - uncomment the following when API done
            LoginApiController.getMe(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    storeUser(response.body());
                    //todo - find out what shit this is
                        sendRegistrationToServer();
//                        if(AppUtil.getCurrentFee() != null)
//                            addStudent(AppUtil.getCurrentFee().student);
                    callback.onSuccess();
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    callback.onError();
                }
            });
            return this;
        }
    }

    protected void sendRegistrationToServer() {
        String token = SharedPref.getInstance().getString(SharedPref.PFFcmId);
        if(token == null) return;

        LoginApiController.sendFcmRegistrationId(token, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
}
