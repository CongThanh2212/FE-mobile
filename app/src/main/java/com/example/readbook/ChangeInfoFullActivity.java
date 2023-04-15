package com.example.readbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.readbook.accoount.Account;
import com.example.readbook.api.API;
import com.example.readbook.saveLogin.SaveLogin;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangeInfoFullActivity extends AppCompatActivity {

    private ImageView back;
    private EditText email;
    private EditText name;
    private EditText password;
    private EditText repassword;
    private EditText change;
    private TextView notification;

    private SaveLogin saveLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_info_full);

        mapping();
        getData();
        onclickChange();
        onclickBack();
    }

    private void mapping() {
        back = findViewById(R.id.back);
        email = findViewById(R.id.email);
        name = findViewById(R.id.name);
        password = findViewById(R.id.password);
        repassword = findViewById(R.id.repassword);
        change = findViewById(R.id.change);
        notification = findViewById(R.id.notification);

        saveLogin = new SaveLogin(this);
    }

    private void getData() {
        email.setText(saveLogin.getEmail());
        name.setText(saveLogin.getName());
    }

    private void onclickChange() {
        change.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                // Test input part1
                if (name.getText().toString().equals("") || password.getText().toString().equals("")
                        || repassword.getText().toString().equals("")) {
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

                // Request change password
                API.api.changeInfo(saveLogin.getEmail(), password.getText().toString(), name.getText().toString()).enqueue(new Callback<Account>() {
                    @Override
                    public void onResponse(Call<Account> call, Response<Account> response) {
                        Account account = response.body();

                        saveLogin.clear();
                        saveLogin.save(account.getEmail(), account.getName());

                        Intent intent = new Intent(ChangeInfoFullActivity.this, AccountActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(Call<Account> call, Throwable t) {

                    }
                });
            }
        });
    }

    private void onclickBack() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChangeInfoFullActivity.this, AccountActivity.class);
                startActivity(intent);
            }
        });
    }
}