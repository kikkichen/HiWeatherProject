package com.chen.hiweatherproject.SelfAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chen.hiweatherproject.R;

import java.util.List;

public class myListAdapter extends ArrayAdapter<String> {

    private Context mContext;
    private int resourceId;
    private List<String> itemList;

    public myListAdapter(@NonNull Context context, int textViewResourceId, @NonNull List<String> objects) {
        super(context, textViewResourceId, objects);
        mContext = context;
        itemList = objects;
        resourceId = textViewResourceId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String city = getItem(position);
        View view = LayoutInflater.from(mContext).inflate(resourceId, parent, false);

        TextView tv = view.findViewById(R.id.tv_anwser_cityname);
        tv.setText(city);
        return view;
    }

    public void setItemList(List<String> cities) {
        itemList.clear();
        for (String city: cities) {
            itemList.add(city);
        }
    }
}