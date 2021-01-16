package ru.android.mooz.api;


import ru.android.mooz.model.Song;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;


public interface MusicApi {
    @GET("master/hostmus.php")
    Call<List<Song>> listSongAll();

    @GET("master/popular.php")
    Call<List<Song>> listSongPop();

    @GET("master/chart.php")
    Call<List<Song>>topChart();

    @GET("master/search.php")
    Call<List<Song>> listSongSearch();

    @GET("master/reccomendation.php")
    Call<List<Song>> listSongRecc();
}
