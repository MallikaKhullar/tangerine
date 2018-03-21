package com.trial.edupay.Modules.Base;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.trial.edupay.R;

import butterknife.BindView;

/**
 * Created by mallikapriyakhullar on 09/01/18.
 */

public class BaseActionBarActivity extends AppCompatActivity{

    @BindView(R.id.toolbar) Toolbar toolbar;

    /**
     * Each activity must implement this activity so that they could have the toolbar
     * @return is action bar set up or not
     */
    protected boolean setupActionBar() {
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            //set up the custom action bar layout
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.toolbar_layout);
            return true;
        }
        return false;
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
