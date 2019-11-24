package com.example.mp3player;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class FavoriteDatabase extends SQLiteOpenHelper {

    public static FavoriteDatabase sInstance;

    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "favorite.db";
    public static final String SQL_CREATE_ENTERS =
            String.format(
                    "CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT)",
                    favoriteMemo.MemoEntry.TABLE_NAME,
                    favoriteMemo.MemoEntry._ID,
                    favoriteMemo.MemoEntry.COLUMN_TITLE,
                    favoriteMemo.MemoEntry.COLUMN_ALBUM,
                    favoriteMemo.MemoEntry.COLUMN_ARTIST,
                    favoriteMemo.MemoEntry.COLUMN_ALBUMID,
                    favoriteMemo.MemoEntry.COLUMN_ID);

    public static final String SQL_DELETE_ENTERS =
            "DROP TABLE IF EXISTS " + favoriteMemo.MemoEntry.TABLE_NAME;

    public static FavoriteDatabase getsInstance(Context context) {
        if (sInstance == null) {
            sInstance = new FavoriteDatabase(context);
        }
        return sInstance;
    }

    public FavoriteDatabase(Context context) {

        super(context, DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion>1){
            db.execSQL(SQL_DELETE_ENTERS);
            onCreate(db);
        }
    }
}
