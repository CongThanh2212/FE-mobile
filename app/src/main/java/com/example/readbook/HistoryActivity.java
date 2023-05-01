package com.example.readbook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.readbook.api.API;
import com.example.readbook.book.Book;
import com.example.readbook.book.HistoryAdapter;
import com.example.readbook.saveLogin.SaveLogin;
import com.example.readbook.select.Select;
import com.example.readbook.select.SelectAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView historyBooks;
    private GridView select;
    private ImageView search;

    private Context context = this;
    private SaveLogin saveLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        mapping();
        showListBook();
        showSelect();
        selectSearch();
    }

    private void mapping() {
        historyBooks = findViewById(R.id.list_item_history);
        select = findViewById(R.id.select);
        search = findViewById(R.id.search);

        saveLogin = new SaveLogin(this);
    }

    public void showListBook() {
        API.api.recently(saveLogin.getEmail()).enqueue(new Callback<ArrayList<Book>>() {
            @Override
            public void onResponse(Call<ArrayList<Book>> call, Response<ArrayList<Book>> response) {
                ArrayList<Book> books = response.body();
                HistoryAdapter adapter;
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                historyBooks.setLayoutManager(linearLayoutManager);
                adapter = new HistoryAdapter(context, books, new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(HistoryActivity.this, BookActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("backType", 1);
                        bundle.putSerializable("book", books.get(i));

                        intent.putExtra("data", bundle);
                        startActivity(intent);
                    }
                });
                historyBooks.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<ArrayList<Book>> call, Throwable t) {

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
                        Intent intent = new Intent(HistoryActivity.this, MainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                        break;
                    }
                    case 2: {
                        Intent intent = new Intent(HistoryActivity.this, CollectionActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        break;
                    }
                    case 3: {
                        Intent intent = new Intent(HistoryActivity.this, AccountActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        break;
                    }
                }
            }
        });
    }

    private void selectSearch() {
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HistoryActivity.this, SearchActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("backType", 1);

                intent.putExtra("data", bundle);
                startActivity(intent);
            }
        });
    }
}