package com.jaggu.sitams;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class Sitamsldb extends SQLiteOpenHelper {

    public Sitamsldb(Context applicationcontext) {
        super(applicationcontext, "sitams", null, 2);
    }

    //Creates Table
    @Override
    public void onCreate(SQLiteDatabase database) {
        String query;
        query = "CREATE TABLE notifications ( nId INTEGER,ntitle TEXT, nbody TEXT,ndate TEXT,ncol INTEGER,noter INTEGER)";
        String query2 = "CREATE TABLE loginid ( nId TEXT,nPas TEXT)";
        database.execSQL(query);
        database.execSQL(query2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int version_old, int current_version) {
        String query;
        query = "DROP TABLE IF EXISTS notifications";
        database.execSQL(query);
        onCreate(database);
    }


    /**
     * Inserts notifications into SQLite DB
     *
     * @param queryValues
     */
    public void insertnotification(HashMap<String, String> queryValues) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nId", queryValues.get("nId"));
        values.put("ntitle", queryValues.get("ntitle"));
        values.put("nbody", queryValues.get("nbody"));
        values.put("ndate", queryValues.get("ndate"));
        values.put("ncol", queryValues.get("ncol"));
        database.insert("notifications", null, values);
        database.close();
    }

    /**
     * Get list of notifications from SQLite DB as Array List
     *
     * @return
     */
    public ArrayList<HashMap<String, String>> getnotifications() {
        ArrayList<HashMap<String, String>> notifyList;
        notifyList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  * FROM notifications";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("nbody", cursor.getString(1));
                map.put("ndate", cursor.getString(2));
                notifyList.add(map);
            } while (cursor.moveToNext());
        }
        database.close();
        return notifyList;
    }

    public String getbody(int r) {
        String str = null;
        String selectQuery = "SELECT  nbody FROM notifications where ncol=" + r;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {

                str = cursor.getString(0);
            } while (cursor.moveToNext());
        }
        database.close();
        return str;
    }

    public ArrayList<HashMap<String, String>> gettitles() {
        ArrayList<HashMap<String, String>> notifyList;
        notifyList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  ndate,ntitle,nbody FROM notifications";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("ndate", cursor.getString(0));
                map.put("ntitle", cursor.getString(1));
                map.put("nbody",cursor.getString(2));
                notifyList.add(map);
            } while (cursor.moveToNext());
        }
        database.close();
        return notifyList;
    }

    public boolean deletetab() {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete("notifications", null, null);
        database.close();
        return true;
    }

    public String getmaxrow() {
        String n = null;
        String selectQuery = "SELECT noter FROM notifications";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                n = cursor.getString(0);
            } while (cursor.moveToNext());
        }
        Log.i("tab val", "" + n);
        database.close();
        return n;
    }

    public String getmaxrow1() {
        String n = null;
        String selectQuery = "SELECT MAX(nId) FROM notifications";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                n = cursor.getString(0);
            } while (cursor.moveToNext());
        }
        Log.i("max val", "" + n);
        database.close();
        return n;
    }

    public void setmax(String kep) {
        String selectQuery = "UPDATE notifications SET noter=" + kep;
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL(selectQuery);
        Log.i("sql inrow", "done");
        database.close();
    }
}
