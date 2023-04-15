package com.example.readbook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.readbook.api.API;
import com.example.readbook.book.Book;
import com.example.readbook.book.CollectionAdapter;
import com.example.readbook.book.HistoryAdapter;
import com.example.readbook.saveLogin.SaveLogin;
import com.example.readbook.select.Select;
import com.example.readbook.select.SelectAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CollectionActivity extends AppCompatActivity {

    private RecyclerView collectionBooks;
    private GridView select;
    private ImageView search;

    private Context context = this;
    private SaveLogin saveLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);

        mapping();
        showListBook();
        showSelect();
        selectSearch();
    }

    private void mapping() {
        collectionBooks = findViewById(R.id.list_item_collection);
        select = findViewById(R.id.select);
        search = findViewById(R.id.search);

        saveLogin = new SaveLogin(this);
    }

    public void showListBook() {
        API.api.collection(saveLogin.getEmail()).enqueue(new Callback<ArrayList<Book>>() {
            @Override
            public void onResponse(Call<ArrayList<Book>> call, Response<ArrayList<Book>> response) {
                ArrayList<Book> books = response.body();
                CollectionAdapter adapter;
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                collectionBooks.setLayoutManager(linearLayoutManager);
                adapter = new CollectionAdapter(context, books, new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(CollectionActivity.this, BookActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("backType", 2);
                        bundle.putSerializable("book", books.get(i));

                        intent.putExtra("data", bundle);
                        startActivity(intent);
                    }
                });
                collectionBooks.setAdapter(adapter);
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
                        Intent intent = new Intent(CollectionActivity.this, MainActivity.class);
                        startActivity(intent);
                        break;
                    }
                    case 1: {
                        Intent intent = new Intent(CollectionActivity.this, HistoryActivity.class);
                        startActivity(intent);
                        break;
                    }
                    case 3: {
                        Intent intent = new Intent(CollectionActivity.this, AccountActivity.class);
                        startActivity(intent);
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
                Intent intent = new Intent(CollectionActivity.this, SearchActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("backType", 2);

                intent.putExtra("data", bundle);
                startActivity(intent);
            }
        });
    }
}