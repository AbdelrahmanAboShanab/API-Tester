package com.example.apitester.database;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

public class PostProvider extends ContentProvider {


    private PostDbHelper postDbHelper;

    private static final int PETS = 100;
    private static final int PET_ID = 101;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(PostContract.CONTENT_AUTHORITY, PostContract.PATH_POSTS, PETS);
        sUriMatcher.addURI(PostContract.CONTENT_AUTHORITY, PostContract.PATH_POSTS + "/#", PET_ID);
    }


    @Override
    public boolean onCreate() {

        postDbHelper = new PostDbHelper(getContext());
        return true;
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        SQLiteDatabase db = postDbHelper.getReadableDatabase();
        Cursor cursor;
        int type = sUriMatcher.match(uri);
        switch (type) {
            case PETS:
                cursor = db.query(PostContract.PostEntry.TABLE_NAME, projection, selection
                        , selectionArgs, null, null, sortOrder);
                break;
            case PET_ID:

                selection = PostContract.PostEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                cursor = db.query(PostContract.PostEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot parser Uri");
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PETS:
                return insertPet(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertPet(Uri uri, ContentValues values) {

        SQLiteDatabase db = postDbHelper.getWritableDatabase();
        sanityCheck(values);

        long id = db.insert(PostContract.PostEntry.TABLE_NAME, null, values);
        if (id == -1) {
            Log.e("LOG_TAG", "Insert new pet failed");
            return null;
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }

    private void sanityCheck(ContentValues values) {
        if (values.containsKey(PostContract.PostEntry.COLUMN_TITLE)) {
            String name = values.getAsString(PostContract.PostEntry.COLUMN_TITLE);
            if (name == null) {
                throw new IllegalArgumentException("Pet requires a name");
            }
        }

    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PETS:
                return updatePet(uri, contentValues, selection, selectionArgs);
            case PET_ID:
                selection = PostContract.PostEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updatePet(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updatePet(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        sanityCheck(values);
        if (values.size() == 0) {
            return 0;
        }
        SQLiteDatabase db = postDbHelper.getWritableDatabase();
        int updatedRow = db.update(PostContract.PostEntry.TABLE_NAME, values, selection, selectionArgs);
        if (updatedRow != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return updatedRow;
    }



    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = postDbHelper.getWritableDatabase();

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PETS:
                break;
            case PET_ID:
                selection = PostContract.PostEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
        int deletedRow = database.delete(PostContract.PostEntry.TABLE_NAME, selection, selectionArgs);
        if (deletedRow != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return deletedRow;
    }


    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PETS:
                return PostContract.PostEntry.CONTENT_LIST_TYPE;
            case PET_ID:
                return PostContract.PostEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

}
