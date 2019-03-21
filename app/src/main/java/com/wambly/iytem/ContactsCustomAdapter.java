package com.wambly.iytem;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ContactsCustomAdapter extends RecyclerView.Adapter<ContactsCustomAdapter.MyViewHolder> {

    private final List<Contact> contacts;

    public ContactsCustomAdapter(List<Contact> contacts) {
        this.contacts = contacts;
    }

    @NonNull
    @Override
    public ContactsCustomAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_row_item, parent, false);

        return new ContactsCustomAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsCustomAdapter.MyViewHolder myViewHolder, int i) {
        Contact contact = contacts.get(i);
        myViewHolder.name.setText(contact.getName());
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        MyViewHolder(@NonNull View view) {
            super(view);
            name = view.findViewById(R.id.name);
        }
    }
}
