package com.example.readbook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.readbook.api.API;
import com.example.readbook.book.Book;
import com.example.readbook.book.Category;
import com.example.readbook.book.MainAdapter;
import com.example.readbook.book.SearchAdapter;
import com.example.readbook.navigation.TypeBookAdapter;
import com.example.readbook.saveLogin.SaveLogin;
import com.example.readbook.select.Select;
import com.example.readbook.select.SelectAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView listItem;
    private RecyclerView newBooks;
    private GridView select;
    private TextView popular;
    private TextView newBook;
    private ImageView search;
    private RecyclerView nav_item;
    private DrawerLayout drawerLayout;
    private ImageView menu;
    private TextView icon;
    private TextView name;

    private Context context = this;
    private SaveLogin saveLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapping();
        getData();
        showNavigation();
        showListBookHot();
        showListBookNew();
        showSelect();
        selectShowAll();
        selectSearch();
    }

    private void mapping() {
        listItem = findViewById(R.id.list_item_popular);
        newBooks = findViewById(R.id.list_item_new);
        select = findViewById(R.id.select);
        popular = findViewById(R.id.show_popular);
        newBook = findViewById(R.id.show_new);
        search = findViewById(R.id.search);
        nav_item = findViewById(R.id.nav_item);
        drawerLayout = findViewById(R.id.drawer_layout);
        menu = findViewById(R.id.menu);
        icon = findViewById(R.id.icon);
        name = findViewById(R.id.name);

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

    private void showListBookHot() {
        // Request
        API.api.limitHot().enqueue(new Callback<ArrayList<Book>>() {
            @Override
            public void onResponse(Call<ArrayList<Book>> call, Response<ArrayList<Book>> response) {
                ArrayList<Book> limitHot = response.body();
                MainAdapter adapter;
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                listItem.setLayoutManager(linearLayoutManager);

                if (limitHot == null) return;
                adapter = new MainAdapter(context, limitHot, new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(MainActivity.this, BookActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("backType", 0);
                        bundle.putSerializable("book", limitHot.get(i));

                        intent.putExtra("data", bundle);
                        startActivity(intent);
                    }
                });
                listItem.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<ArrayList<Book>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Mất kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showListBookNew() {
        API.api.limitNew().enqueue(new Callback<ArrayList<Book>>() {
            @Override
            public void onResponse(Call<ArrayList<Book>> call, Response<ArrayList<Book>> response) {
                ArrayList<Book> limitNew = response.body();
                MainAdapter adapter;
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                newBooks.setLayoutManager(linearLayoutManager);

                if (limitNew == null) return;

                adapter = new MainAdapter(context, limitNew, new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(MainActivity.this, BookActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("backType", 0);
                        bundle.putSerializable("book", limitNew.get(i));

                        intent.putExtra("data", bundle);
                        startActivity(intent);
                    }
                });
                newBooks.setAdapter(adapter);
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
                if (i != 0 && saveLogin.getEmail().equals("")) {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    return;
                }
                switch (i) {
                    case 1: {
                        Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                        startActivity(intent);
                        break;
                    }
                    case 2: {
                        Intent intent = new Intent(MainActivity.this, CollectionActivity.class);
                        startActivity(intent);
                        break;
                    }
                    case 3: {
                        Intent intent = new Intent(MainActivity.this, AccountActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });
    }

    private void selectShowAll() {
        popular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Category category = new Category("Sách đọc nhiều", "hot");

                Intent intent = new Intent(MainActivity.this, CategoryBookActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("category", category);

                intent.putExtra("data", bundle);
                startActivity(intent);
            }
        });

        newBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Category category = new Category("Sách mới nhất", "new");

                Intent intent = new Intent(MainActivity.this, CategoryBookActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("category", category);

                intent.putExtra("data", bundle);
                startActivity(intent);
            }
        });
    }

    private void selectSearch() {
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("backType", 0);

                intent.putExtra("data", bundle);
                startActivity(intent);
            }
        });
    }

    private void showNavigation() {
        API.api.listCategory().enqueue(new Callback<ArrayList<Category>>() {
            @Override
            public void onResponse(Call<ArrayList<Category>> call, Response<ArrayList<Category>> response) {
                ArrayList<Category> items = response.body();

                TypeBookAdapter adapter = new TypeBookAdapter(items, new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(MainActivity.this, CategoryBookActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("category", items.get(i));

                        intent.putExtra("data", bundle);
                        startActivity(intent);
                    }
                });
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                nav_item.setLayoutManager(linearLayoutManager);
                nav_item.setAdapter(adapter);

                menu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        drawerLayout.openDrawer(GravityCompat.START);
                    }
                });
            }

            @Override
            public void onFailure(Call<ArrayList<Category>> call, Throwable t) {

            }
        });
    }
}

