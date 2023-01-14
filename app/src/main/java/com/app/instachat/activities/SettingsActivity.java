package com.app.instachat.activities;

import static com.app.instachat.activities.constants.IConstants.FALSE;
import static com.app.instachat.activities.constants.IConstants.TRUE;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import com.app.instachat.activities.views.SingleClickListener;
import com.app.instachat.chat.activities.BuildConfig;
import com.app.instachat.activities.managers.SessionManager;
import com.app.instachat.activities.managers.Utils;
import com.app.instachat.chat.activities.R;

public class SettingsActivity extends BaseActivity implements View.OnClickListener {

    private SwitchCompat notificationOnOff, rtlOnOff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        final Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.strSettings);
        mToolbar.setNavigationOnClickListener(new SingleClickListener() {
            @Override
            public void onClickView(View v) {
                onBackPressed();
            }
        });

        final LinearLayout layoutNotification = findViewById(R.id.layoutNotification);
        final LinearLayout layoutLogout = findViewById(R.id.layoutLogout);

        final TextView mTxtVersionName = findViewById(R.id.txtAppVersion);
        mTxtVersionName.setText(String.format(getString(R.string.settingVersion), BuildConfig.VERSION_NAME));

        notificationOnOff = findViewById(R.id.notificationOnOff);
        notificationOnOff.setOnCheckedChangeListener((compoundButton, b) -> SessionManager.get().setOnOffNotification(b));

        if (SessionManager.get().isNotificationOn()) {
            notificationOnOff.setChecked(TRUE);
        } else {
            notificationOnOff.setChecked(FALSE);
        }

        layoutNotification.setOnClickListener(this);
        layoutLogout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        final int id = view.getId();
        if (id == R.id.layoutNotification) {
            if (notificationOnOff.isChecked()) {
                notificationOnOff.setChecked(FALSE);
            } else {
                notificationOnOff.setChecked(TRUE);
            }
        }else if (id == R.id.layoutLogout) {
            Utils.logout(mActivity);
        }
    }

    private void restartApp() {
        Utils.showOKDialog(mActivity, R.string.ref_title, R.string.ref_message,
                () -> screens.showClearTopScreen(MainActivity.class));
    }

}
