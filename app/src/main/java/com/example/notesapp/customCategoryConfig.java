package com.example.notesapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class customCategoryConfig extends BaseAdapter {
    private Context mContext;
    public static ArrayList<String> categoryArrayList = new ArrayList<String>();

    // Constructor
    public customCategoryConfig(Context c) {
        mContext = c;
    }

    @Override
    public int getCount() {
        return categoryArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return categoryArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).
                    inflate(R.layout.custom_category,parent, false);
        }
        TextView name = convertView.findViewById(R.id.categoryName);

        name.setText(categoryArrayList.get(position).toString());
        return convertView;
    }
}
