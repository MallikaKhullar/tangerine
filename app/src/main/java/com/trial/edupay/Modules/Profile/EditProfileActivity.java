package com.trial.edupay.Modules.Profile;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.trial.edupay.Controller.LoginApiController;
import com.trial.edupay.Model.User;
import com.trial.edupay.Modules.Base.BaseActionBarActivity;
import com.trial.edupay.R;
import com.trial.edupay.Utils.ToastHandler;
import com.trial.edupay.Utils.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

public class EditProfileActivity extends BaseActionBarActivity implements Validator.ValidationListener{

    /**
     * VIEWS
     */
    @BindView(R.id.btnSave) View btnSave;
    @BindView(R.id.userImage) ImageView userImage;

    @NotEmpty(message = "Please enter your name")
    @BindView(R.id.userName) EditText userName;

    @NotEmpty(message = "Please enter your email")
    @BindView(R.id.userEmail) EditText userEmail;
    @BindView(R.id.userOccupation) EditText userOccupation;
    @BindView(R.id.userAddress) EditText userAddress;
    protected ProgressDialog progressDialog;

    /**
     * DATA
     */
    User user;

    Validator validator;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ButterKnife.bind(this);
        setupActionBar();
        validator = new Validator(this);
        validator.setValidationListener(this);
        setupUI();
        retrieveUser();
    }

    @Override
    protected boolean setupActionBar() {
        super.setupActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        return true;
    }
    
    void retrieveUser() {
        user = Utils.getUser();
        if (user != null) inflate();
    }
    
    void inflate() {
        userName.setText(user.name);
        userEmail.setText(user.email);
        userOccupation.setText(user.occupation);
        userAddress.setText(user.address);
    }
    
    void setupUI(){
        userAddress.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    submitUser();
                    return true;
                }
                return false;
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitUser();
            }
        });
    }

    void submitUser() {
        hideKeyboard(EditProfileActivity.this, userAddress);
        validator.validate();
    }

    public void hideKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * show progress bar
     */
    protected void showProgressBar() {
        if (progressDialog != null)
            progressDialog.show();
        else {
            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
        }
    }

    /**
     * hide progress bar
     */
    protected void hideProgressBar() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    @Override public void onValidationSucceeded() {
        showProgressBar();
        LoginApiController.saveUser(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                hideProgressBar();
                User user = response.body();
                if (user!=null){
                    Utils.saveUser(user);
                    finish();
                } else {
                    ToastHandler.showToast("Could not update user");
                    finish();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                hideProgressBar();
                ToastHandler.showToast("Could not update user");
                finish();
            }
        },userName.getText().toString(), userEmail.getText().toString(), userOccupation.getText().toString(), userAddress.getText().toString(), Utils.getUser().mobile);
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            if (view instanceof EditText) ((EditText) view).setError(message);
            else ToastHandler.showToast(message);
        }
    }
}
