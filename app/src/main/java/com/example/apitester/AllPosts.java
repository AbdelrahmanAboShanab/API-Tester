package com.example.apitester;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.CursorLoader;
import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.example.apitester.adapter.PostsCursorAdapter;
import com.example.apitester.database.PostContract;
import com.example.apitester.database.PostDbHelper;

public class AllPosts extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    ListView recyclerView;
    PostDbHelper postDbHelper;
    Cursor cursor;
    PostsCursorAdapter postsCursorAdapter;

    TextView id,userID,body,title;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_posts);

        postsCursorAdapter = new PostsCursorAdapter(this,null);
        getLoaderManager().initLoader(1,null,this);
        recyclerView = findViewById(R.id.dataRecycler);

        recyclerView.setAdapter(postsCursorAdapter);
    }



    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                PostContract.PostEntry._ID,
                PostContract.PostEntry.COLUMN_USER_ID,
                PostContract.PostEntry.COLUMN_TITLE,
                PostContract.PostEntry.COLUMN_BODY };

        return new CursorLoader(this,
                PostContract.PostEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.i("TAG", "onLoadFinished: "+cursor.getCount());
        postsCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        postsCursorAdapter.swapCursor(null);
    }

}