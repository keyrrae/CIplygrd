/*
 *  Copyright (c) 2017 - present, Zhenyu Yang
 *  All rights reserved.
 *
 *  This source code is licensed under the BSD-style license found in the
 *  LICENSE file in the root directory of this source tree.
 */

package edu.ucsb.cs.cs190i.papertown.town.newtown;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import edu.ucsb.cs.cs190i.papertown.models.Town;
import edu.ucsb.cs.cs190i.papertown.models.TownBuilder;

/**
 * Created by Zhenyu on 2017-06-03.
 */

    public class TownDatabaseHelper extends SQLiteOpenHelper {

    private static final String CreateTownTable = "CREATE TABLE Towns (Id integer PRIMARY KEY AUTOINCREMENT, TownID text, Title text, Address text, Category text, Description text, UserAlias text, Location text, ImageUris text);";

    private static final String DatabaseName = "TownDatabase.db";
    private static TownDatabaseHelper Instance;
    private List<OnDatabaseChangeListener> Listeners;

    private TownDatabaseHelper(Context context) {
        super(context, DatabaseName, null, 1);
        Listeners = new ArrayList<>();
    }

    public static void Initialize(Context context) {
        Instance = new TownDatabaseHelper(context);
    }

    public static TownDatabaseHelper GetInstance() {
        return Instance;
    }

    public void Subscribe(OnDatabaseChangeListener listener) {
        Listeners.add(listener);
    }

    private boolean TryUpdate(Cursor cursor) {
        try {
            cursor.moveToFirst();
        } catch (SQLiteConstraintException exception) {
            return false;
        } finally {
            cursor.close();
        }
        NotifyListeners();
        return true;
    }

    private void NotifyListeners() {
        for (OnDatabaseChangeListener listener : Listeners) {
            listener.OnDatabaseChange();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("ImageTagDatabaseHelper", "onCreate");
        db.execSQL(CreateTownTable);
        Log.i("ImageTagDatabaseHelper", "db = " + db.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void ClearDB(SQLiteDatabase db){


        db.execSQL("DROP TABLE IF EXISTS " + "Image");
        db.execSQL("DROP TABLE IF EXISTS " + "Tag");
        db.execSQL("DROP TABLE IF EXISTS " + "TABLE_NAME");
        onCreate(db);


    }

    public interface OnDatabaseChangeListener {
        void OnDatabaseChange();
    }

    int saveTownToDB(Town town) {
        SQLiteDatabase database_w = this.getWritableDatabase();
        Log.i("SQLiteDatabase", "database = " + database_w.toString());
        // Insert the new row, returning the primary key value of the new row
        String tableName_insert = "Towns";

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
//        String column_name_insert = "Text";

        values.put("TownID", town.getId());
        values.put("Title", town.getTitle());
        values.put("Address", town.getAddress());
        values.put("Category", town.getCategory());
        values.put("Description", town.getDescription());
        values.put("UserAlias", town.getUserAlias());
        values.put("Location", town.getLatLng());
        values.put("ImageUris", town.getImageUriString());



        try {
            long newRowId = database_w.insertOrThrow(tableName_insert, null, values);
            //  Log.i("SQLiteDatabase", "Tag saved = " + tag_input + ", index = " + newRowId);
            return (int) newRowId;
        } catch (Exception e) {
//            //Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
//            int back = getTagsIndexByContent(tag_input, database_r);
            Log.i("SQLiteDatabase", "Exception original ,   " + e.toString());
//            Log.i("SQLiteDatabase", "Exception,   " + tag_input + "   has a duplicated value in the database, get int back = " + back);
//            Log.i("onActivityResult", "tag not saved, existing tag with index = "+back);
            return 1;
        }
    }


    List<Town> getALLTownsFromDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        String tableName_read = "Towns";
        String query = "SELECT * FROM " + tableName_read;

        Cursor cursor = db.rawQuery(query, null);
        List<Town> towns = new ArrayList<>();



        Log.i("Read from DB", "getALLTownsFromDB !");
        ArrayList<String> itemIds = new ArrayList<>();
        ArrayList<String> itemIds2 = new ArrayList<>();
        ArrayList<String> itemIds_des = new ArrayList<>();
        ArrayList<String> itemIds_uris = new ArrayList<>();
        while (cursor.moveToNext()) {
            String TownID = (cursor.getString(cursor.getColumnIndex("TownID")));
            String Title = (cursor.getString(cursor.getColumnIndex("Title")));
            String Address = (cursor.getString(cursor.getColumnIndex("Address")));
            String Category = (cursor.getString(cursor.getColumnIndex("Category")));
            String Description = (cursor.getString(cursor.getColumnIndex("Description")));
            String UserAlias = (cursor.getString(cursor.getColumnIndex("UserAlias")));
            String Location = (cursor.getString(cursor.getColumnIndex("Location")));
            String ImageUris = (cursor.getString(cursor.getColumnIndex("ImageUris")));



            //processing address to latlng
            String[] separated = Location.split(",");
            float lat = Float.parseFloat(separated[0]);
            float lng = Float.parseFloat(separated[1]);


            //Process to get ImageUris
            ArrayList<String> uriStringArrayList_temp = new ArrayList<>();
            separated = ImageUris.split(",");
            for(int i = 0;i<separated.length;i++){
                uriStringArrayList_temp.add(separated[i]);
            }

            Town outputTown = new TownBuilder()
                    .setTitle(Title)
                    .setAddress(Address)
                    .setCategory(Category)
                    .setDescription(Description)
                    .setUserAlias(UserAlias)
                    .setLat(lat)
                    .setLng(lng)
                    .setImages(uriStringArrayList_temp)
                    .build();

            outputTown.setId(TownID);
            towns.add(outputTown);
        }
        cursor.close();
        return towns;
    }
}
