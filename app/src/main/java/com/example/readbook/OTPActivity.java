package com.example.readbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.readbook.accoount.Account;
import com.example.readbook.api.API;
import com.example.readbook.response.ResponseMess;
import com.example.readbook.saveLogin.SaveLogin;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OTPActivity extends AppCompatActivity {

    private ImageView back;
    private EditText otp;
    private EditText next;
    private TextView notification;

    private SaveLogin saveLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        /*
            Intent receive from:
                + SignUpActivity about: String backType
                + ForgetPasswordActivity about: String backType, String email
         */
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("data");

        mapping();
        onclickBack(bundle);
        onclickNext(bundle);
    }

    private void mapping() {
        back = findViewById(R.id.back);
        otp = findViewById(R.id.otp);
        next = findViewById(R.id.next);
        notification = findViewById(R.id.notification);

        saveLogin = new SaveLogin(this);
    }

    private void onclickBack(Bundle bundle) {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bundle.getString("backType").equals("signUp")) {
                    Intent intent = new Intent(OTPActivity.this, SignUpActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(OTPActivity.this, ForgetPasswordActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void onclickNext(Bundle bundle) {
        next.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                // Test input
                if (otp.getText().toString().equals("")) {
                    notification.setText("Chưa nhập đầy đủ thông tin");

                    ViewGroup.LayoutParams params = notification.getLayoutParams();
                    params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    notification.setLayoutParams(params);
                    return;
                }

                // Send request if type is sign up
                if (bundle.getString("backType").equals("signUp")) {
                    API.api.create(otp.getText().toString()).enqueue(new Callback<Account>() {
                        @Override
                        public void onResponse(Call<Account> call, Response<Account> response) {
                            Account account = response.body();
                            if (account.isSuccess()) {
                                saveLogin.save(account.getEmail(), account.getName(), "email");
                                Intent intent = new Intent(OTPActivity.this, AccountActivity.class);
                                startActivity(intent);
                                return;
                            }
                            notification.setText("OTP không chính xác");

                            ViewGroup.LayoutParams params = notification.getLayoutParams();
                            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                            notification.setLayoutParams(params);
                        }

                        @Override
                        public void onFailure(Call<Account> call, Throwable t) {

                        }
                    });
                } else {
                    // Send request if type is forget password
                    API.api.verificationForgetPass(otp.getText().toString()).enqueue(new Callback<ResponseMess>() {
                        @Override
                        public void onResponse(Call<ResponseMess> call, Response<ResponseMess> response) {
                            ResponseMess mess = response.body();
                            if (!mess.getSuccess()) {
                                notification.setText("OTP không chính xác");

                                ViewGroup.LayoutParams params = notification.getLayoutParams();
                                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                                notification.setLayoutParams(params);
                                return;
                            }

                            Intent intent = new Intent(OTPActivity.this, ChangePasswordActivity.class);
                            Bundle bundleSend = new Bundle();
                            bundleSend.putString("email", bundle.getString("email"));

                            intent.putExtra("data", bundleSend);
                            startActivity(intent);
                        }

                        @Override
                        public void onFailure(Call<ResponseMess> call, Throwable t) {

                        }
                    });
                }
            }
        });
    }
}