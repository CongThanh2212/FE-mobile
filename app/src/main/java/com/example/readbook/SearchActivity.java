package com.example.readbook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.example.readbook.api.API;
import com.example.readbook.book.Book;
import com.example.readbook.book.SearchAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {

    private SearchView search;
    private RecyclerView listSearch;
    private TextView close;

    private Context context = this;
    private ArrayList<Book> books;
    private SearchAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        /*
            Intent receive from:
            + MainActivity about: int backType
            + HistoryActivity about: int backType
            + CollectionActivity about: int backType
            + CategoryBookActivity about: int backType, Category category
            + BookActivity about: int backType, Category category
        */
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("data");

        mapping();
        searchBar();
        showAllBook(bundle);
        onclickClose(bundle);
    }

    private void mapping() {
        search = findViewById(R.id.search);
        listSearch = findViewById(R.id.list_search);
        close = findViewById(R.id.close);
    }

    private void searchBar() {
        search.clearFocus();
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });
    }

    private void filter(String newText) {
        ArrayList<Book> listFilter = new ArrayList<>();
        for (Book book : books) {
            if (book.getName().toLowerCase().contains(newText.toLowerCase())) listFilter.add(book);
        }

        adapter.setFilterList(listFilter);
    }

    private void showAllBook(Bundle bundle) {
        API.api.allBook().enqueue(new Callback<ArrayList<Book>>() {
            @Override
            public void onResponse(Call<ArrayList<Book>> call, Response<ArrayList<Book>> response) {
                books = response.body();

                adapter = new SearchAdapter(books, new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(SearchActivity.this, BookActivity.class);
                        Bundle bundleSend = new Bundle();
                        bundleSend.putInt("backType", 5);
                        bundleSend.putInt("searchBackType", bundle.getInt("backType"));
                        bundleSend.putSerializable("category", bundle.getSerializable("category"));
                        bundleSend.putSerializable("book", books.get(i));

                        intent.putExtra("data", bundleSend);
                        startActivity(intent);
                    }
                });
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                listSearch.setLayoutManager(linearLayoutManager);
                listSearch.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<ArrayList<Book>> call, Throwable t) {

            }
        });
    }

    private void onclickClose(Bundle bundle) {
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (bundle.getInt("backType")) {
                    case 0: {
                        Intent intent = new Intent(SearchActivity.this, MainActivity.class);
                        startActivity(intent);
                        break;
                    }
                    case 1: {
                        Intent intent = new Intent(SearchActivity.this, HistoryActivity.class);
                        startActivity(intent);
                        break;
                    }
                    case 4: {
                        Intent intent = new Intent(SearchActivity.this, CategoryBookActivity.class);
                        Bundle bundleSend = new Bundle();
                        bundleSend.putSerializable("category", bundle.getSerializable("category"));

                        intent.putExtra("data", bundleSend);
                        startActivity(intent);
                        break;
                    }
                    default: {
                        Intent intent = new Intent(SearchActivity.this, CollectionActivity.class);
                        startActivity(intent);
                        break;
                    }
                }
            }
        });
    }
}