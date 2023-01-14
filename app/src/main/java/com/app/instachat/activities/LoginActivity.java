package com.app.instachat.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.app.instachat.activities.views.SingleClickListener;
import com.app.instachat.activities.managers.Utils;
import com.app.instachat.chat.activities.R;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends BaseActivity {

    private EditText mTxtEmail;
    private EditText mTxtPassword;

    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mTxtEmail = findViewById(R.id.txtEmail);
        mTxtPassword = findViewById(R.id.txtPassword);
        final Button mBtnSignUp = findViewById(R.id.btnSignUp);
        final TextView mTxtNewUser = findViewById(R.id.txtNewUser);
        final TextView txtForgetPassword = findViewById(R.id.txtForgetPassword);

        auth = FirebaseAuth.getInstance();

        Utils.setHTMLMessage(mTxtNewUser, getString(R.string.strNewSignUp));

        txtForgetPassword.setOnClickListener(new SingleClickListener() {
            @Override
            public void onClickView(View v) {
                screens.showCustomScreen(ForgetPasswordActivity.class);
            }
        });

        mBtnSignUp.setOnClickListener(new SingleClickListener() {
            @Override
            public void onClickView(View v) {
                String strEmail = mTxtEmail.getText().toString().trim();
                String strPassword = mTxtPassword.getText().toString().trim();

                if (TextUtils.isEmpty(strEmail) || TextUtils.isEmpty(strPassword)) {
                    screens.showToast(R.string.strAllFieldsRequired);
                } else {
                    login(strEmail, strPassword);
                }
            }
        });
        mTxtNewUser.setOnClickListener(new SingleClickListener() {
            @Override
            public void onClickView(View v) {
                screens.showCustomScreen(RegisterActivity.class);
            }
        });
    }

    private void login(String email, String password) {
        showProgress();

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            hideProgress();
            if (task.isSuccessful()) {
                screens.showClearTopScreen(MainActivity.class);
            } else {
                screens.showToast(R.string.strInvalidEmailPassword);
            }
        }).addOnFailureListener(e -> hideProgress()).addOnCanceledListener(this::hideProgress);
    }
}
