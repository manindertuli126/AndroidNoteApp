package com.example.notesapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class database extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "NoteApp.db";
    public static final String TABLE_NAME1 = "Category_tbl";
    public static final String TABLE_NAME2 = "NotesList_tbl";
    public static final String TABLE_NAME3 = "NotesImage_tbl";

    public static final String CAT_COL = "categoryname";

    public static final String NOTE_COL0= "id";
    public static final String NOTE_COL1 = "categoryid";
    public static final String NOTE_COL2 = "notetitle";
    public static final String NOTE_COL3 = "notedetail";
    public static final String NOTE_COL4 = "notedate";
    public static final String NOTE_COL5 = "notelocation";

    public static final String NOTE_IMAGE_COL1 = "notetitle";
    public static final String NOTE_IMAGE_COL2 = "noteimage";

    public database(Context context) {
        super(context, DATABASE_NAME, null, 1);
//        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+TABLE_NAME1+" (id INTEGER PRIMARY KEY AUTOINCREMENT,categoryname TEXT)");
        db.execSQL("create table "+TABLE_NAME2+" (id INTEGER PRIMARY KEY AUTOINCREMENT,categoryid INTEGER,notetitle TEXT, notedetail TEXT, notedate TEXT, notelocation TEXT)");
        db.execSQL("create table "+TABLE_NAME3+" (notetitle TEXT, noteimage BLOB)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL(SQL_DELETE_ENTRIES);
//        onCreate(db);
    }

    public boolean insertImage(String imagelocation, String notetitle){
        SQLiteDatabase db = this.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(NOTE_IMAGE_COL1, notetitle);
        values.put(NOTE_IMAGE_COL2, imagelocation);

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(TABLE_NAME3, null, values);
        if (newRowId == -1){return false;}else{return true;}
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

    public boolean insertNewNoteTable(String categoryid,String title,String detail,String date,String location){
        SQLiteDatabase db = this.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(NOTE_COL1, categoryid);
        values.put(NOTE_COL2, title);
        values.put(NOTE_COL3, detail);
        values.put(NOTE_COL4, date);
        values.put(NOTE_COL5, location);

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(TABLE_NAME2, null, values);
        if (newRowId == -1){return false;}else{return true;}
    }

    public Cursor selectCategoryTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res  = db.rawQuery("select * from "+TABLE_NAME1,null);
        return res;
    }

    public Cursor selectnotesTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res  = db.rawQuery("select * from "+TABLE_NAME2,null);
        return res;
    }

    public Cursor selectCategoryTableWhere(String catid){
        SQLiteDatabase db2 = this.getWritableDatabase();
        Cursor res = db2.query
                (TABLE_NAME1,
                        new String[] {CAT_COL},
                        "id" + "=" + catid,
                        null, null, null, null, null
                );
        return res;
    }

    public Cursor selectImageTableWhere(String notetitle){
        SQLiteDatabase db2 = this.getWritableDatabase();
        Cursor res = db2.query
                (TABLE_NAME3,
                        new String[] {NOTE_IMAGE_COL2},
                        "notetitle" + "=" + notetitle,
                        null, null, null, null, null
                );
        return res;
    }

    public Cursor selectNoteListTableWhere(String catid){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor res = db.query
                (TABLE_NAME2,new String[] {NOTE_COL2,NOTE_COL4,NOTE_COL5}, "categoryid" + "=" + catid,
                        null, null, null, null, null
                );
        return res;
    }

    public Cursor selectNoteTitleTableWhere(String noteTitle){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.query
                (TABLE_NAME2,new String[] {NOTE_COL0}, "notetitle" + "=" + noteTitle,
                        null, null, null, null, null
                );
        return res;
    }

    public Cursor searchlist(String keyword){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME2 + " where " + NOTE_COL2 + " like ?", new String[] { "%" + keyword + "%" });
        return cursor;
    }

    public Cursor searchCat(String keyword){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME1 + " where " + CAT_COL + " like ?", new String[] { "%" + keyword + "%" });
        return cursor;
    }

    public Cursor selectallNoteListTableWhere(String noteid){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor res = db.query
                (TABLE_NAME2,new String[] {NOTE_COL2,NOTE_COL3}, "id" + "=" + noteid,
                        null, null, null, null, null
                );
        return res;
    }

    public Cursor NoteListTableSortDesc(){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor res = db.query
                (TABLE_NAME2,new String[] {NOTE_COL2,NOTE_COL4,NOTE_COL5}, null,
                        null, null, null, NOTE_COL2+" DESC", null
                );
        return res;
    }
    public Cursor NoteListTableSortAsc(){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor res = db.query
                (TABLE_NAME2,new String[] {NOTE_COL2,NOTE_COL4,NOTE_COL5}, null,
                        null, null, null, NOTE_COL2+" ASC", null
                );
        return res;
    }

    public Cursor NoteListTableSortDesc(String catid){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor res = db.query
                (TABLE_NAME2,new String[] {NOTE_COL2,NOTE_COL4,NOTE_COL5}, "categoryid" + "=" + catid,
                        null, null, null, NOTE_COL2+" DESC", null
                );
        return res;
    }
    public Cursor NoteListTableSortAsc(String catid){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor res = db.query
                (TABLE_NAME2,new String[] {NOTE_COL2,NOTE_COL4,NOTE_COL5}, "categoryid" + "=" + catid,
                        null, null, null, NOTE_COL2+" ASC", null
                );
        return res;
    }

    public Cursor NoteListTableSortdateDesc(String catid){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor res = db.query
                (TABLE_NAME2,new String[] {NOTE_COL2,NOTE_COL4,NOTE_COL5}, "categoryid" + "=" + catid,
                        null, null, null, NOTE_COL4+" DESC", null
                );
        return res;
    }
    public Cursor NoteListTableSortdateAsc(String catid){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor res = db.query
                (TABLE_NAME2,new String[] {NOTE_COL2,NOTE_COL4,NOTE_COL5}, "categoryid" + "=" + catid,
                        null, null, null, NOTE_COL4+" ASC", null
                );
        return res;
    }

    public Integer deleteFromCategoryTable(String catid){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME1, "id = ?", new String [] {catid});
    }

    public Integer deleteFromNoteListTable(String notetitle){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME2, "notetitle = ?", new String [] {notetitle});
    }

    public Integer deleteAllNoteListTable(String catid){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME2, "categoryid = ?", new String [] {catid});
    }

    public Boolean updatelistnoteTable(String noteid, String categoryid, String title,String detail,String date,String location){
        SQLiteDatabase db = this.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(NOTE_COL1, categoryid);
        values.put(NOTE_COL2, title);
        values.put(NOTE_COL3, detail);
        values.put(NOTE_COL4, date);
        values.put(NOTE_COL5, location);
        db.update(TABLE_NAME2,values, "id = ?", new String [] {noteid});
        return true;
    }

}
