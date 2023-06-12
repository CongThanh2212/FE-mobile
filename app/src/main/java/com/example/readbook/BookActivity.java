package com.example.readbook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.readbook.api.API;
import com.example.readbook.book.Book;
import com.example.readbook.book.CollectionAdapter;
import com.example.readbook.book.MainAdapter;
import com.example.readbook.response.ResponseMess;
import com.example.readbook.saveLogin.SaveLogin;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookActivity extends AppCompatActivity {

    private TextView header;
    private ImageView bookImage;
    private TextView bookName;
    private TextView bookAuthor;
    private TextView numberOfRead;
    private TextView description;
    private TextView type;
    private TextView author;
    private TextView language;
    private RecyclerView relatedBooks;
    private ImageView back;
    private Button read;
    private Button addCollection;

    private Context context = this;
    private SaveLogin saveLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        /*
            Intent receive from:
            + MainActivity about: int backType, Book book
            + HistoryActivity about: int backType, Book book
            + CollectionActivity about: int backType, Book book
            + CategoryBookActivity about: int backType, Book book, Category category
            + SearchActivity about: int backType, Book book, Category category, int searchBackType
            + ReadActivity about: int backType, Book book, Category category, int searchBackType
        */
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("data");
        Book book = (Book) bundle.getSerializable("book");

        mapping();
        showBook(book);
        onclickBack(bundle);
        onclickAddCollection(book);
        onclickRead(bundle, book);
    }

    private void mapping() {
        header = findViewById(R.id.header);
        bookImage = findViewById(R.id.bookImage);
        bookName = findViewById(R.id.bookName);
        bookAuthor = findViewById(R.id.bookAuthor);
        numberOfRead = findViewById(R.id.numberOfRead);
        description = findViewById(R.id.description);
        type = findViewById(R.id.type);
        author = findViewById(R.id.author);
        language = findViewById(R.id.language);
        relatedBooks = findViewById(R.id.related_books);
        back = findViewById(R.id.back);
        read = findViewById(R.id.read);
        addCollection = findViewById(R.id.addCollection);

        saveLogin = new SaveLogin(this);
    }

    private void showBook(Book book) {
        // Info book
        API.api.infoBook(book.getName()).enqueue(new Callback<Book>() {
            @Override
            public void onResponse(Call<Book> call, Response<Book> response) {
                Book book1 = response.body();
                header.setText(book1.getName());
                Glide.with(context).load(book1.getImg()).into(bookImage);
                bookName.setText(book1.getName());
                bookAuthor.setText(book1.getAuthor());
                numberOfRead.setText(String.valueOf(book1.getNumberOfRead()));
                description.setText(book1.getDescription());
                type.setText(book1.getType());
                author.setText(book1.getAuthor());
                language.setText(book1.getLanguage());

                setBook(book, book1);
                // Related book
                showRelatedBooks(book);
            }

            @Override
            public void onFailure(Call<Book> call, Throwable t) {

            }
        });

        // Collection yes/ no
        if (!saveLogin.getEmail().equals("")) {
            API.api.collectionYesNo(saveLogin.getEmail(), book.getName()).enqueue(new Callback<ResponseMess>() {
                @Override
                public void onResponse(Call<ResponseMess> call, Response<ResponseMess> response) {
                    ResponseMess mess = response.body();
                    if (mess.getSuccess()) addCollection.setText("XÓA KHỎI BST");
                    else addCollection.setText("THÊM VÀO BST");
                }

                @Override
                public void onFailure(Call<ResponseMess> call, Throwable t) {

                }
            });
        }
    }

    private void onclickAddCollection(Book book) {
        addCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // If chưa login -> direction to login IF
                if (saveLogin.getEmail().equals("")) {
                    Intent intent = new Intent(BookActivity.this, LoginActivity.class);
                    startActivity(intent);
                    return;
                }
                // Add or delete khỏi collection
                if (addCollection.getText().toString().equals("THÊM VÀO BST")) {
                    API.api.addCollection(saveLogin.getEmail(), book.getImg(), book.getName(), book.getAuthor()).enqueue(new Callback<ResponseMess>() {
                        @Override
                        public void onResponse(Call<ResponseMess> call, Response<ResponseMess> response) {
                            ResponseMess mess = response.body();
                            if (mess.getSuccess()) {
                                addCollection.setText("XÓA KHỎI BST");
                                Toast.makeText(context, "Thêm thành công", Toast.LENGTH_SHORT).show();
                            } else Toast.makeText(context, "Thêm thất bại", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<ResponseMess> call, Throwable t) {

                        }
                    });
                } else {
                    API.api.delCollection(saveLogin.getEmail(), book.getName()).enqueue(new Callback<ResponseMess>() {
                        @Override
                        public void onResponse(Call<ResponseMess> call, Response<ResponseMess> response) {
                            ResponseMess mess = response.body();
                            if (mess.getSuccess()) {
                                addCollection.setText("THÊM VÀO BST");
                                Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
                            } else Toast.makeText(context, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<ResponseMess> call, Throwable t) {

                        }
                    });
                }
            }
        });
    }

    private void showRelatedBooks(Book book) {
        API.api.relatedBook(book.getName(), book.getType()).enqueue(new Callback<ArrayList<Book>>() {
            @Override
            public void onResponse(Call<ArrayList<Book>> call, Response<ArrayList<Book>> response) {
                ArrayList<Book> books = response.body();
                MainAdapter adapter;

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                relatedBooks.setLayoutManager(linearLayoutManager);
                adapter = new MainAdapter(context, books, new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        book.setName(books.get(i).getName());
                        showBook(book);
                    }
                });
                relatedBooks.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<ArrayList<Book>> call, Throwable t) {

            }
        });
    }

    private void onclickBack(Bundle bundle) {
        int backType = bundle.getInt("backType");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (backType) {
                    case 0: {
                        Intent intent = new Intent(BookActivity.this, MainActivity.class);
                        startActivity(intent);
                        break;
                    }
                    case 1: {
                        Intent intent = new Intent(BookActivity.this, HistoryActivity.class);
                        startActivity(intent);
                        break;
                    }
                    case 4: {
                        Intent intent = new Intent(BookActivity.this, CategoryBookActivity.class);
                        Bundle bundleSend = new Bundle();
                        bundleSend.putSerializable("category", bundle.getSerializable("category"));

                        intent.putExtra("data", bundleSend);
                        startActivity(intent);
                        break;
                    }
                    case 5: {
                        Intent intent = new Intent(BookActivity.this, SearchActivity.class);
                        Bundle bundleSend = new Bundle();
                        bundleSend.putInt("backType", bundle.getInt("searchBackType"));
                        bundleSend.putSerializable("category", bundle.getSerializable("category"));

                        intent.putExtra("data", bundleSend);
                        startActivity(intent);
                        break;
                    }
                    default: {
                        Intent intent = new Intent(BookActivity.this, CollectionActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });
    }

    private void onclickRead(Bundle bundle, Book book) {
        read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!saveLogin.getEmail().equals("")) {
                    API.api.addRecently(saveLogin.getEmail(), book.getImg(), book.getName(), book.getAuthor()).enqueue(new Callback<ResponseMess>() {
                        @Override
                        public void onResponse(Call<ResponseMess> call, Response<ResponseMess> response) {

                        }

                        @Override
                        public void onFailure(Call<ResponseMess> call, Throwable t) {

                        }
                    });
                } else {
                    API.api.addNumberOfRead(book.getName()).enqueue(new Callback<ArrayList<Book>>() {
                        @Override
                        public void onResponse(Call<ArrayList<Book>> call, Response<ArrayList<Book>> response) {

                        }

                        @Override
                        public void onFailure(Call<ArrayList<Book>> call, Throwable t) {

                        }
                    });
                }
                Intent intent = new Intent(BookActivity.this, ReadActivity.class);
                Bundle bundleSend = new Bundle();
                bundleSend.putSerializable("category", bundle.getSerializable("category"));
                bundleSend.putInt("backType", bundle.getInt("backType"));
                bundleSend.putInt("searchBackType", bundle.getInt("searchBackType"));
                bundleSend.putSerializable("book", book);

                intent.putExtra("data", bundle);
                startActivity(intent);
            }
        });
    }

    private void setBook(Book oldBook, Book newBook) {
        oldBook.setImg(newBook.getImg());
        oldBook.setName(newBook.getName());
        oldBook.setAuthor(newBook.getAuthor());
        oldBook.setDescription(newBook.getDescription());
        oldBook.setType(newBook.getType());
        oldBook.setNumberOfRead(newBook.getNumberOfRead());
        oldBook.setLanguage(newBook.getLanguage());
        oldBook.setPdf(newBook.getPdf());
    }
}