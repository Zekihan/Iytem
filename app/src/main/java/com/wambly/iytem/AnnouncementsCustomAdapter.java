package com.wambly.iytem;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wambly.iytem.R;

import java.util.List;

public class AnnouncementsCustomAdapter extends RecyclerView.Adapter<AnnouncementsCustomAdapter.MyViewHolder> {

    private final List<Announcement> announcements;

    public AnnouncementsCustomAdapter(List<Announcement> announcements) {
        this.announcements = announcements;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.announcements_row_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        Announcement announcement = announcements.get(i);
        myViewHolder.title.setText(announcement.getTitle());
        myViewHolder.date.setText(announcement.getDate());
        myViewHolder.description.setText(announcement.getDescription());
    }

    @Override
    public int getItemCount() {
        return announcements.size();
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
