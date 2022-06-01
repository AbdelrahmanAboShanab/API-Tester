package com.example.apitester.adapter;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.apitester.R;
import com.example.apitester.database.PostContract;

public class PostsCursorAdapter extends CursorAdapter {

    public PostsCursorAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }


    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        int idCol = cursor.getColumnIndex(PostContract.PostEntry._ID);
        int userIDCol = cursor.getColumnIndex(PostContract.PostEntry.COLUMN_USER_ID);
        int titleCol = cursor.getColumnIndex(PostContract.PostEntry.COLUMN_TITLE);
        int bodyCol = cursor.getColumnIndex(PostContract.PostEntry.COLUMN_BODY);

        TextView id = (TextView) view.findViewById(R.id.id);
        TextView userID = (TextView) view.findViewById(R.id.userID);
        TextView title = (TextView) view.findViewById(R.id.title);
        TextView body = (TextView) view.findViewById(R.id.body);

        id.setText("Post id : "+cursor.getString(idCol));
        userID.setText("\nUser id : "+cursor.getString(userIDCol));
        title.setText("\nTitle : "+cursor.getString(titleCol));
        body.setText("\nBody : "+cursor.getString(bodyCol));

    }
}
