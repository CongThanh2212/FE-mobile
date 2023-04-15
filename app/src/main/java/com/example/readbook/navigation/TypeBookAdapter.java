package com.example.readbook.navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.readbook.R;
import com.example.readbook.book.Category;

import java.util.ArrayList;

public class TypeBookAdapter extends RecyclerView.Adapter<TypeBookAdapter.ViewHolder> {

    private ArrayList<Category> typeBooks;
    private AdapterView.OnItemClickListener onItemClickListener;

    public TypeBookAdapter(ArrayList<Category> typeBooks, AdapterView.OnItemClickListener onItemClickListener) {
        this.typeBooks = typeBooks;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.navigation_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(null, view, viewHolder.getAdapterPosition(), 1);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.item.setText(typeBooks.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return typeBooks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView item;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.item);
        }
    }
}
