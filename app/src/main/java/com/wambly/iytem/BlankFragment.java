package com.wambly.iytem;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BlankFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BlankFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BlankFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_CONTENT1 = "content1";
    private static final String ARG_CONTENT2 = "content2";

    // TODO: Rename and change types of parameters
    private ArrayList<String> content1;
    private ArrayList<String> content2;

    public boolean direction[] = {false};
    public boolean today = false;


    private OnFragmentInteractionListener mListener;

    public BlankFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BlankFragment newInstance(ArrayList<String> param1,ArrayList<String> param2,boolean today) {
        BlankFragment fragment = new BlankFragment();
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


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_blank, container, false);
        if (getArguments() != null) {
            content1 = getArguments().getStringArrayList(ARG_CONTENT1);
            content2 = getArguments().getStringArrayList(ARG_CONTENT2);
            today = getArguments().getBoolean("today");
        }
        if(today){
            final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(rootView.getContext());
            final ListView listView = rootView.findViewById(R.id.timeList);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(rootView.getContext(),android.R.layout.simple_list_item_1, getSchedule());
            listView.setAdapter(adapter);
            ArrayList<String> list = new ArrayList<String>();
            if(prefs.getBoolean("direction",false)){
                list.add("İZMİR --> İYTE");
                list.addAll(filterByTime(content2,getTime()));
                adapter = new ArrayAdapter<String>(rootView.getContext(),android.R.layout.simple_list_item_1, list);
                listView.setAdapter(adapter);
            }else{
                list.add("İYTE --> İZMİR");
                list.addAll(filterByTime(content1,getTime()));
                adapter = new ArrayAdapter<String>(rootView.getContext(),android.R.layout.simple_list_item_1, list);
                listView.setAdapter(adapter);
            }
            prefs.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                    ArrayList<String> list = new ArrayList<String>();
                    ArrayAdapter<String> adapter;
                    if(prefs.getBoolean("direction",false)){
                        list.add("İZMİR --> İYTE");
                        list.addAll(filterByTime(content2,getTime()));
                        adapter = new ArrayAdapter<String>(rootView.getContext(),android.R.layout.simple_list_item_1, list);
                        listView.setAdapter(adapter);
                    }else{
                        list.add("İYTE --> İZMİR");
                        list.addAll(filterByTime(content1,getTime()));
                        adapter = new ArrayAdapter<String>(rootView.getContext(),android.R.layout.simple_list_item_1, list);
                        listView.setAdapter(adapter);
                    }
                }
            });
        }else{
            final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(rootView.getContext());
            final ListView listView = rootView.findViewById(R.id.timeList);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(rootView.getContext(),android.R.layout.simple_list_item_1, getSchedule());
            listView.setAdapter(adapter);
            ArrayList<String> list = new ArrayList<String>();
            if(prefs.getBoolean("direction",false)){
                list.add("İZMİR --> İYTE");
                list.addAll(content2);
                adapter = new ArrayAdapter<String>(rootView.getContext(),android.R.layout.simple_list_item_1, list);
                listView.setAdapter(adapter);
            }else{
                list.add("İYTE --> İZMİR");
                list.addAll(content1);
                adapter = new ArrayAdapter<String>(rootView.getContext(),android.R.layout.simple_list_item_1, list);
                listView.setAdapter(adapter);
            }
            prefs.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                    ArrayList<String> list = new ArrayList<String>();
                    ArrayAdapter<String> adapter;
                    if(prefs.getBoolean("direction",false)){
                        list.add("İZMİR --> İYTE");
                        list.addAll(content2);
                        adapter = new ArrayAdapter<String>(rootView.getContext(),android.R.layout.simple_list_item_1, list);
                        listView.setAdapter(adapter);
                    }else{
                        list.add("İYTE --> İZMİR");
                        list.addAll(content1);
                        adapter = new ArrayAdapter<String>(rootView.getContext(),android.R.layout.simple_list_item_1, list);
                        listView.setAdapter(adapter);
                    }
                }
            });
        }




        return rootView;
    }
    private ArrayList<String> getSchedule(){
        ArrayList<String> list = new ArrayList<String>();
        list.add("İYTE --> İZMİR");
        list.addAll(content1);
        list.add(" ");
        list.add("İZMİR --> İYTE");
        list.addAll(content2);
        return  list;
    }
    private String getTime(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(calendar.getTimeInMillis()-900000);
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minuteOfDay = calendar.get(Calendar.MINUTE);
        return hourOfDay+":"+minuteOfDay;
    }

    private List<String> filterByTime(List<String> list,String time){
        List<String> result = new ArrayList<>();

        for (String s :list) {
            if(s.contains(":")&&time.contains(":")){
                String ss[] = s.split(":");
                String[] time2 = time.split(":");
                if(Integer.parseInt(ss[0])>Integer.parseInt(time2[0])){
                    result.add(s);
                }else if(Integer.parseInt(ss[0]) == Integer.parseInt(time2[0])){
                    if(Integer.parseInt(ss[1])>Integer.parseInt(time2[1])){
                        result.add(s);
                    }
                }
            }else{
                list.add(s);
            }

        }

        return result;
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
