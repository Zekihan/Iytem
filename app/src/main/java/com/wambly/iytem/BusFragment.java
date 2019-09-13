package com.wambly.iytem;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class BusFragment extends Fragment {

    private static final String ARG_CONTENT1 = "content1";
    private static final String ARG_CONTENT2 = "content2";
    
    private ArrayList<String> content1;
    private ArrayList<String> content2;
    private boolean today = false;
    private OnFragmentInteractionListener mListener;
    ArrayAdapter<String> adapter;

    public BusFragment() { }

    public static BusFragment newInstance(ArrayList<String> param1, ArrayList<String> param2, boolean today) {
        BusFragment fragment = new BusFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(ARG_CONTENT1, param1);
        args.putStringArrayList(ARG_CONTENT2, param2);
        args.putBoolean("today",today);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if (Build.VERSION.SDK_INT >= 26) {
            ft.setReorderingAllowed(false);
        }
        ft.detach(this).attach(this).commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_blank, container, false);
        if (getArguments() != null) {
            content1 = getArguments().getStringArrayList(ARG_CONTENT1);
            content2 = getArguments().getStringArrayList(ARG_CONTENT2);
            today = getArguments().getBoolean("today");
        }

        if(today){
            final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(rootView.getContext());
            final ListView listView = rootView.findViewById(R.id.timeList);
            ArrayList<String> list = new ArrayList<>();
            if(prefs.getBoolean("direction",false)){
                list.addAll(filterByTime(content2,getTime()));
                adapter = new ArrayAdapter<>(rootView.getContext(), android.R.layout.simple_list_item_1, list);
                listView.setAdapter(adapter);
            }else{
                list.addAll(filterByTime(content1,getTime()));
                adapter = new ArrayAdapter<>(rootView.getContext(), android.R.layout.simple_list_item_1, list);
                listView.setAdapter(adapter);
            }
            prefs.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                    ArrayList<String> list = new ArrayList<>();
                    if(prefs.getBoolean("direction",false)){
                        list.addAll(filterByTime(content2,getTime()));
                        adapter = new ArrayAdapter<>(rootView.getContext(), android.R.layout.simple_list_item_1, list);
                        listView.setAdapter(adapter);
                    }else{
                        list.addAll(filterByTime(content1,getTime()));
                        adapter = new ArrayAdapter<>(rootView.getContext(), android.R.layout.simple_list_item_1, list);
                        listView.setAdapter(adapter);
                    }
                }
            });

        }else{
            final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(rootView.getContext());
            final ListView listView = rootView.findViewById(R.id.timeList);
            adapter = new ArrayAdapter<>(rootView.getContext(), android.R.layout.simple_list_item_1, getSchedule());
            listView.setAdapter(adapter);
            ArrayList<String> list = new ArrayList<>();
            if(prefs.getBoolean("direction",false)){
                list.addAll(content2);
            }else{
                list.addAll(content1);
            }
            if(list.isEmpty()){
                list = new ArrayList<>();
                list.add("Loading");
            }
            adapter = new ArrayAdapter<>(rootView.getContext(), android.R.layout.simple_list_item_1, list);
            listView.setAdapter(adapter);
            prefs.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                    ArrayList<String> list = new ArrayList<>();
                    if(prefs.getBoolean("direction",false)){
                        list.addAll(content2);
                    }else{
                        list.addAll(content1);
                    }
                    adapter = new ArrayAdapter<>(rootView.getContext(), android.R.layout.simple_list_item_1, list);
                    listView.setAdapter(adapter);
                }
            });
        }
        return rootView;
    }
    private ArrayList<String> getSchedule(){
        ArrayList<String> list = new ArrayList<>();
        String direction2 = "";
        list.add(direction2);
        list.addAll(content1);
        list.add(" ");
        String direction1 = "";
        list.add(direction1);
        list.addAll(content2);
        return list;
    }
    private String getTime(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(calendar.getTimeInMillis()-900000);
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minuteOfDay = calendar.get(Calendar.MINUTE);
        return hourOfDay+":"+minuteOfDay;
    }

    private List<String> filterByTime(List<String> list, String time){
        List<String> result = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            Log.e("Blank", list.size()+"");
            String s = list.get(i);
            if(s.contains(":")&&time.contains(":")){
                if(s.equals("00:00")){
                    s = "24:00";
                }
                String[] ss = s.split(":");
                String[] time2 = time.split(":");
                if(Integer.parseInt(ss[0])>Integer.parseInt(time2[0])){
                    if(s.equals("24:00")){
                        s = "00:00";
                    }
                    result.add(s);

                }else if(Integer.parseInt(ss[0]) == Integer.parseInt(time2[0])){
                    if(Integer.parseInt(ss[1])>Integer.parseInt(time2[1])){
                        result.add(s);
                    }
                }
            }
        }
        if(result.isEmpty()){
            result.add("Sefer Yok - No Service");
        }
        return result;
    }
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            if (Build.VERSION.SDK_INT >= 26) {
                ft.setReorderingAllowed(false);
            }
            ft.detach(this).attach(this).commit();
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
