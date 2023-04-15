package com.example.readbook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.readbook.api.API;
import com.example.readbook.book.Book;
import com.example.readbook.book.Category;
import com.example.readbook.book.CollectionAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryBookActivity extends AppCompatActivity {

    private TextView header;
    private ImageView search;
    private RecyclerView collectionBooks;
    private ImageView back;

    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_book);

        /*
            Intent receive from:
            + MainActivity about: Category category
            + SearchActivity about: Category category
            + BookActivity about: Category category
        */
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("data");

        mapping();
        showCategory(bundle);
        showListBook(bundle);
        onclickBack();
    }

    private void mapping() {
        header = findViewById(R.id.categoryHeader);
        search = findViewById(R.id.search);
        collectionBooks = findViewById(R.id.list_books);
        back = findViewById(R.id.back);
    }

    private void showCategory(Bundle bundle) {
        Category category0 = (Category) bundle.getSerializable("category");

        header.setText(category0.getName());
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CategoryBookActivity.this, SearchActivity.class);
                Bundle bundleSend = new Bundle();
                bundleSend.putInt("backType", 4);
                bundleSend.putSerializable("category", category0);

                intent.putExtra("data", bundleSend);
                startActivity(intent);
            }
        });
    }

    public void showListBook(Bundle bundle) {
        Category category1 = (Category) bundle.getSerializable("category");
        API.api.listBookByCategory(category1.getLink()).enqueue(new Callback<ArrayList<Book>>() {
            @Override
            public void onResponse(Call<ArrayList<Book>> call, Response<ArrayList<Book>> response) {
                ArrayList<Book> books = response.body();
                CollectionAdapter adapter;
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                collectionBooks.setLayoutManager(linearLayoutManager);
                adapter = new CollectionAdapter(context, books, new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(CategoryBookActivity.this, BookActivity.class);
                        Bundle bundleSend = new Bundle();
                        bundleSend.putInt("backType", 4);
                        bundleSend.putSerializable("category", category1);
                        bundleSend.putSerializable("book", books.get(i));

                        intent.putExtra("data", bundleSend);
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

    private void onclickBack() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CategoryBookActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}