package com.example.notesapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class database extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "NoteApp.db";
    public static final String TABLE_NAME1 = "Category_tbl";
    public static final String TABLE_NAME2 = "NotesList_tbl";
    public static final String TABLE_NAME3 = "NotesImage_tbl";

    public static final String CAT_COL = "categoryname";

    public database(Context context) {
        super(context, DATABASE_NAME, null, 1);
//        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+TABLE_NAME1+" (id INTEGER PRIMARY KEY AUTOINCREMENT,categoryname TEXT)");
        db.execSQL("create table "+TABLE_NAME2+" (id INTEGER PRIMARY KEY AUTOINCREMENT,categoryid INTEGER,notetitle TEXT, notedetail TEXT, location TEXT)");
        db.execSQL("create table "+TABLE_NAME3+" (id INTEGER PRIMARY KEY AUTOINCREMENT,categoryid INTEGER, imagepath TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL(SQL_DELETE_ENTRIES);
//        onCreate(db);
    }

    public boolean insertCategoryTable(String categoryname){
        SQLiteDatabase db = this.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(CAT_COL, categoryname);

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(TABLE_NAME1, null, values);
        if (newRowId == -1){return false;}else{return true;}
    }

    public Cursor selectCategoryTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res  = db.rawQuery("select * from "+TABLE_NAME1,null);
        return res;
    }

    public Integer deleteFromCategoryTable(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME1, "id = ?", new String [] {id});
    }

}
