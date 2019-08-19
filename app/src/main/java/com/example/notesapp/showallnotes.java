package com.example.notesapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;

public class showallnotes extends AppCompatActivity {

    private ListView notelist;
    database notelistdb;
    String notelistcategoryid = "";
    Button sorttitle;
    Button sortdate;
    SearchView searchnote;
    int DescAsc = 1;
    int DescAscDate = 1;
    customallnotes noteAdapter = new customallnotes(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showallnotes);

        Intent intent = getIntent();
        notelistcategoryid = intent.getStringExtra("passcategoryid");
        System.out.println(notelistcategoryid);

        sorttitle = (Button)findViewById(R.id.alltitle);
        sortdate = (Button)findViewById(R.id.alldate);
        searchnote = (SearchView)findViewById(R.id.allsearch_List);

        getSupportActionBar().setTitle("Notes");
        notelistdb = new database(this);
        orignalList();

        //create category grid view
        notelist = findViewById(R.id.allnote_list);
        notelist.setAdapter(noteAdapter);
        notelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent mIntent = new Intent(showallnotes.this, updateNote.class);
                mIntent.putExtra("passnoteid", "");
                mIntent.putExtra("passnoteid", ""+(position+1));
                mIntent.putExtra("passcategoryid", "");
                mIntent.putExtra("passcategoryid", notelistcategoryid);
                startActivity(mIntent);
            }
        });

        getSupportActionBar().setTitle("All Notes");
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sorttitle.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Cursor notesortquery = null;
                switch(DescAsc){
                    case 1:
                        notesortquery = notelistdb.NoteListTableSortDesc();
                        DescAsc =2;
                        break;
                    case 2:
                        notesortquery = notelistdb.NoteListTableSortAsc();
                        DescAsc =1;
                        break;
                }
                if (notesortquery.getCount() == 0) {
                    Log.i("notesortListDB", "FAIL");
                } else {
                    customnotelist.notetitleArrayList.clear();
                    customnotelist.notedateArrayList.clear();
                    customnotelist.notelocationArrayList.clear();
                    while (notesortquery.moveToNext()) {
                        customnotelist.notetitleArrayList.add(notesortquery.getString(0));
                        customnotelist.notedateArrayList.add(notesortquery.getString(1));
                        customnotelist.notelocationArrayList.add(notesortquery.getString(2));
                    }
                }
                noteAdapter.notifyDataSetChanged();
            }
        });

        sortdate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Cursor notesortdatequery = null;
                switch(DescAscDate){
                    case 1:
                        notesortdatequery = notelistdb.NoteListTableSortdateDesc(notelistcategoryid);
                        DescAscDate =2;
                        break;
                    case 2:
                        notesortdatequery = notelistdb.NoteListTableSortdateAsc(notelistcategoryid);
                        DescAscDate =1;
                        break;
                }
                if (notesortdatequery.getCount() == 0) {
                    Log.i("noteListDB", "FAIL");
                } else {
                    customnotelist.notetitleArrayList.clear();
                    customnotelist.notedateArrayList.clear();
                    customnotelist.notelocationArrayList.clear();
                    while (notesortdatequery.moveToNext()) {
                        customnotelist.notetitleArrayList.add(notesortdatequery.getString(0));
                        customnotelist.notedateArrayList.add(notesortdatequery.getString(1));
                        customnotelist.notelocationArrayList.add(notesortdatequery.getString(2));
                    }
                }
                noteAdapter.notifyDataSetChanged();
            }
        });

        searchnote.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchContact(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(TextUtils.isEmpty(newText))
                {
                    orignalList();
                }
                else {
                    searchContact(newText);
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.show_all, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        switch(item.getItemId()){

            case android.R.id.home:
                finish();
                break;

            case R.id.delete_allnote_list:
                deleteAllNoteAlert();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void deleteAllNoteAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("DELETED !!");
        builder.setMessage("Delete 'ALL NOTES' ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        notelistdb.deleteAllNoteListTable(notelistcategoryid);
                        finish();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void searchContact(String keyword) {
        Cursor notelistquery = notelistdb.searchlist(keyword);
        if (notelistquery.getCount() == 0) {
            customallnotes.notetitleArrayList.clear();
            customallnotes.notedateArrayList.clear();
            customallnotes.notelocationArrayList.clear();
        } else {
            customallnotes.notetitleArrayList.clear();
            customallnotes.notedateArrayList.clear();
            customallnotes.notelocationArrayList.clear();
            while (notelistquery.moveToNext()) {
                customallnotes.notetitleArrayList.add(notelistquery.getString(2));
                customallnotes.notedateArrayList.add(notelistquery.getString(4));
                customallnotes.notelocationArrayList.add(notelistquery.getString(5));
            }
        }
        noteAdapter.notifyDataSetChanged();
    }

    private void orignalList(){
        Cursor notelistquery = notelistdb.selectnotesTable();
        if (notelistquery.getCount() == 0) {
            Log.i("noteListDB", "FAIL");
        } else {
            customallnotes.notetitleArrayList.clear();
            customallnotes.notedateArrayList.clear();
            customallnotes.notelocationArrayList.clear();
            while (notelistquery.moveToNext()) {
                customallnotes.notetitleArrayList.add(notelistquery.getString(2));
                customallnotes.notedateArrayList.add(notelistquery.getString(4));
                customallnotes.notelocationArrayList.add(notelistquery.getString(5));
            }
        }
        noteAdapter.notifyDataSetChanged();
    }
}

