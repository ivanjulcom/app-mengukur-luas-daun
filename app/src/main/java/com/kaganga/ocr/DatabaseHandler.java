package com.kaganga.ocr;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler {

    // Database name
    private static final String DB_NAME = "kagangaocr.db";
    // Version number
    private static final int DB_VERSION = 1;

    // Table names
    private static final String TABLE_HISTORY = "riwayat";
    private static final String TABLE_TEMPLATE = "template";

    // History column values
    private static final String KEY_R_ID = "riwayat_id";
    private static final String KEY_R_DATE = "riwayat_date";
    private static final String KEY_R_IMAGEURI = "riwayat_imageuri";
    private static final String KEY_R_RESULT = "riwayat_result";

    // MatTemplate column values
    private static final String KEY_T_ID = "template_id";
    private static final String KEY_T_JENIS = "template_jenis";
    private static final String KEY_T_HURUF = "template_huruf";
    private static final String KEY_T_FILE = "template_file";

    //History table creation statement
    private static final String TABLE_HISTORY_CREATE = "CREATE TABLE " + TABLE_HISTORY + "("
            + KEY_R_ID + " INTEGER PRIMARY KEY, "
            + KEY_R_DATE + " TEXT NOT NULL, "
            + KEY_R_IMAGEURI + " TEXT NOT NULL, "
            + KEY_R_RESULT + " TEXT NOT NULL);";

    //History table creation statement
    private static final String TABLE_TEMPLATE_CREATE = "CREATE TABLE " + TABLE_TEMPLATE + "("
            + KEY_T_ID + " INTEGER PRIMARY KEY, "
            + KEY_T_JENIS + " TEXT NOT NULL, "
            + KEY_T_HURUF + " TEXT NOT NULL, "
            + KEY_T_FILE + " TEXT NOT NULL);";

    private DbHelper dbHelper;
    private Context context;
    private SQLiteDatabase db;

    public DatabaseHandler(Context context) {
        this.context = context;
    }

    public DatabaseHandler open() throws SQLiteException {
        dbHelper = new DbHelper(context);
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    // insert table
    public void insertHistory(History r) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_R_DATE, r.getDate());
        cv.put(KEY_R_IMAGEURI, r.getImageuri());
        cv.put(KEY_R_RESULT, r.getResult());
        db.insert(TABLE_HISTORY, null, cv);
    }

    // Get from table
    public List<History> getHistory() {
        List<History> historyList = new ArrayList<>();
        String selectQuery = "SELECT " + TABLE_HISTORY +".* FROM " + TABLE_HISTORY;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                History b = new History();
                b.setId(cursor.getInt(0));
                b.setDate(cursor.getString(1));
                b.setImageuri(cursor.getString(2));
                b.setResult(cursor.getString(3));

                historyList.add(b);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return historyList;
    }

    // Get from table
    public List<Template> getTemplateHuruf() {
        List<Template> templateList = new ArrayList<>();
        String selectQuery = "SELECT " + TABLE_TEMPLATE +".* FROM " + TABLE_TEMPLATE + " WHERE " + KEY_T_JENIS + "='HURUF'" ;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Template template = new Template();
                template.setId(cursor.getInt(0));
                template.setJenis(cursor.getString(1));
                template.setHuruf(cursor.getString(2));
                template.setFile(cursor.getString(3));

                templateList.add(template);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return templateList;
    }

    // Get from table
    public List<Template> getTemplateSandangan() {
        List<Template> templateList = new ArrayList<>();
        String selectQuery = "SELECT " + TABLE_TEMPLATE +".* FROM " + TABLE_TEMPLATE + " WHERE " + KEY_T_JENIS + "='SANDANGAN'" ;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Template template = new Template();
                template.setId(cursor.getInt(0));
                template.setJenis(cursor.getString(1));
                template.setHuruf(cursor.getString(2));
                template.setFile(cursor.getString(3));

                templateList.add(template);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return templateList;
    }

    // delete
    public boolean deleteHistory(int i) {
        return db.delete(TABLE_HISTORY, KEY_R_ID+"="+i, null)>0;
    }

    private static class DbHelper extends SQLiteOpenHelper {

        private DbHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(TABLE_HISTORY_CREATE);
            db.execSQL(TABLE_TEMPLATE_CREATE);

            db.execSQL("INSERT INTO "+TABLE_TEMPLATE+" ("+KEY_T_JENIS+","+KEY_T_HURUF+","+KEY_T_FILE+") VALUES ('HURUF','a','a1')");
            db.execSQL("INSERT INTO "+TABLE_TEMPLATE+" ("+KEY_T_JENIS+","+KEY_T_HURUF+","+KEY_T_FILE+") VALUES ('HURUF','a','a2')");
            db.execSQL("INSERT INTO "+TABLE_TEMPLATE+" ("+KEY_T_JENIS+","+KEY_T_HURUF+","+KEY_T_FILE+") VALUES ('HURUF','ba','ba1')");
            db.execSQL("INSERT INTO "+TABLE_TEMPLATE+" ("+KEY_T_JENIS+","+KEY_T_HURUF+","+KEY_T_FILE+") VALUES ('HURUF','ba','ba2')");
            db.execSQL("INSERT INTO "+TABLE_TEMPLATE+" ("+KEY_T_JENIS+","+KEY_T_HURUF+","+KEY_T_FILE+") VALUES ('HURUF','ca','ca1')");
            db.execSQL("INSERT INTO "+TABLE_TEMPLATE+" ("+KEY_T_JENIS+","+KEY_T_HURUF+","+KEY_T_FILE+") VALUES ('HURUF','da','da1')");
            db.execSQL("INSERT INTO "+TABLE_TEMPLATE+" ("+KEY_T_JENIS+","+KEY_T_HURUF+","+KEY_T_FILE+") VALUES ('HURUF','ga','ga1')");
            db.execSQL("INSERT INTO "+TABLE_TEMPLATE+" ("+KEY_T_JENIS+","+KEY_T_HURUF+","+KEY_T_FILE+") VALUES ('HURUF','ha','ha1')");
            db.execSQL("INSERT INTO "+TABLE_TEMPLATE+" ("+KEY_T_JENIS+","+KEY_T_HURUF+","+KEY_T_FILE+") VALUES ('HURUF','ja','ja1')");
            db.execSQL("INSERT INTO "+TABLE_TEMPLATE+" ("+KEY_T_JENIS+","+KEY_T_HURUF+","+KEY_T_FILE+") VALUES ('HURUF','ja','ja2')");
            db.execSQL("INSERT INTO "+TABLE_TEMPLATE+" ("+KEY_T_JENIS+","+KEY_T_HURUF+","+KEY_T_FILE+") VALUES ('HURUF','ka','ka1')");
            db.execSQL("INSERT INTO "+TABLE_TEMPLATE+" ("+KEY_T_JENIS+","+KEY_T_HURUF+","+KEY_T_FILE+") VALUES ('HURUF','ka','ka2')");
            db.execSQL("INSERT INTO "+TABLE_TEMPLATE+" ("+KEY_T_JENIS+","+KEY_T_HURUF+","+KEY_T_FILE+") VALUES ('HURUF','ka','ka3')");
            db.execSQL("INSERT INTO "+TABLE_TEMPLATE+" ("+KEY_T_JENIS+","+KEY_T_HURUF+","+KEY_T_FILE+") VALUES ('HURUF','la','la')");
            db.execSQL("INSERT INTO "+TABLE_TEMPLATE+" ("+KEY_T_JENIS+","+KEY_T_HURUF+","+KEY_T_FILE+") VALUES ('HURUF','ma','ma1')");
            db.execSQL("INSERT INTO "+TABLE_TEMPLATE+" ("+KEY_T_JENIS+","+KEY_T_HURUF+","+KEY_T_FILE+") VALUES ('HURUF','ma','ma2')");
            db.execSQL("INSERT INTO "+TABLE_TEMPLATE+" ("+KEY_T_JENIS+","+KEY_T_HURUF+","+KEY_T_FILE+") VALUES ('HURUF','ma','ma3')");
            db.execSQL("INSERT INTO "+TABLE_TEMPLATE+" ("+KEY_T_JENIS+","+KEY_T_HURUF+","+KEY_T_FILE+") VALUES ('HURUF','ma','ma4')");
            db.execSQL("INSERT INTO "+TABLE_TEMPLATE+" ("+KEY_T_JENIS+","+KEY_T_HURUF+","+KEY_T_FILE+") VALUES ('HURUF','mba','mba1')");
            db.execSQL("INSERT INTO "+TABLE_TEMPLATE+" ("+KEY_T_JENIS+","+KEY_T_HURUF+","+KEY_T_FILE+") VALUES ('HURUF','mpa','mpa1')");
            db.execSQL("INSERT INTO "+TABLE_TEMPLATE+" ("+KEY_T_JENIS+","+KEY_T_HURUF+","+KEY_T_FILE+") VALUES ('HURUF','mpa','mpa2')");
            db.execSQL("INSERT INTO "+TABLE_TEMPLATE+" ("+KEY_T_JENIS+","+KEY_T_HURUF+","+KEY_T_FILE+") VALUES ('HURUF','na','na1')");
            db.execSQL("INSERT INTO "+TABLE_TEMPLATE+" ("+KEY_T_JENIS+","+KEY_T_HURUF+","+KEY_T_FILE+") VALUES ('HURUF','nca','nca1')");
            db.execSQL("INSERT INTO "+TABLE_TEMPLATE+" ("+KEY_T_JENIS+","+KEY_T_HURUF+","+KEY_T_FILE+") VALUES ('HURUF','nda','nda1')");
            db.execSQL("INSERT INTO "+TABLE_TEMPLATE+" ("+KEY_T_JENIS+","+KEY_T_HURUF+","+KEY_T_FILE+") VALUES ('HURUF','nda','nda2')");
            db.execSQL("INSERT INTO "+TABLE_TEMPLATE+" ("+KEY_T_JENIS+","+KEY_T_HURUF+","+KEY_T_FILE+") VALUES ('HURUF','nda','nda3')");
            db.execSQL("INSERT INTO "+TABLE_TEMPLATE+" ("+KEY_T_JENIS+","+KEY_T_HURUF+","+KEY_T_FILE+") VALUES ('HURUF','nga','nga1')");
            db.execSQL("INSERT INTO "+TABLE_TEMPLATE+" ("+KEY_T_JENIS+","+KEY_T_HURUF+","+KEY_T_FILE+") VALUES ('HURUF','nga2','nga2')");
            db.execSQL("INSERT INTO "+TABLE_TEMPLATE+" ("+KEY_T_JENIS+","+KEY_T_HURUF+","+KEY_T_FILE+") VALUES ('HURUF','ngga','ngga1')");
            db.execSQL("INSERT INTO "+TABLE_TEMPLATE+" ("+KEY_T_JENIS+","+KEY_T_HURUF+","+KEY_T_FILE+") VALUES ('HURUF','ngga','ngga2')");
            db.execSQL("INSERT INTO "+TABLE_TEMPLATE+" ("+KEY_T_JENIS+","+KEY_T_HURUF+","+KEY_T_FILE+") VALUES ('HURUF','ngka','ngka1')");
            db.execSQL("INSERT INTO "+TABLE_TEMPLATE+" ("+KEY_T_JENIS+","+KEY_T_HURUF+","+KEY_T_FILE+") VALUES ('HURUF','nja','nja1')");
            db.execSQL("INSERT INTO "+TABLE_TEMPLATE+" ("+KEY_T_JENIS+","+KEY_T_HURUF+","+KEY_T_FILE+") VALUES ('HURUF','nja','nja2')");
            db.execSQL("INSERT INTO "+TABLE_TEMPLATE+" ("+KEY_T_JENIS+","+KEY_T_HURUF+","+KEY_T_FILE+") VALUES ('HURUF','nta','nta1')");
            db.execSQL("INSERT INTO "+TABLE_TEMPLATE+" ("+KEY_T_JENIS+","+KEY_T_HURUF+","+KEY_T_FILE+") VALUES ('HURUF','nta','nta2')");
            db.execSQL("INSERT INTO "+TABLE_TEMPLATE+" ("+KEY_T_JENIS+","+KEY_T_HURUF+","+KEY_T_FILE+") VALUES ('HURUF','nya','nya1')");
            db.execSQL("INSERT INTO "+TABLE_TEMPLATE+" ("+KEY_T_JENIS+","+KEY_T_HURUF+","+KEY_T_FILE+") VALUES ('HURUF','pa','pa1')");
            db.execSQL("INSERT INTO "+TABLE_TEMPLATE+" ("+KEY_T_JENIS+","+KEY_T_HURUF+","+KEY_T_FILE+") VALUES ('HURUF','ra','ra1')");
            db.execSQL("INSERT INTO "+TABLE_TEMPLATE+" ("+KEY_T_JENIS+","+KEY_T_HURUF+","+KEY_T_FILE+") VALUES ('HURUF','ra','ra2')");
            db.execSQL("INSERT INTO "+TABLE_TEMPLATE+" ("+KEY_T_JENIS+","+KEY_T_HURUF+","+KEY_T_FILE+") VALUES ('HURUF','ra','ra3')");
            db.execSQL("INSERT INTO "+TABLE_TEMPLATE+" ("+KEY_T_JENIS+","+KEY_T_HURUF+","+KEY_T_FILE+") VALUES ('HURUF','sa','sa1')");
            db.execSQL("INSERT INTO "+TABLE_TEMPLATE+" ("+KEY_T_JENIS+","+KEY_T_HURUF+","+KEY_T_FILE+") VALUES ('HURUF','sa','sa2')");
            db.execSQL("INSERT INTO "+TABLE_TEMPLATE+" ("+KEY_T_JENIS+","+KEY_T_HURUF+","+KEY_T_FILE+") VALUES ('HURUF','ta','ta1')");
            db.execSQL("INSERT INTO "+TABLE_TEMPLATE+" ("+KEY_T_JENIS+","+KEY_T_HURUF+","+KEY_T_FILE+") VALUES ('HURUF','ta','ta3')");
            db.execSQL("INSERT INTO "+TABLE_TEMPLATE+" ("+KEY_T_JENIS+","+KEY_T_HURUF+","+KEY_T_FILE+") VALUES ('HURUF','wa','wa1')");
            db.execSQL("INSERT INTO "+TABLE_TEMPLATE+" ("+KEY_T_JENIS+","+KEY_T_HURUF+","+KEY_T_FILE+") VALUES ('HURUF','ya','ya1')");

            db.execSQL("INSERT INTO "+TABLE_TEMPLATE+" ("+KEY_T_JENIS+","+KEY_T_HURUF+","+KEY_T_FILE+") VALUES ('SANDANGAN','a','a')");
            db.execSQL("INSERT INTO "+TABLE_TEMPLATE+" ("+KEY_T_JENIS+","+KEY_T_HURUF+","+KEY_T_FILE+") VALUES ('SANDANGAN','e','e')");
            db.execSQL("INSERT INTO "+TABLE_TEMPLATE+" ("+KEY_T_JENIS+","+KEY_T_HURUF+","+KEY_T_FILE+") VALUES ('SANDANGAN','E','e2')");
            db.execSQL("INSERT INTO "+TABLE_TEMPLATE+" ("+KEY_T_JENIS+","+KEY_T_HURUF+","+KEY_T_FILE+") VALUES ('SANDANGAN','end','end1')");
            db.execSQL("INSERT INTO "+TABLE_TEMPLATE+" ("+KEY_T_JENIS+","+KEY_T_HURUF+","+KEY_T_FILE+") VALUES ('SANDANGAN','end','end2')");
            db.execSQL("INSERT INTO "+TABLE_TEMPLATE+" ("+KEY_T_JENIS+","+KEY_T_HURUF+","+KEY_T_FILE+") VALUES ('SANDANGAN','i','i')");
            db.execSQL("INSERT INTO "+TABLE_TEMPLATE+" ("+KEY_T_JENIS+","+KEY_T_HURUF+","+KEY_T_FILE+") VALUES ('SANDANGAN','I','i2')");
            db.execSQL("INSERT INTO "+TABLE_TEMPLATE+" ("+KEY_T_JENIS+","+KEY_T_HURUF+","+KEY_T_FILE+") VALUES ('SANDANGAN','n','n')");
            db.execSQL("INSERT INTO "+TABLE_TEMPLATE+" ("+KEY_T_JENIS+","+KEY_T_HURUF+","+KEY_T_FILE+") VALUES ('SANDANGAN','ng','ng')");
            db.execSQL("INSERT INTO "+TABLE_TEMPLATE+" ("+KEY_T_JENIS+","+KEY_T_HURUF+","+KEY_T_FILE+") VALUES ('SANDANGAN','o','o')");
            db.execSQL("INSERT INTO "+TABLE_TEMPLATE+" ("+KEY_T_JENIS+","+KEY_T_HURUF+","+KEY_T_FILE+") VALUES ('SANDANGAN','r','r')");
            db.execSQL("INSERT INTO "+TABLE_TEMPLATE+" ("+KEY_T_JENIS+","+KEY_T_HURUF+","+KEY_T_FILE+") VALUES ('SANDANGAN','u','u')");
            db.execSQL("INSERT INTO "+TABLE_TEMPLATE+" ("+KEY_T_JENIS+","+KEY_T_HURUF+","+KEY_T_FILE+") VALUES ('SANDANGAN','w','w')");
            db.execSQL("INSERT INTO "+TABLE_TEMPLATE+" ("+KEY_T_JENIS+","+KEY_T_HURUF+","+KEY_T_FILE+") VALUES ('SANDANGAN','y','y')");

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEMPLATE);

            onCreate(db);
        }
    }
}