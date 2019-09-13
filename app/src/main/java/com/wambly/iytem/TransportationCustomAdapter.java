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
        String dir = busServices.get(i).getDirectionTileStr();
        myViewHolder.service.setText(service);
        myViewHolder.direction.setText(dir);
    }

    @Override
    public int getItemCount() {
        return busServices.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        final TextView service;
        final TextView direction;
        MyViewHolder(@NonNull View view) {
            super(view);
            service = view.findViewById(R.id.service);
            direction = view.findViewById(R.id.direction);
        }
    }

    ArrayList<BusService> getBusService() {
        return busServices;
    }

}
