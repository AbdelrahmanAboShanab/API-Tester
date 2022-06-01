package com.example.apitester.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class PostDbHelper extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "posts.db";

    private static final int DATABASE_VERSION = 1;


    public PostDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String SQL_CREATE_PETS_TABLE = "CREATE TABLE "
                + PostContract.PostEntry.TABLE_NAME + " ("
                + PostContract.PostEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PostContract.PostEntry.COLUMN_USER_ID + " INTEGER , "
                + PostContract.PostEntry.COLUMN_TITLE + " TEXT NOT NULL, "
                + PostContract.PostEntry.COLUMN_BODY + " TEXT NOT NULL );";
        sqLiteDatabase.execSQL(SQL_CREATE_PETS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
