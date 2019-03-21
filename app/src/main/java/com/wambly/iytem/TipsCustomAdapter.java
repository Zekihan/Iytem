package com.wambly.iytem;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.TextView;

import java.util.List;

public class TipsCustomAdapter extends RecyclerView.Adapter<TipsCustomAdapter.MyViewHolder> {
    private final List<Tip> tips;

    public TipsCustomAdapter(List<Tip> tips) {
        this.tips = tips;
    }

    @NonNull
    @Override
    public TipsCustomAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.announcements_row_item, parent, false);

        return new TipsCustomAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TipsCustomAdapter.MyViewHolder myViewHolder, int i) {
        Tip tip = tips.get(getItemCount()-1-i);
        myViewHolder.title.setText(tip.getTitle());
        myViewHolder.date.setText(tip.getDate());
        myViewHolder.description.setText(tip.getDescription());
    }

    @Override
    public int getItemCount() {
        return tips.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView description;
        TextView date;
        MyViewHolder(@NonNull View view) {
            super(view);
            title = view.findViewById(R.id.title);
            description = view.findViewById(R.id.description);
            date = view.findViewById(R.id.date);

        }
    }
}
