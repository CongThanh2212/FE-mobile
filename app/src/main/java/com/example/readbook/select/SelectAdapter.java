package com.example.readbook.select;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.readbook.R;

import java.util.ArrayList;

public class SelectAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<Select> selects;

    public SelectAdapter(Context context, int layout, ArrayList<Select> selects) {
        this.context = context;
        this.layout = layout;
        this.selects = selects;
    }

    @Override
    public int getCount() {
        return selects.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(layout, null);

        ImageView img = view.findViewById(R.id.selectImg);
        TextView des = view.findViewById(R.id.selectDes);

        img.setImageResource(selects.get(i).getImg());
        des.setText(selects.get(i).getDescription());

        return view;
    }
}
