package com.example.apitester.database;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class PostContract {


    private PostContract() {
    }

    public static final String CONTENT_AUTHORITY = "com.example.apitester";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_POSTS = "posts";


    public static final class PostEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_POSTS);

        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_POSTS;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_POSTS;

        public final static String TABLE_NAME = "posts";

        public final static String _ID = BaseColumns._ID;

        public final static String COLUMN_TITLE = "title";

        public final static String COLUMN_BODY = "body";

        public final static String COLUMN_USER_ID = "userID";

    }
}
