package com.example.notesapp;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class noteList extends AppCompatActivity {

    private ListView notelist;
    database notelistdb;
    String passcategoryid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);

        Intent intent = getIntent();
        passcategoryid = intent.getStringExtra("categoryidfromnewnote");

        //set title
        getSupportActionBar().setTitle("Notes");

        //create db and tables
        notelistdb = new database(this);

        Cursor resultset = notelistdb.selectNoteListTableWhere(passcategoryid);
        if (resultset.getCount() == 0) {
            Log.i("DB", "FAIL");
        } else {
            while (resultset.moveToNext()) {
                customnotelist.notetitleArrayList.add(resultset.getString(0));
                customnotelist.notedateArrayList.add(resultset.getString(1));
            }
        }

        //create category grid view
        customnotelist noteAdapter = new customnotelist(this);
        notelist = findViewById(R.id.note_list);
        notelist.setAdapter(noteAdapter);
        notelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent mIntent = new Intent(noteList.this, newNotes.class);
                mIntent.putExtra("passcategoryid", ""+(position+1));
                startActivity(mIntent);
            }
        });

        getSupportActionBar().setTitle("Notes List");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.note_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        switch(item.getItemId()){

            case android.R.id.home:
                Intent mIntent = new Intent(noteList.this, MainActivity.class);
                startActivity(mIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
