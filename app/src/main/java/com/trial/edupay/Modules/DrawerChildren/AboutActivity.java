package com.trial.edupay.Modules.DrawerChildren;

import android.os.Bundle;
import android.webkit.WebView;

import com.trial.edupay.Modules.Base.BaseActionBarActivity;
import com.trial.edupay.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AboutActivity extends BaseActionBarActivity {

    @BindView(R.id.webView) WebView webView;

    String htmData = "<html>\n" +
                "<head> <style> body{ font-size: 14px; font-color: #505050; text-align:justify; } </style> </head>\n" +
                "<body>\n" +
                "EduPay About section HTML goes here" +
                "</body>\n" +
                "</html>";

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_about);
            ButterKnife.bind(this);
            setupActionBar();
            webView.loadData(htmData, "text/html", null);
        }

    @Override
    protected boolean setupActionBar() {
        super.setupActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        return true;
    }
}
