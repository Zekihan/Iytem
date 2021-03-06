package com.wambly.iytem;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ContactsCustomAdapter extends RecyclerView.Adapter<ContactsCustomAdapter.MyViewHolder> implements Filterable {

    private List<Contact> contacts;
    private List<Contact> mDisplayedValues;

    void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    ContactsCustomAdapter(List<Contact> contacts) {
        this.contacts = contacts;
        this.mDisplayedValues = contacts;
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

        Contact contact = mDisplayedValues.get(i);
        myViewHolder.name.setText(contact.getName());
        myViewHolder.department.setText(contact.getDepartment());
        myViewHolder.title.setText(contact.getTitle());

    }


    @Override
    public int getItemCount() {
        return mDisplayedValues.size();
    }

    List<Contact> getmDisplayedValues() {
        return mDisplayedValues;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                List<Contact> filteredList = new ArrayList<>();
                if (charString.isEmpty()) {
                    filteredList = contacts;
                }else {
                    final String prefixString = charSequence.toString().toLowerCase();

                    for (Contact row : contacts) {
                        if(checkName(row, prefixString) || checkDep(row, prefixString)){
                            filteredList.add(row);
                        }
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mDisplayedValues = (ArrayList<Contact>) filterResults.values;
                ContactsCustomAdapter.this.notifyDataSetChanged();
                // refresh the list with filtered data
                notifyDataSetChanged();
            }
        };
    }

    private boolean checkDep(Contact contact, String chars){
        String department = contact.getDepartment().toLowerCase();

        if(chars.length() > 2){
            return department.startsWith(chars);
        }
        return false;
    }

    private boolean checkName(Contact contact, String chars){
        String name = contact.getName().toLowerCase();
        String[] nameArr = name.split(" ");
        if(nameArr.length == 1){
            return name.startsWith(chars);
        }else if(nameArr.length == 2){
            return name.startsWith(chars) || nameArr[1].startsWith(chars);
        }else if (nameArr.length > 2){
            return name.startsWith(chars) || nameArr[1].startsWith(chars)
                    || nameArr[2].startsWith(chars) ;
        }
        return false;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        final TextView name;
        final TextView department;
        final TextView title;

        MyViewHolder(@NonNull View view) {
            super(view);
            name = view.findViewById(R.id.name);
            department = view.findViewById(R.id.department);
            title = view.findViewById(R.id.title);
        }
    }
}
