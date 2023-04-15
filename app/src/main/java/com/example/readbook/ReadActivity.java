package com.example.readbook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.readbook.book.Book;
import com.example.readbook.bottomSheet.OnChangeFontFamily;
import com.example.readbook.bottomSheet.OnChangeFontSize;
import com.example.readbook.bottomSheet.ToolsBottomSheet;
import com.example.readbook.epubAdapter.BookEpubAdapter;
import com.example.readbook.view.MyRecycler;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import io.hamed.htepubreadr.app.util.StringUtils;
import io.hamed.htepubreadr.component.EpubReaderComponent;
import io.hamed.htepubreadr.entity.BookEntity;
import io.hamed.htepubreadr.entity.FontEntity;
import io.hamed.htepubreadr.ui.view.OnHrefClickListener;

public class ReadActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = "@MY_APP";
    private MyRecycler rvBook;
    private TextView name;
    private ImageView back;
    private ProgressBar load;

    private List<FontEntity> listFont = new ArrayList<>();
    private EpubReaderComponent epubReader;
    private BookEpubAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        /*
            Intent receive from:
            + BookActivity about: int backType, Category category, int searchBackType, Book book
         */
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("data");

        mapping();
        showName(bundle);
        onclickBack(bundle);

        downloadFile(((Book) bundle.getSerializable("book")).getPdf());

        listFont.add(new FontEntity("https://hamedtaherpour.github.io/sample-assets/font/Acme.css", "Acme"));
        listFont.add(new FontEntity("https://hamedtaherpour.github.io/sample-assets/font/IndieFlower.css", "IndieFlower"));
        listFont.add(new FontEntity("https://hamedtaherpour.github.io/sample-assets/font/SansitaSwashed.css", "SansitaSwashed"));
    }

    @Override
    public void onClick(View v) {
        openToolsMenu();
    }

    private void mapping() {
        rvBook = findViewById(R.id.rv_book);
        name = findViewById(R.id.name);
        back = findViewById(R.id.back);
        findViewById(R.id.btn_setting).setOnClickListener(this);
        load = findViewById(R.id.load);
    }

    private void onBookReady(String filePath) {
        try {
            epubReader = new EpubReaderComponent(filePath);
            BookEntity bookEntity = epubReader.make(this);
            setUpBookAdapter(bookEntity.getPagePathList());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void openToolsMenu() {
        ToolsBottomSheet sheet = new ToolsBottomSheet();
        sheet.setFontSize(adapter.getFontSize());
        sheet.setAllFontFamily(listFont);
        sheet.setOnChangeFontFamily(new OnChangeFontFamily() {
            @Override
            public void onChange(int position) {
                adapter.setFontEntity(listFont.get(position));
                adapter.notifyDataSetChanged();
            }
        });
        sheet.setOnChangeFontSize(new OnChangeFontSize() {
            @Override
            public void onChangeSize(int size) {
                adapter.setFontSize(size);
                adapter.notifyDataSetChanged();
            }
        });
        sheet.show(getSupportFragmentManager(), sheet.getTag());
    }

    private void setUpBookAdapter(List<String> list) {
        adapter = new BookEpubAdapter(list, epubReader.getAbsolutePath());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        rvBook.setLayoutManager(layoutManager);
        rvBook.setAdapter(adapter);
        adapter.setFontSize(30);
        adapter.setOnHrefClickListener(new OnHrefClickListener() {
            @Override
            public void onClick(String href) {
                gotoPageByHref(href);
            }
        });
        new LinearSnapHelper().attachToRecyclerView(rvBook);
    }

    public void gotoPageByHref(String href) {
        int position = epubReader.getPagePositionByHref(href);
        if (position != EpubReaderComponent.PAGE_NOT_FOUND)
            rvBook.scrollToPosition(position);
    }

    private void downloadFile(String url) {
        String fileName = StringUtils.getName(url);
        Ion.with(getApplicationContext())
                .load(url)
                .write(new File(getFileDirectoryDownloads(), fileName))
                .setCallback(new FutureCallback<File>() {
                    @Override
                    public void onCompleted(Exception e, File file) {
                        if (e == null)
                            onBookReady(file.getPath());
                        else {
                            Toast.makeText(getApplicationContext(), "Lỗi! Thử lại", Toast.LENGTH_SHORT).show();
                            Log.i(TAG, "Lỗi! Thử lại --> " + e.toString());
                        }
                        load.setVisibility(View.GONE);
                    }
                });
    }

    private File getFileDirectoryDownloads() {
        final File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File myDirectory = new File(directory, "book");
        if (!myDirectory.exists())
            myDirectory.mkdir();
        return myDirectory;
    }

    private Bitmap fileToBitmap(File file) {
        if (file.exists()) {
            return BitmapFactory.decodeFile(file.getAbsolutePath());
        }
        return null;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void showName(Bundle bundle) {
        Book book = (Book) bundle.getSerializable("book");
        name.setText(book.getName());
    }

    private void onclickBack(Bundle bundle) {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReadActivity.this, BookActivity.class);
                Bundle bundleSend = new Bundle();
                bundleSend.putSerializable("category", bundle.getSerializable("category"));
                bundleSend.putInt("backType", bundle.getInt("backType"));
                bundleSend.putInt("searchBackType", bundle.getInt("searchBackType"));
                bundleSend.putSerializable("book", bundle.getSerializable("book"));

                intent.putExtra("data", bundleSend);
                startActivity(intent);
            }
        });
    }
}