package com.example.notesapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class newNotes extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_notes);

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
                        Intent mIntent = new Intent(newNotes.this, MainActivity.class);
                        //Set value to pass on next activity
                        //mIntent.putExtra("name", "Pritesh Patel");
                        startActivity(mIntent);
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
