package com.example.notesapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class customnotelist extends BaseAdapter {

    private Context mContext;
    public static ArrayList<String> notetitleArrayList = new ArrayList<String>();
    public static ArrayList<String> notedetailArrayList = new ArrayList<String>();
    public static ArrayList<String> notelocationArrayList = new ArrayList<String>();

    public customnotelist(Context nc){
        mContext = nc;
    }

    @Override
    public int getCount() {
        return notetitleArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return notetitleArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).
                    inflate(R.layout.custom_note_list,parent, false);
        }
        TextView notetitle = convertView.findViewById(R.id.note_title);
        TextView notedate = convertView.findViewById(R.id.note_date);
        TextView notelocation = convertView.findViewById(R.id.note_location);

        notetitle.setText(notetitleArrayList.get(position).toString());
        notedate.setText(notedetailArrayList.get(position).toString());
        notelocation.setText(notelocationArrayList.get(position).toString());
        return convertView;
    }
}
