package com.example.notesapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class customCategoryConfig extends RecyclerView.Adapter<customCategoryConfig.Myviewholder> {
    private List<String> Categoryname;
    private Context mcontext;
    database notedb1;
    int postion;

    // Constructor
    public customCategoryConfig(List<String> MainCategory) {
        Categoryname = MainCategory;
    }

    @NonNull
    @Override
    public customCategoryConfig.Myviewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.custom_category, viewGroup, false);
        mcontext = viewGroup.getContext();
        return new Myviewholder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull customCategoryConfig.Myviewholder myviewholder, final int i) {
        myviewholder.cat.setText(Categoryname.get(i));
        notedb1 = new database(mcontext);
        myviewholder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create db and tables
                Cursor getDefaultcategory = notedb1.selectNoteListTableWhere(""+(i+1));
                Log.i("trueOrfalse",""+getDefaultcategory.getCount());
                if (getDefaultcategory.getCount() == 0) {
                    Intent mIntent = new Intent(mcontext, newNotes.class);
                    mIntent.putExtra("passcategoryid", "");
                    mIntent.putExtra("passcategoryid", ""+(i+1));
                    mcontext.startActivity(mIntent);
                } else {
                    Intent mIntent = new Intent(mcontext, noteList.class);
                    mIntent.putExtra("passcategoryid", "");
                    mIntent.putExtra("passcategoryid", ""+(i+1));
                    mcontext.startActivity(mIntent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return Categoryname.size();
    }

    public class Myviewholder extends RecyclerView.ViewHolder
    {
        public TextView cat;

        public Myviewholder(  View itemView) {
            super(itemView);
            cat=(TextView) itemView.findViewById(R.id.categoryName);
        }
    }
}
