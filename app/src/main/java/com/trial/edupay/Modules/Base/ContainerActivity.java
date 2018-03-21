package com.trial.edupay.Modules.Base;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.trial.edupay.Controller.SharedPref;
import com.trial.edupay.Model.User;
import com.trial.edupay.Modules.Current.RemindersFragment;
import com.trial.edupay.Modules.DrawerChildren.AboutActivity;
import com.trial.edupay.Modules.History.HistoryFragment;
import com.trial.edupay.Modules.Login.SplashActivity;
import com.trial.edupay.Modules.Notices.NoticesFragment;
import com.trial.edupay.Modules.Profile.ProfileActivity;
import com.trial.edupay.R;
import com.trial.edupay.Utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContainerActivity extends BaseActionBarActivity {

    @BindView(R.id.drawer_layout) DrawerLayout drawerLayout;
    @BindView(R.id.navigation) BottomNavigationView navigation;
    @BindView(R.id.viewpager) ViewPager viewPager;
    @BindView(R.id.tvUserName) TextView tvUserName;
    @BindView(R.id.tvUserNumber) TextView tvUserNumber;

    NoticesFragment noticesFragment;
    RemindersFragment remindersFragment;
    HistoryFragment historyFragment;

    Boolean drawerOpen = false;

    ActionBarDrawerToggle mDrawerToggle;
    MenuItem prevMenuItem;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_current: viewPager.setCurrentItem(0); return true;
                case R.id.nav_notifications: viewPager.setCurrentItem(1); return true;
                case R.id.nav_history: viewPager.setCurrentItem(2); return true;
            }
            return false;
        }
    };


    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);
        ButterKnife.bind(this);
        initUI();
        setupViewPager();
        inflateNavDrawer();
    }

    /**
     * Initializes the UI
     */
    void initUI(){
        if(setupActionBar()) setupDrawer();
        //set up the click event listeners for bottom navigation
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) navigation.getChildAt(0);
        for (int i = 0; i < menuView.getChildCount(); i++) {
            final View iconView = menuView.getChildAt(i).findViewById(android.support.design.R.id.icon);
            final ViewGroup.LayoutParams layoutParams = iconView.getLayoutParams();
            final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 22, displayMetrics);
            layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 22, displayMetrics);
            iconView.setLayoutParams(layoutParams);
        }
    }

    @Override protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    /**
     * TRIGGERED ON CLICK OF FEEDBACK
     * @param view
     */
    void openFeedback(View view){
        drawerLayout.closeDrawer(Gravity.LEFT);
//        String[] TO = {"support@qles.com"};
        String[] TO = {"mallikapriyak@gmail.com"};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback for QLes app");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "");
        startActivity(emailIntent);
    }

    /**
     * TRIGGERED ON CLICK OF LOGIN
     * @param view
     */
    void openLogout(View view){
        drawerLayout.closeDrawer(Gravity.LEFT);
        SharedPref.getInstance().clearAll();
        startActivity(new Intent(ContainerActivity.this, SplashActivity.class));
    }

    /**
     * TRIGGERED ON CLICK OF RATE
     * @param view
     */
    void openRate(View view){
        drawerLayout.closeDrawer(Gravity.LEFT);
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.gaps.qles&hl=en")));
    }

    /**
     * TRIGGERED ON CLICK OF PROFILE
     * @param view
     */
    void openProfile(View view){
        drawerLayout.closeDrawer(Gravity.LEFT);
        startActivity(new Intent(ContainerActivity.this, ProfileActivity.class));
    }

    /**
     * TRIGGERED ON CLICK OF ABOUT
     * @param view
     */
    void openAbout(View view){
        drawerLayout.closeDrawer(Gravity.LEFT);
        startActivity(new Intent(ContainerActivity.this, AboutActivity.class));
    }

    private void setupViewPager() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                }
                else {
                    navigation.getMenu().getItem(0).setChecked(false);
                }
                navigation.getMenu().getItem(position).setChecked(true);
                prevMenuItem = navigation.getMenu().getItem(position);
            }

            @Override public void onPageScrollStateChanged(int state) {}
        });

        FragmentViewPagerAdapter adapter = new FragmentViewPagerAdapter(getSupportFragmentManager());
        noticesFragment=new NoticesFragment();
        remindersFragment =new RemindersFragment();
        historyFragment=new HistoryFragment();
        adapter.addFragment(remindersFragment);
        adapter.addFragment(noticesFragment);
        adapter.addFragment(historyFragment);
        viewPager.setOffscreenPageLimit(4);
        viewPager.setAdapter(adapter);
    }

    void setupDrawer(){
        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name) {

            public void onDrawerClosed(View view) {
                supportInvalidateOptionsMenu(); drawerOpen = false;
            }

            public void onDrawerOpened(View drawerView) {
                supportInvalidateOptionsMenu(); drawerOpen = true;
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if(drawerOpen && drawerLayout!=null) {
            drawerLayout.closeDrawer(Gravity.LEFT);
            return;
        }
        super.onBackPressed();
    }

    void inflateNavDrawer(){
        User user = Utils.getUser();
        if(user == null) return;
        tvUserName.setText(user.name == null || user.name.isEmpty() ? "Your Profile" : user.name + "");
        tvUserNumber.setText(user.mobile + "");
    }
}
