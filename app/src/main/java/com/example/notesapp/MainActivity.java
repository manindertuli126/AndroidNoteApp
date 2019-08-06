package com.example.notesapp;

import android.app.ActionBar;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private GridView NoteCategory;
    private String alert_text = "";
    database notedb;
    private SharedPreferences sharedPreferences;
    boolean defaultCategoryflag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set title
        getSupportActionBar().setTitle("Notes Category");

        //check default category ??

        //create db and tables
        notedb = new database(this);

        //get category from db

//        if(defaultCategoryflag) {
//            // insert default Category
//            notedb.insertCategoryTable("Home");
//            notedb.insertCategoryTable("Work");
//            notedb.insertCategoryTable("Holiday");
//            defaultCategoryflag = false;
//        }
        Cursor resultset = notedb.selectCategoryTable();
        if (resultset.getCount() == 0) {
            Log.i("DB", "FAIL");
        } else {
            while (resultset.moveToNext()) {
                customCategoryConfig.categoryArrayList.add(resultset.getString(1));
            }
        }

        //create category grid view
        customCategoryConfig categoryAdapter = new customCategoryConfig(this);
        NoteCategory = findViewById(R.id.categoryList);
        NoteCategory.setAdapter(categoryAdapter);
        NoteCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent mIntent = new Intent(MainActivity.this, newNotes.class);
//                sharedPreferences = getSharedPreferences("passcategoryid", position);
                mIntent.putExtra("passcategoryid", ""+(position+1));
                startActivity(mIntent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        int id = item.getItemId();

        switch(id){
            case R.id.add_category:
                Intent mIntent = new Intent(MainActivity.this, newNotes.class);
                //Set value to pass on next activity
                //mIntent.putExtra("name", "Pritesh Patel");
                startActivity(mIntent);
                break;

            case R.id.delete_category:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
