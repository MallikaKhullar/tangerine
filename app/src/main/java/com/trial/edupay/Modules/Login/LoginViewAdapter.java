package com.trial.edupay.Modules.Login;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.goodiebag.pinview.Pinview;
import com.trial.edupay.Controller.LoginApiController;
import com.trial.edupay.R;
import com.trial.edupay.Utils.BasicCallback;
import com.trial.edupay.Utils.TextValidator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mallikapriyakhullar on 28/12/17.
 */

public class LoginViewAdapter extends PagerAdapter {

    /**
     * ALL VIEWS FOR THE ADAPTER
     */
    View otpPrev;
    View otpProgress;
    View otpSubmit;
    TextView phoneNext;
    View phoneProgress;
    EditText etPhone;
    Pinview etOtp;
    ViewGroup mainLayout;


    String mobile;

    private Context mContext;
    NavigationButtonClickedListener buttonClicked;

    /**
     * Constructor for the adapter
     * @param context
     * @param buttonClicked
     */
    public LoginViewAdapter(Context context, NavigationButtonClickedListener buttonClicked) {
        mContext = context;
        this.buttonClicked = buttonClicked;
    }


    @Override public Object instantiateItem(ViewGroup collection, int position) {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        final ViewGroup layout;

        switch (position) {
            case 0:
            default:
                mainLayout = layout = (ViewGroup) inflater.inflate(R.layout.item_phone_view, collection, false);
                initPhoneView(layout);
                break;
            case 1:
                mainLayout = layout = (ViewGroup) inflater.inflate(R.layout.item_view_otp, collection, false);
                initOTPView(layout);
                break;
        }

        collection.addView(layout);
        return layout;
    }

    @Override public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override public int getCount() {
        return 2;
    }

    @Override public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override public CharSequence getPageTitle(int position) {
        return "";
    }

    void triggerOtp(String mobile, final BasicCallback basicCallback) {
        LoginApiController.triggerOtp(mobile, new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                basicCallback.onSuccess();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                basicCallback.onError();
            }
        });
    }

    void submitOtp() {
        otpProgress.setVisibility(View.VISIBLE);
        buttonClicked.onLogin(etOtp.getValue().toString());
    }

    void submitPhone() {
        if (etPhone.getText() == null || etPhone.getText().toString().isEmpty() || etPhone.getText().toString().length() != 10) {
            etPhone.setError("Please enter a valid 10 digit number");
            return;
        }

        mobile = etPhone.getText().toString();

        phoneProgress.setVisibility(View.VISIBLE);
        triggerOtp(etPhone.getText().toString(), new BasicCallback() {
            @Override
            public void onSuccess() {
                buttonClicked.onNext(etPhone.getText().toString());
                phoneProgress.setVisibility(View.GONE);
            }

            @Override
            public void onError() {
                phoneProgress.setVisibility(View.GONE);
            }
        });
    }

    void initPhoneView(ViewGroup layout) {
        phoneNext = layout.findViewById(R.id.tvNext);
        phoneProgress = layout.findViewById(R.id.progress);
        etPhone = layout.findViewById(R.id.etPhone);

        //remove errors if he's re-typing
        etPhone.addTextChangedListener(new TextValidator(etPhone) {
            @Override public void validate(TextView textView, String text) {
                if (text != null && !text.isEmpty() && text.length() == 10) { etPhone.setError(null); }
            }
        });

        phoneNext.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                hideKeyboard(mContext, etPhone);
                submitPhone();
            }
        });

        //auto submission when he presses 'done' on the keyboard
        etPhone.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    hideKeyboard(mContext, etPhone);
                    submitPhone();
                    return true;
                }
                return false;
            }
        });
    }

    void initOTPView(ViewGroup layout) {
        otpPrev = layout.findViewById(R.id.tvBack);
        otpProgress = layout.findViewById(R.id.progress);
        otpSubmit = layout.findViewById(R.id.tvSubmit);
        etOtp = layout.findViewById(R.id.etOtp);

        otpPrev.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                buttonClicked.onPrev();
            }
        });

        otpSubmit.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                hideKeyboard(mContext, etOtp);
                submitOtp();
            }
        });

        //auto submission when he enters all four digits
        etOtp.setPinViewEventListener(new Pinview.PinViewEventListener() {
            @Override public void onDataEntered(Pinview pinview, boolean b) {
                hideKeyboard(mContext, etOtp);
                submitOtp();
            }
        });
    }

    public void hideKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}