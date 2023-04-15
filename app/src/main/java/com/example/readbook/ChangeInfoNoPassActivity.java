package com.example.readbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class ChangeInfoNoPassActivity extends AppCompatActivity {

    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_info_no_pass);

        mapping();
        onclickBack();
    }

    private void mapping() {
        back = findViewById(R.id.back);
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