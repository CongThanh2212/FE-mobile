package com.example.readbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.readbook.saveLogin.SaveLogin;

public class ChangeInfoNoPassActivity extends AppCompatActivity {

    private ImageView back;
    private EditText email;
    private EditText name;

    private SaveLogin saveLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_info_no_pass);

        mapping();
        getData();
        onclickBack();
    }

    private void mapping() {
        back = findViewById(R.id.back);
        email = findViewById(R.id.email);
        name = findViewById(R.id.name);

        saveLogin = new SaveLogin(this);
    }

    private void getData() {
        email.setText(saveLogin.getEmail());
        name.setText(saveLogin.getName());
    }

    private void onclickBack() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChangeInfoNoPassActivity.this, AccountActivity.class);
                startActivity(intent);
            }
        });
    }
}