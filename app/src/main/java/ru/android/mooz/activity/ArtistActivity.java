package ru.android.mooz.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ru.android.mooz.adapter.ArtistAdapter;
import ru.android.mooz.model.SearchPlaylist;
import ru.android.mooz.service.ServiceManager;
import ru.android.mooz.R;


public class ArtistActivity extends AppCompatActivity {

    private EditText textSearch;
    private ImageView btnSearch;
    private RecyclerView rvPlaylist;
    private ArtistAdapter artistAdapter;

    @Nullable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist);
        initView();
    }

    private void initView() {
        textSearch = findViewById(R.id.et_search);
        btnSearch = findViewById(R.id.btn_search);

        rvPlaylist = findViewById(R.id.rv_playlist);
        artistAdapter = new ArtistAdapter();

        rvPlaylist.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        //rvPlaylist.setLayoutManager(new GridLayoutManager(this, 2));
        rvPlaylist.setAdapter(artistAdapter);
        rvPlaylist.setHasFixedSize(true);

        new Thread(() -> {
            new ServiceManager.GETSearchPlaylist("artist", new ServiceManager.GETSearchPlaylist.OnResponseListener() {
                @Override
                public void onResponse(String response) {
                    runOnUiThread(() -> {

                        JSONObject json = null;    // create JSON obj from string
                        try {
                            json = new JSONObject(response);
                            JSONArray data = json.getJSONArray("data");

                            String playlist_json = data.toString();
                            ArrayList<SearchPlaylist> playlist = new Gson().fromJson(playlist_json, new TypeToken<List<SearchPlaylist>>() {
                            }.getType());

                            artistAdapter.setData(playlist);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    });
                }
            });
        }).start();

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String search = textSearch.getText().toString();
                if (!search.isEmpty()) {
                    new Thread(() -> {
                        new ServiceManager.GETSearchPlaylist(search, new ServiceManager.GETSearchPlaylist.OnResponseListener() {
                            @Override
                            public void onResponse(String response) {
                                runOnUiThread(() -> {

                                    JSONObject json = null;    // create JSON obj from string
                                    try {
                                        json = new JSONObject(response);
                                        JSONArray data = json.getJSONArray("data");

                                        String playlist_json = data.toString();

                                        ArrayList<SearchPlaylist> playlist = new Gson().fromJson(playlist_json, new TypeToken<List<SearchPlaylist>>() {
                                        }.getType());

                                        artistAdapter.setData(playlist);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                });
                            }
                        });
                    }).start();
                }
            }
        });

    }
}