package ru.android.mooz.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SongsFavoriteTable extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "songManager";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "songFavorite";

    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_AUTHOR = "author";
    public static final String ID_PROVIDER = "id_provider";
    public static final String IS_FAVORITE = "is_favorite";
    public static final String COUNT_OF_PLAY = "count_of_play";

    public SongsFavoriteTable(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_songs_table = String.
                format("CREATE TABLE %s(%s INTEGER PRIMARY KEY AUTOINCREMENT,%s INTEGER, %s TEXT, %s TEXT,%s INTEGER, %s INTEGER )",
                        TABLE_NAME, KEY_ID, ID_PROVIDER, KEY_NAME,
                        KEY_AUTHOR, IS_FAVORITE, COUNT_OF_PLAY);
        db.execSQL(create_songs_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String drop_songs_table = String.format("DROP TABLE IF EXISTS %s", TABLE_NAME);
        db.execSQL(drop_songs_table);

        onCreate(db);
    }
}
