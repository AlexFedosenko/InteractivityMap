package com.alexander.interactivitymap.map.core;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper{

    private static DatabaseHelper sInstance;

    static final String DATABASE_NAME = "InterMap.db";
    static final String MARKERS_TABLE_NAME = "markersTable";
    static final String	COLUMN_ID = "_id";
    static final String	COLUMN_LATITUDE = "latitude";
    static final String	COLUMN_LONGITUDE = "longitude";
    static final String	COLUMN_TITLE = "title";
    static final String	COLUMN_DESCRIPTION = "description";
    static final int	DATABASE_VERSION = 1;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    public static DatabaseHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DatabaseHelper(context);
        }
        return sInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        StringBuilder createDatabase = new StringBuilder();
        createDatabase.append("create table ");
        createDatabase.append(MARKERS_TABLE_NAME);
        createDatabase.append('(');
        createDatabase.append(COLUMN_ID);
        createDatabase.append(" integer primary key autoincrement, ");
        createDatabase.append(COLUMN_LATITUDE);
        createDatabase.append(" real, ");
        createDatabase.append(COLUMN_LONGITUDE);
        createDatabase.append(" real, ");
        createDatabase.append(COLUMN_TITLE);
        createDatabase.append(" text, ");
        createDatabase.append(COLUMN_DESCRIPTION);
        createDatabase.append(" text);");

        sqLiteDatabase.execSQL(createDatabase.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        sqLiteDatabase.execSQL("drop table if exists " + MARKERS_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public int editMarker(double lat, double lng, String title, String description) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_LATITUDE, lat);
        cv.put(COLUMN_LONGITUDE, lng);
        cv.put(COLUMN_TITLE, title);
        cv.put(COLUMN_DESCRIPTION, description);
        return db.update(MARKERS_TABLE_NAME, cv, COLUMN_LATITUDE + " = " + lat + " and " + COLUMN_LONGITUDE + " = " + lng, null);
    }

    public long addMarker(double lat, double lng, String title, String description) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_LATITUDE, lat);
        cv.put(COLUMN_LONGITUDE, lng);
        cv.put(COLUMN_TITLE, title);
        cv.put(COLUMN_DESCRIPTION, description);
        return db.insert(MARKERS_TABLE_NAME, null, cv);
    }

    public List<CustomMarker> getMarkers() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(MARKERS_TABLE_NAME, null, null, null, null, null, null);
        List<CustomMarker> markerList = new ArrayList<CustomMarker>();

        if (cursor.moveToFirst()) {
            do {
                double lat = cursor.getDouble(cursor.getColumnIndex(COLUMN_LATITUDE));
                double lng = cursor.getDouble(cursor.getColumnIndex(COLUMN_LONGITUDE));
                String title = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE));
                String description = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION));
                markerList.add(new CustomMarker(lat, lng, title, description));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return markerList;
    }
}
