package com.trial.edupay.Modules.Profile;

import android.content.Intent;
import android.support.v4.widget.NestedScrollView;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.trial.edupay.Model.User;
import com.trial.edupay.Modules.Base.BaseActionBarActivity;
import com.trial.edupay.R;
import com.trial.edupay.Utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileActivity extends BaseActionBarActivity {


    @BindView(R.id.scrollView) NestedScrollView scrollView;
    @BindView(R.id.userPicture) ImageView userImage;
    @BindView(R.id.userName) TextView userName;
    @BindView(R.id.recycler) RecyclerView recyclerView;
    @BindView(R.id.userNumber) TextView userNumber;
    @BindView(R.id.userEmail) TextView userEmail;
    @BindView(R.id.userOccupation) TextView userOccupation;
    @BindView(R.id.userAddress) TextView userAddress;
    @BindView(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        setupActionBar();
    }

    @Override
    protected boolean setupActionBar() {
        super.setupActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        return true;
    }

    void addStudent(View view) {
        startActivity(new Intent(ProfileActivity.this,AddStudentActivity.class).putExtra("isLoggedIn", true));
    }

    void editProfile(View view){
        startActivity(new Intent(ProfileActivity.this, EditProfileActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        inflateData();
    }

    void inflateData(){
        User user = Utils.getUser();
        if(user!=null) {
            userNumber.setText(user.mobile + "");
            userName.setText(user.name + "");
            userAddress.setText(user.address + "");
            userEmail.setText(user.email + "");
            userOccupation.setText(user.occupation + "");
        }
    }
}
