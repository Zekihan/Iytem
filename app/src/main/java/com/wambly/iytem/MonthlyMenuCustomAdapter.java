package com.wambly.iytem;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class MonthlyMenuCustomAdapter extends RecyclerView.Adapter<MonthlyMenuCustomAdapter.MyViewHolder> {
    private List<String> menus;

    public MonthlyMenuCustomAdapter(List<String> menus) {
        this.menus = menus;
    }

    @NonNull
    @Override
    public MonthlyMenuCustomAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.monthly_menu_row_item, parent, false);

        return new MonthlyMenuCustomAdapter.MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull MonthlyMenuCustomAdapter.MyViewHolder myViewHolder, int i) {
        String menu = menus.get(i);
        myViewHolder.date.setText(getDate(i+1));
        myViewHolder.menu.setText(menu);
    }

    private String getDate(int day){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("d MMMM EEEE");
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH,day);
        return format.format(c.getTime());
    }

    @Override
    public int getItemCount() {
        return menus.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView date;
        TextView menu;
        MyViewHolder(@NonNull View view) {
            super(view);
            menu = view.findViewById(R.id.menu);
            date = view.findViewById(R.id.date);

        }
    }
}
