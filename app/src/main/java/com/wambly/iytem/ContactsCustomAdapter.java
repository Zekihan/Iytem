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
                if (charString.isEmpty()) {
                    mDisplayedValues = contacts;
                }else {
                    final String prefixString = charSequence.toString().toLowerCase();
                    List<Contact> filteredList = new ArrayList<>();
                    for (Contact row : contacts) {
                        if(checkName(row, prefixString)
                                || row.getDepartment().toLowerCase().startsWith(prefixString)){
                            filteredList.add(row);
                        }
                    }
                    mDisplayedValues = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = mDisplayedValues;
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

    private boolean checkName(Contact contact, String chars){
        String[] nameArr = contact.getName().split(" ");
        if(nameArr.length == 1){
            return nameArr[0].toLowerCase().startsWith(chars);
        }else if(nameArr.length == 2){
            return nameArr[0].toLowerCase().startsWith(chars) || nameArr[1].toLowerCase().startsWith(chars);
        }else if (nameArr.length > 2){
            return nameArr[0].toLowerCase().startsWith(chars) || nameArr[1].toLowerCase().startsWith(chars) || nameArr[2].toLowerCase().startsWith(chars) ;
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
