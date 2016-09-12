package main.dbay.sqlitedb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by vladvidavsky on 25/05/15.
 */
public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String newTable = ("CREATE TABLE Favorites_diamonds (_id INTEGER PRIMARY KEY, itemid TEXT, metal TEXT, details TEXT, certificate TEXT, price INT, cameraimage TEXT, imageapp TEXT, video TEXT)");
        try {
            db.execSQL(newTable);
        } catch (SQLiteException e) {
            e.getCause();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
