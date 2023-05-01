package com.example.readbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.readbook.accoount.Account;
import com.example.readbook.book.Book;
import com.example.readbook.saveLogin.SaveLogin;
import com.example.readbook.select.Select;
import com.example.readbook.select.SelectAdapter;

import java.util.ArrayList;

public class AccountActivity extends AppCompatActivity {

    private TextView icon;
    private TextView name;
    private RelativeLayout changeInfo;
    private RelativeLayout feedBack;
    private RelativeLayout logOut;
    private GridView select;

    private SaveLogin saveLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        mapping();
        getData();
        showSelect();
        onclickChangeInfo();
        onclickLogOut();
        onclickFeedBack();
    }

    private void mapping() {
        icon = findViewById(R.id.icon);
        name = findViewById(R.id.name);
        changeInfo = findViewById(R.id.changeInfoAccount);
        feedBack = findViewById(R.id.feedback);
        logOut = findViewById(R.id.logOut);
        select = findViewById(R.id.select);

        saveLogin = new SaveLogin(this);
    }

    private void getData() {
        if (saveLogin.getEmail().equals("")) return;
        // Create notation
        String[] elementName = saveLogin.getName().split(" ");
        String notation = "";
        int count = 0;
        for (int i = 0; i < elementName.length; i++) {
            notation += elementName[i].charAt(0);
            count++;
            if (count == 2) break;
        }

        icon.setText(notation);
        name.setText(saveLogin.getName());
    }

    private void onclickChangeInfo() {
        changeInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AccountActivity.this, ChangeInfoFullActivity.class);
                if (saveLogin.getType().equals("facebook")) {
                    intent = new Intent(AccountActivity.this, ChangeInfoNoPassActivity.class);
                }
                startActivity(intent);
            }
        });
    }

    private void onclickLogOut() {
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveLogin.clear();
                Intent intent = new Intent(AccountActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void onclickFeedBack() {
        feedBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    public void showSelect() {
        ArrayList<Select> selects = new ArrayList<>();
        SelectAdapter adapter;

        selects.add(new Select(R.drawable.ic_baseline_home_24, "Trang chủ"));
        selects.add(new Select(R.drawable.ic_baseline_history_24, "Đọc gần đây"));
        selects.add(new Select(R.drawable.ic_baseline_local_library_24, "Bộ sưu tập"));
        selects.add(new Select(R.drawable.ic_baseline_account_circle_24, "Tài khoản"));
        adapter = new SelectAdapter(this, R.layout.select_bar, selects);
        select.setAdapter(adapter);

        select.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0: {
                        Intent intent = new Intent(AccountActivity.this, MainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                        break;
                    }
                    case 1: {
                        Intent intent = new Intent(AccountActivity.this, HistoryActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                        break;
                    }
                    case 2: {
                        Intent intent = new Intent(AccountActivity.this, CollectionActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                        break;
                    }
                }
            }
        });
    }
}