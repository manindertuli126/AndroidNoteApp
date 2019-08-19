package com.example.notesapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private customCategoryConfig categoryAdapter;
    private List<String> Categoryname = new ArrayList<>();
    private String alert_text = "";
    database notedb;
    private String m_Text = "";
    SearchView searchCategory;
    boolean defaultCategoryflag = true;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        categoryAdapter = new customCategoryConfig(Categoryname);
        getSupportActionBar().setTitle("Notes Category");
        notedb = new database(this);
        searchCategory = findViewById(R.id.categorysearch);

            Cursor verify = notedb.selectCategoryTable();
            if (verify.getCount() == 0) {
                notedb.insertCategoryTable("Home");
                notedb.insertCategoryTable("Work");
                notedb.insertCategoryTable("Holiday");
            }

        orignalCategory();

        searchCategory.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchCatkeyword(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(TextUtils.isEmpty(newText))
                {
                    orignalCategory();
                }
                else {
                    searchCatkeyword(newText);
                }
                return false;
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
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Add New Category");

                final EditText input = new EditText(this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        m_Text = input.getText().toString();
                        notedb.insertCategoryTable(m_Text);
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
            case R.id.notes:
                Intent mIntent = new Intent(MainActivity.this, showallnotes.class);
                startActivity(mIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void searchCatkeyword(String keyword) {
        Cursor notelistquery = notedb.searchCat(keyword);
        if (notelistquery.getCount() == 0) {
            Categoryname.clear();
        } else {
            Categoryname.clear();
            while (notelistquery.moveToNext()) {
                Categoryname.add(notelistquery.getString(1));
            }
        }
        categoryAdapter.notifyDataSetChanged();
    }

    private void orignalCategory() {
        recyclerView=findViewById(R.id.categoryid);
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
}
