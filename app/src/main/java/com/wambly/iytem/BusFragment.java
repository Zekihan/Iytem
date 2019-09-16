package com.wambly.iytem;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
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
import java.util.Timer;
import java.util.TimerTask;


public class BusFragment extends Fragment {

    private static final String ARG_CONTENT1 = "content0";
    private static final String ARG_CONTENT2 = "content1";
    
    private ArrayList<String> content0;
    private ArrayList<String> content1;
    private boolean today = false;
    private OnFragmentInteractionListener mListener;
    private ArrayAdapter<String> adapter;

    public BusFragment() { }

    static BusFragment newInstance(ArrayList<String> param1, ArrayList<String> param2, boolean today) {
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
        final BusFragment context = this;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                FragmentManager fm = getFragmentManager();
                if(fm != null){
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.detach(context).attach(context).commit();
                }
            }
        }, 100);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_blank, container, false);
        if (getArguments() != null) {
            content0 = getArguments().getStringArrayList(ARG_CONTENT1);
            content1 = getArguments().getStringArrayList(ARG_CONTENT2);
            today = getArguments().getBoolean("today");
        }
        final ListView listView = rootView.findViewById(R.id.timeList);
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(rootView.getContext());
        final ArrayList<String> list = new ArrayList<>();
        adapter = new ArrayAdapter<>(rootView.getContext(), android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);
        if(today){
            if(prefs.getBoolean("direction",false)){
                list.addAll(filterByTime(content1,getTime()));
            }else{
                list.addAll(filterByTime(content0,getTime()));
            }
            if(list.isEmpty()){
                list.add("Sefer Yok");
            }
            adapter.notifyDataSetChanged();
            prefs.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                    list.clear();
                    if(prefs.getBoolean("direction",false)){
                        list.addAll(filterByTime(content1,getTime()));
                    }else{
                        list.addAll(filterByTime(content0,getTime()));
                    }
                    if(list.isEmpty()){
                        list.add("Sefer Yok");
                    }
                    adapter.notifyDataSetChanged();
                }
            });
        }else{
            if(prefs.getBoolean("direction",false)){
                list.addAll(content1);
            }else{
                list.addAll(content0);
            }
            if(list.isEmpty()){
                list.add("Sefer Yok");
            }
            adapter.notifyDataSetChanged();
            prefs.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                    list.clear();
                    if(prefs.getBoolean("direction",false)){
                        list.addAll(content1);
                    }else{
                        list.addAll(content0);
                    }
                    if(list.isEmpty()){
                        list.add("Sefer Yok");
                    }
                    adapter.notifyDataSetChanged();
                }
            });
        }
        return rootView;
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
        return result;
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
