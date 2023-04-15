package com.example.readbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.readbook.api.API;
import com.example.readbook.response.ResponseMess;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgetPasswordActivity extends AppCompatActivity {

    private ImageView back;
    private EditText email;
    private EditText sendOtp;
    private TextView notification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        mapping();
        onclickBack();
        onclickSendOtp();
    }

    private void mapping() {
        back = findViewById(R.id.back);
        email = findViewById(R.id.email);
        sendOtp = findViewById(R.id.send_otp);
        notification = findViewById(R.id.notification);
    }

    private void onclickBack() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ForgetPasswordActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void onclickSendOtp() {
        sendOtp.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                // Test input
                if (email.getText().toString().equals("")) {
                    notification.setText("Chưa nhập đầy đủ thông tin");

                    ViewGroup.LayoutParams params = notification.getLayoutParams();
                    params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    notification.setLayoutParams(params);
                    return;
                }
                Intent intent = new Intent(ForgetPasswordActivity.this, OTPActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("backType", "forget");
                bundle.putString("email", email.getText().toString());

                intent.putExtra("data", bundle);
                startActivity(intent);

                // Send request forget password
                API.api.forgetPass(email.getText().toString()).enqueue(new Callback<ResponseMess>() {
                    @Override
                    public void onResponse(Call<ResponseMess> call, Response<ResponseMess> response) {
//                        ResponseMess mess = response.body();
//                        if (!mess.getSuccess()) {
//                            notification.setText(mess.getMess());
//
//                            ViewGroup.LayoutParams params = notification.getLayoutParams();
//                            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//                            notification.setLayoutParams(params);
//                            return;
//                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseMess> call, Throwable t) {

                    }
                });
            }
        });
    }
}