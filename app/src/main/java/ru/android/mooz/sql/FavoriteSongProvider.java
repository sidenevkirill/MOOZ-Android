package ru.android.mooz.sql;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class FavoriteSongProvider extends ContentProvider {

    private static final String AUTHORITY = "ru.pineapple.vku.sqlite.FavoriteSongProvider";
    public static final int SONG_TABLE = 100;
    public static final int SONG_ID = 110;

    private static final String SONG_BASE_PATH = "songFavorite";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
            + "/" + SONG_BASE_PATH);

    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/mt-songFavorite";
    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/mt-songFavorite";

    private SongsFavoriteTable mSongsFavorite;
    private SQLiteDatabase mData;


    private static final UriMatcher sURIMatcher = new UriMatcher(
            UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(AUTHORITY, SONG_BASE_PATH, SONG_TABLE);
        sURIMatcher.addURI(AUTHORITY, SONG_BASE_PATH + "/#", SONG_ID);
    }

    @Override
    public boolean onCreate() {
        mSongsFavorite = new SongsFavoriteTable(getContext());
        mData = mSongsFavorite.getReadableDatabase();
        mData = mSongsFavorite.getWritableDatabase();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        return mData.query(SongsFavoriteTable.TABLE_NAME,
                projection, selection, selectionArgs, null, null, sortOrder);
    }


    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        //them 1 bai hat moi
        long rowID = mData.insert(mSongsFavorite.TABLE_NAME, null, values);
        //neu 1 ban ghi dc them thanh cong

        if (rowID > 0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
        throw new SQLException("failed to add a record into " + uri);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
