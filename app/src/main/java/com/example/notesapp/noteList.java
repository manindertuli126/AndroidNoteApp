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
import android.widget.Toast;

import java.util.List;

public class noteList extends AppCompatActivity {

    private ListView notelist;
    database notelistdb;
    String notelistcategoryid = "";
    Button sorttitle;
    Button sortdate;
    SearchView searchnote;
    int DescAsc = 1;
    int DescAscDate = 1;
    customnotelist noteAdapter = new customnotelist(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);

        Intent intent = getIntent();
        notelistcategoryid = intent.getStringExtra("passcategoryid");
        System.out.println(notelistcategoryid);

        sorttitle = (Button)findViewById(R.id.title);
        sortdate = (Button)findViewById(R.id.date);
//        searchnote = (SearchView)findViewById(R.id.search_note);

        //set title
        getSupportActionBar().setTitle("Notes");

        //create db and tables
        notelistdb = new database(this);
        Cursor notelistquery = notelistdb.selectNoteListTableWhere(notelistcategoryid);
        if (notelistquery.getCount() == 0) {
            Log.i("cat id", notelistcategoryid);
            Log.i("noteListDB", "FAIL");
        } else {
            customnotelist.notetitleArrayList.clear();
            customnotelist.notedateArrayList.clear();
            customnotelist.notelocationArrayList.clear();
            while (notelistquery.moveToNext()) {
                customnotelist.notetitleArrayList.add(notelistquery.getString(0));
                customnotelist.notedateArrayList.add(notelistquery.getString(1));
                customnotelist.notelocationArrayList.add(notelistquery.getString(2));
            }
        }

        //create category grid view
        notelist = findViewById(R.id.note_list);
        notelist.setAdapter(noteAdapter);
        notelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent mIntent = new Intent(noteList.this, updateNote.class);
                mIntent.putExtra("passnoteid", "");
                mIntent.putExtra("passnoteid", ""+(position+1));
                mIntent.putExtra("passcategoryid", "");
                mIntent.putExtra("passcategoryid", notelistcategoryid);
                startActivity(mIntent);
            }
        });

        getSupportActionBar().setTitle("Notes List");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sorttitle.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Cursor notesortquery = null;
                switch(DescAsc){
                    case 1:
                        notesortquery = notelistdb.NoteListTableSortDesc(notelistcategoryid);
                        DescAsc =2;
                        break;
                    case 2:
                        notesortquery = notelistdb.NoteListTableSortAsc(notelistcategoryid);
                        DescAsc =1;
                        break;
                }
                if (notesortquery.getCount() == 0) {
                    Log.i("noteListDB", "FAIL");
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
//        searchnote.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                searchContact(query);
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                if(TextUtils.isEmpty(newText))
//                {
//                    //contacts.clear();
//                    getdata();
//                    // Toast.makeText(getApplicationContext(),"Hello Javatpoint",Toast.LENGTH_SHORT).show();
//                }
//                else {
//                    searchContact(newText);
//                }
//                return false;
//            }
//        });
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
                Intent mainIntent = new Intent(noteList.this, MainActivity.class);
                startActivity(mainIntent);
                break;

            case R.id.add_new_note:
                Intent newnoteIntent = new Intent(noteList.this, newNotes.class);
                newnoteIntent.putExtra("passcategoryid", "");
                newnoteIntent.putExtra("passcategoryid", notelistcategoryid);
                startActivity(newnoteIntent);
                break;

            case R.id.delete_note_list:
                deleteCatNoteAlert();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void deleteCatNoteAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("DELETED !!");
        builder.setMessage("Delete 'CATEGORY' or 'ALL NOTES' ?")
                .setCancelable(false)
                .setPositiveButton("Notes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        notelistdb.deleteAllNoteListTable(notelistcategoryid);
                        //navigate to note list
                        Intent deletenoteIntent = new Intent(noteList.this, MainActivity.class);
                        deletenoteIntent.putExtra("passcategoryid", "");
                        startActivity(deletenoteIntent);
                    }
                }).setNegativeButton("Category", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                notelistdb.deleteAllNoteListTable(notelistcategoryid);
                notelistdb.deleteFromCategoryTable(notelistcategoryid);
                //navigate to note list
                Intent deleteCategoryIntent = new Intent(noteList.this, MainActivity.class);
                deleteCategoryIntent.putExtra("passcategoryid", "");
                startActivity(deleteCategoryIntent);
            }
        }).setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

//    private void searchContact(String keyword) {
//        List<noteList> contacts = notelistdb.search(keyword);
//        if (contacts != null) {
//            noteAdapter = new customnotelist(this);
//            notelist = findViewById(R.id.note_list);
//            notelist.setAdapter(noteAdapter);
//            notelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    Intent mIntent = new Intent(noteList.this, updateNote.class);
//                    mIntent.putExtra("passnoteid", "");
//                    mIntent.putExtra("passnoteid", ""+(position+1));
//                    mIntent.putExtra("passcategoryid", "");
//                    mIntent.putExtra("passcategoryid", notelistcategoryid);
//                    startActivity(mIntent);
//                }
//            });
//            noteAdapter.notifyDataSetChanged();
//        }else
//        {
//            Toast.makeText(getApplicationContext(),"No Notes Found",Toast.LENGTH_SHORT).show();
//        }
//    }
}
