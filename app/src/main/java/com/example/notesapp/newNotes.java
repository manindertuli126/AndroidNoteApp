package com.example.notesapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class newNotes extends AppCompatActivity {

    database newnotedb;
    String passcategoryid = "";
    private EditText usernotetitle;
    private EditText usernotedetail;
    private EditText usernotecategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_notes);

        Intent intent = getIntent();
        passcategoryid = intent.getStringExtra("passcategoryid");

        usernotecategory = findViewById(R.id.enter_category);
        usernotetitle = findViewById(R.id.enter_title);
        usernotedetail = findViewById(R.id.enter_detail);

        newnotedb = new database(this);
        if(passcategoryid != ""){
            Cursor catresult = newnotedb.selectCategoryTableWhere(passcategoryid);
            if (catresult.getCount() == 0) {
                Log.i("**DB**", "FAIL");
            } else {
                while (catresult.moveToNext()) {
                    usernotecategory.setText(catresult.getString(0));
                }
            }
        }

        getSupportActionBar().setTitle("New Note");

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new_note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        switch(item.getItemId()){
            case R.id.save_new_note:

//                newnotedb.insertNewNoteTable(passcategoryid)
                successAlert();
            break;

            case android.R.id.home:
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void successAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("SUCCESS !!");
        builder.setMessage("Note saved successfully ..")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
