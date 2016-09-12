package main.dbay.sqlitedb;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import main.dbay.Constants;
import main.dbay.ToastCustom;

/**
 * Created by vladvidavsky on 25/05/15.
 */
public class DBHandler {
    DBHelper dbHelper;
    ToastCustom tc;

    public DBHandler(Context context, Activity activity) {
        dbHelper = new DBHelper(context, "Dbay.db", null, 1);
        tc = new ToastCustom(activity);
    }



    public void saveItemToFavorites(String itemID, String favMetal, String favDetails, String favCertificate, String favPrice, String favCamera,  String favImageapp, String favVideo) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        try{
            ContentValues values = new ContentValues();
            values.put("itemid", itemID);
            values.put("metal", favMetal);
            values.put("details", favDetails);
            values.put("certificate", favCertificate);
            values.put("price", favPrice);
            values.put("cameraimage", favCamera);
            values.put("imageapp", favImageapp);
            values.put("video", favVideo);
            database.insertOrThrow(Constants.FAVORITES_SQL_TABLE, null, values);
            tc.showCustomToast(5);
        } catch (SQLiteException e){
            e.getCause();
        } finally {
            if(database.isOpen()) database.close();
        }
    };

    public void deleteItemFromFavorites(String id){
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        try{
            database.delete(Constants.FAVORITES_SQL_TABLE, "itemid = ?", new String[]{id});
            tc.showCustomToast(6);
        } catch(SQLiteException e){
            e.getCause();
        } finally {
            if(database.isOpen()) database.close();
        }
    };

    public void updateItemInFavorites(String itemID, String favMetal, String favDetails, String favCertificate, String favPrice, String favCamera, String favImageapp, String favVideo){
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        try{
            ContentValues values = new ContentValues();
            values.put("itemid", itemID);
            values.put("metal", favMetal);
            values.put("details", favDetails);
            values.put("certificate", favCertificate);
            values.put("price", favPrice);
            values.put("cameraimage", favCamera);
            values.put("imageapp", favImageapp);
            values.put("video", favVideo);
            database.update(Constants.FAVORITES_SQL_TABLE, values, "itemid = ?", new String[]{itemID});
        } catch (SQLiteException e){
            e.getCause();
        } finally {
            if(database.isOpen()) database.close();
        }
    }

    public Cursor getAllItems(){
        Cursor cursor = null;
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        try {
            cursor = database.query(Constants.FAVORITES_SQL_TABLE, null, null, null, null, null, null);
        } catch (SQLiteException e) {
            e.getCause();
        }

        return cursor;
    }


}
