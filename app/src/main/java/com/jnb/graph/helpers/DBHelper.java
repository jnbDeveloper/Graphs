package com.jnb.graph.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;

import com.jnb.graph.models.Reading;

import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, "graph.db", null, 1);
    }

    @Override
    public void onCreate(@NonNull SQLiteDatabase db) {
        db.execSQL("CREATE TABLE readings(x_val REAL, x_box INTEGER, y_val REAL, y_box INTEGER)");
    }

    @Override
    public void onUpgrade(@NonNull SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS readings");
    }

    public Boolean insertData(@NonNull List<Reading> readings) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();

        db.execSQL("DELETE FROM readings");

        boolean success = true;

        for (Reading r: readings) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("x_val", r.xValue);
            contentValues.put("x_box", r.xBoxes);
            contentValues.put("y_val", r.yValue);
            contentValues.put("y_box", r.yBoxes);

            long res = db.insert("readings", null, contentValues);

            if (res == -1)
                success = false;
        }

        db.setTransactionSuccessful();
        db.endTransaction();

        return success;
    }

    public Boolean deleteReadings(String name) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.execSQL("DELETE FROM readings");
            return true;
        }
        catch (Exception ex) {
            return false;
        }
    }

    public Cursor getReadings(){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM readings", null);
        return cursor;
    }
}