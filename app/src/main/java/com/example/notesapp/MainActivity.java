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
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private customCategoryConfig categoryAdapter;
    private List<String> Categoryname = new ArrayList<>();
    private String alert_text = "";
    database notedb;
    private String m_Text = "";
    boolean defaultCategoryflag = true;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set title
        getSupportActionBar().setTitle("Notes Category");

        //create db and tables
        notedb = new database(this);

        //get category from db
            Cursor verify = notedb.selectCategoryTable();
            if (verify.getCount() == 0) {
                notedb.insertCategoryTable("Home");
                notedb.insertCategoryTable("Work");
                notedb.insertCategoryTable("Holiday");
            }

        //recycle view
        recyclerView=findViewById(R.id.categoryid);
        categoryAdapter = new customCategoryConfig(Categoryname);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(categoryAdapter);
        Cursor resultset = notedb.selectCategoryTable();
        if (resultset.getCount() == 0) {
            Log.i("DB", "FAIL");
        } else {
            Categoryname.clear();
            while (resultset.moveToNext()) {
                Categoryname.add(resultset.getString(1));
            }
        }
        categoryAdapter.notifyDataSetChanged();
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
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Add New Category");

                // Set up the input
                final EditText input = new EditText(this);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        m_Text = input.getText().toString();
                        notedb.insertCategoryTable(m_Text);
//                        categoryAdapter.notifyDataSetChanged();
                        finish();
                        startActivity(getIntent());
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
                break;
            case R.id.delete_category:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
