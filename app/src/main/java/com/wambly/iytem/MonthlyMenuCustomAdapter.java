package com.wambly.iytem;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class MonthlyMenuCustomAdapter extends RecyclerView.Adapter<MonthlyMenuCustomAdapter.MyViewHolder> {
    private final List<String> menus;
    private final Context context;

    public MonthlyMenuCustomAdapter(List<String> menus, Context context) {
        this.menus = menus;
        this.context = context;
        Log.e("Adapter",menus+"");
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
        Log.e("Adapter",i+"");
        myViewHolder.date.setText(getDate(i+1));
        myViewHolder.menu.setText(menu);
    }

    private String getDate(int day){
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
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
