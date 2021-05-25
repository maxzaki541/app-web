package com.example.projectqueue;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class MyAdapter extends ArrayAdapter<history> {

    Context context;
    List<history> arrayListHistory;


    public MyAdapter(@NonNull Context context, List<history> arrayListHistory) {
        super(context, R.layout.custom_list_item,arrayListHistory);

        this.context = context;
        this.arrayListHistory = arrayListHistory;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_list_item,null,true);

        TextView tvID = view.findViewById(R.id.txt_idhis);
        TextView tvdate = view.findViewById(R.id.txt_datehis);
        TextView tvtime = view.findViewById(R.id.txt_timehis);
        TextView tvtype = view.findViewById(R.id.txt_typehis);

        tvID.setText(arrayListHistory.get(position).getQueue_id());
        tvdate.setText(arrayListHistory.get(position).getQueue_date());
        tvtime.setText(arrayListHistory.get(position).getQueue_time());
        tvtype.setText(arrayListHistory.get(position).getType_name());

        return view;
    }
}
