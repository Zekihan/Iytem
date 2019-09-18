package com.wambly.iytem;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class ShortcutsCustomAdapter extends RecyclerView.Adapter<ShortcutsCustomAdapter.MyViewHolder>  {
    private final ArrayList<Shortcut> shortcuts;

    ShortcutsCustomAdapter(ArrayList<Shortcut> shortcuts) {
        this.shortcuts = shortcuts;
    }

    @NonNull
    @Override
    public ShortcutsCustomAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shortcut_row_item, parent, false);
        return new ShortcutsCustomAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ShortcutsCustomAdapter.MyViewHolder myViewHolder, int i) {
        String name = shortcuts.get(i).getName();
        int img = shortcuts.get(i).getImg();
        myViewHolder.name.setText(name);
        myViewHolder.img.setImageResource(img);
    }

    @Override
    public int getItemCount() {
        return shortcuts.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        final TextView name;
        final ImageView img;
        MyViewHolder(@NonNull View view) {
            super(view);
            name = view.findViewById(R.id.name);
            img = view.findViewById(R.id.image);
        }
    }

    ArrayList<Shortcut> getShortcuts() {
        return shortcuts;
    }

}
