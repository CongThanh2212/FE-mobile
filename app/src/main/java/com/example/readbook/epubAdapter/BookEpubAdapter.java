package com.example.readbook.epubAdapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.readbook.R;

import java.util.List;

import io.hamed.htepubreadr.entity.FontEntity;
import io.hamed.htepubreadr.ui.view.EpubView;
import io.hamed.htepubreadr.ui.view.OnHrefClickListener;
import io.hamed.htepubreadr.util.EpubUtil;

public class BookEpubAdapter extends RecyclerView.Adapter<BookEpubAdapter.MyViewHolder> {

    private List<String> data;
    private OnHrefClickListener onHrefClickListener;
    private String baseUrl;
    private FontEntity fontEntity;
    private int fontSize = -1;
    private String background = "#ffffff";

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public EpubView epubView;

        public MyViewHolder(View v) {
            super(v);
            epubView = v.findViewById(R.id.epub_view);
        }

        public void bind(String content) {
            if (fontSize != -1)
                epubView.setFontSize(fontSize);
            if (fontEntity != null)
                epubView.setFont(fontEntity);
            epubView.setBaseUrl(baseUrl);
            if (onHrefClickListener != null)
                epubView.setOnHrefClickListener(onHrefClickListener);
            epubView.setUp(content);
            epubView.setBackgroundColor(Color.parseColor(background));
        }
    }

    public BookEpubAdapter(List<String> data, String baseUrl) {
        this.data = data;
        this.baseUrl = baseUrl;
    }

    @Override
    public BookEpubAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String content = "Error";
        try {
            content = EpubUtil.getHtmlContent(data.get(position));
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.bind(content);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public OnHrefClickListener getOnHrefClickListener() {
        return onHrefClickListener;
    }

    public void setOnHrefClickListener(OnHrefClickListener onHrefClickListener) {
        this.onHrefClickListener = onHrefClickListener;
    }

    public FontEntity getFontEntity() {
        return fontEntity;
    }

    public void setFontEntity(FontEntity fontEntity) {
        this.fontEntity = fontEntity;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public void setBackground(String background) {this.background = background;}

    public String getBackground() {
        return this.background;
    }
}