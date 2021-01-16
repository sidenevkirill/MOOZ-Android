package ru.android.mooz.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import ru.android.mooz.model.Song;

@Database(version = 1, entities = {Song.class}, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {


    public abstract SongsDao songs();
}
