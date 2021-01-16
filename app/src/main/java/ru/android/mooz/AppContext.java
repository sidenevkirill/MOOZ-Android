package ru.android.mooz;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.room.Room;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.android.mooz.api.MusicApi;
import ru.android.mooz.db.AppDatabase;

public class AppContext extends Application {
    public static volatile SharedPreferences preferences;
    public static volatile Context context;
    public static volatile OkHttpClient client;
    public static volatile Retrofit retrofit;
    public static volatile MusicApi api;
    public static volatile AppDatabase database;


    @Override
    public void onCreate() {
        super.onCreate();

        context = this;
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        initApi();
        initDatabase();
    }

    private void initDatabase() {
        database = Room.databaseBuilder(context, AppDatabase.class, "cache.db")
                .allowMainThreadQueries()
                .build();
    }

    private void initApi() {
        client = new OkHttpClient.Builder()
                .connectTimeout(30000, TimeUnit.SECONDS)
                .readTimeout(30000, TimeUnit.SECONDS).build();
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        retrofit = new Retrofit.Builder()
                .baseUrl("https://raw.githubusercontent.com/sidenevkirill/MOOZ/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        api = retrofit.create(MusicApi.class);
    }
}
