package com.wambly.iytem;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TransportationCustomAdapter extends RecyclerView.Adapter<TransportationCustomAdapter.MyViewHolder>  {
    private final ArrayList<BusService> busServices;

    TransportationCustomAdapter(ArrayList<BusService> busServices) {
        this.busServices = busServices;
    }

    @NonNull
    @Override
    public TransportationCustomAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.transportation_row_item, parent, false);
        return new TransportationCustomAdapter.MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull TransportationCustomAdapter.MyViewHolder myViewHolder, int i) {
        String service = busServices.get(i).getNameStr();
        String way0 = busServices.get(i).getWay0();
        String way1 = busServices.get(i).getWay1();
        myViewHolder.service.setText(service);
        myViewHolder.way0.setText(way0);
        myViewHolder.way1.setText(way1);
    }

    @Override
    public int getItemCount() {
        return busServices.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        final TextView service;
        final TextView way0;
        final TextView way1;
        MyViewHolder(@NonNull View view) {
            super(view);
            service = view.findViewById(R.id.service);
            way0 = view.findViewById(R.id.way0);
            way1 = view.findViewById(R.id.way1);
        }
    }

    ArrayList<BusService> getBusService() {
        return busServices;
    }

}
