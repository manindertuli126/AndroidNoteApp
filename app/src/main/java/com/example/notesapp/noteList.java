package com.example.notesapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class noteList extends AppCompatActivity {

    private ListView notelist;
    database notelistdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);

        //set title
        getSupportActionBar().setTitle("Notes");

        //create db and tables
        notelistdb = new database(this);

        //create category grid view
        customnotelist noteAdapter = new customnotelist(this);
        notelist = findViewById(R.id.note_list);
        notelist.setAdapter(noteAdapter);
        notelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent mIntent = new Intent(noteList.this, newNotes.class);
//                sharedPreferences = getSharedPreferences("passcategoryid", position);
                mIntent.putExtra("passcategoryid", ""+(position+1));
                startActivity(mIntent);
            }
        });
    }
}
