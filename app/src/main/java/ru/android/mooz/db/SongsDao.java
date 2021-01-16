package ru.android.mooz.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import ru.android.mooz.model.Song;

@Dao
public interface SongsDao {
    @Query("SELECT * FROM songs")
    LiveData<List<Song>> get();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<Song> songs);

    @Delete
    void delete(List<Song> songs);
}
