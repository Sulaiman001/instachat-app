package com.app.instachat.activities;

import static com.app.instachat.activities.constants.IConstants.EXTRA_LINK;
import static com.app.instachat.activities.constants.IConstants.EXTRA_USERNAME;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import androidx.appcompat.widget.Toolbar;

import com.app.instachat.activities.views.SingleClickListener;
import com.app.instachat.activities.managers.Utils;
import com.app.instachat.chat.activities.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class WebViewBrowserActivity extends BaseActivity {


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        try {
            final Toolbar mToolbar = findViewById(R.id.toolbar);
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getIntent().getStringExtra(EXTRA_USERNAME));
            mToolbar.setNavigationOnClickListener(new SingleClickListener() {
                @Override
                public void onClickView(View v) {
                    onBackPressed();
                }
            });
        } catch (Exception e) {
            Utils.getErrors(e);
        }


        final String linkPath = getIntent().getStringExtra(EXTRA_LINK);
        final WebView webView = findViewById(R.id.webView);

        StringBuilder sb = new StringBuilder();
        try {
            InputStream is = getAssets().open(linkPath);
            BufferedReader br;
            br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            String str;
            while ((str = br.readLine()) != null) {
                sb.append(str);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            String mimeType = "text/html;charset=UTF-8";
            String encoding = "utf-8";
            String htmlText = sb.toString();

            String text = "<html><head>"
                    + "<style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"file:///android_res/font/roboto_regular.ttf\")}body{font-family: MyFont;color: #8D8D8D;}"
                    + "</style></head>"
                    + htmlText
                    + "</body></html>";

            webView.loadDataWithBaseURL(null, text, mimeType, encoding, null);
        } catch (Exception e) {
            try {
                webView.loadUrl("file:///android_asset/" + linkPath);
                webView.getSettings().setJavaScriptEnabled(true);
            } catch (Exception en) {
                Utils.getErrors(en);
            }
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
