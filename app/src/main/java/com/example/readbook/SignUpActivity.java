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

public class SignUpActivity extends AppCompatActivity {

    private ImageView back;
    private EditText email;
    private EditText name;
    private EditText password;
    private EditText repassword;
    private EditText signUp;
    private TextView notification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mapping();
        onclickBack();
        onclickSignUp();
    }

    private void mapping() {
        back = findViewById(R.id.back);
        email = findViewById(R.id.email);
        name = findViewById(R.id.name);
        password = findViewById(R.id.password);
        repassword = findViewById(R.id.repassword);
        signUp = findViewById(R.id.sign_up);
        notification = findViewById(R.id.notification);
    }

    private void onclickBack() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void onclickSignUp() {
        signUp.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                // Test input part1
                if (password.getText().toString().equals("") || repassword.getText().toString().equals("")
                        || email.getText().toString().equals("") || name.getText().toString().equals("")) {
                    notification.setText("Chưa nhập đầy đủ thông tin");

                    ViewGroup.LayoutParams params = notification.getLayoutParams();
                    params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    notification.setLayoutParams(params);
                    return;
                }

                // Test input part2
                if (!password.getText().toString().equals(repassword.getText().toString())) {
                    notification.setText("Mật khẩu nhập lại không chính xác");

                    ViewGroup.LayoutParams params = notification.getLayoutParams();
                    params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    notification.setLayoutParams(params);

                    return;
                }

                // Send request require create account
                API.api.requireCreate(email.getText().toString(), password.getText().toString(), name.getText().toString()).enqueue(new Callback<ResponseMess>() {
                    @Override
                    public void onResponse(Call<ResponseMess> call, Response<ResponseMess> response) {
                        ResponseMess mess = response.body();
                        if (mess.getSuccess()) {
                            Intent intent = new Intent(SignUpActivity.this, OTPActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("backType", "signUp");

                            intent.putExtra("data", bundle);
                            startActivity(intent);
                            return;
                        }
                        notification.setText(mess.getMess());

                        ViewGroup.LayoutParams params = notification.getLayoutParams();
                        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                        notification.setLayoutParams(params);
                    }

                    @Override
                    public void onFailure(Call<ResponseMess> call, Throwable t) {

                    }
                });
            }
        });
    }
}