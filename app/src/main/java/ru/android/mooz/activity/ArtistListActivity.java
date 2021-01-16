package ru.android.mooz.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ru.android.mooz.R;
import ru.android.mooz.adapter.AlbumAdapter;
import ru.android.mooz.adapter.PlayListAdapter;
import ru.android.mooz.adapter.ReccomendsAdapter;
import ru.android.mooz.adapter.TrackAdapter;
import ru.android.mooz.model.Playlist;
import ru.android.mooz.model.SearchPlaylist;
import ru.android.mooz.model.Track;
import ru.android.mooz.service.ServiceManager;

public class ArtistListActivity extends AppCompatActivity {

    private ImageView ivPlaylist;
    private TextView etTitle;
    private TextView etDescription;
    private TextView etTotal;
    private RecyclerView rvTracks;
    private RecyclerView rvPlaylist;
    private PlayListAdapter playlistAdapter;
    private AlbumAdapter albumAdapter;

    private TrackAdapter trackAdapter;
    private ReccomendsAdapter reccomendsAdapter;
    ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.artist_song);
        list();
        initView();
//        ew();

        ivPlaylist = findViewById(R.id.single_playlist_image);
        etTitle = findViewById(R.id.single_playlist_title);
        etDescription = findViewById(R.id.single_playlist_description);
        etTotal = findViewById(R.id.single_playlist_total);

        rvTracks = findViewById(R.id.rv_tracks);
        trackAdapter = new TrackAdapter();
        rvTracks.setLayoutManager(new LinearLayoutManager(this));
        rvTracks.setAdapter(trackAdapter);
        rvTracks.setHasFixedSize(true);

        if (getIntent().hasExtra("id")) {
            String id = getIntent().getStringExtra("id");
            new Thread(() -> {
                new ServiceManager.GETPlaylist(id, new ServiceManager.GETPlaylist.OnResponseListener() {
                    @Override
                    public void onResponse(String response) {
                        runOnUiThread(() -> {

                            JSONObject json = null;    // create JSON obj from string
                            try {
                                json = new JSONObject(response);

                                JSONObject tracks_object = json.getJSONObject("tracks");
                                JSONArray tracks_data = tracks_object.getJSONArray("data");

                                String tracks_json = tracks_data.toString();

                                String playlist_json = json.toString();

                                Playlist playlist = new Gson().fromJson(playlist_json, new TypeToken<Playlist>() {
                                }.getType());

                                ArrayList<Track> tracks = new Gson().fromJson(tracks_json, new TypeToken<List<Track>>() {
                                }.getType());

                                trackAdapter.setData(tracks);

                                etTitle.setText(playlist.getTitle());
                                etDescription.setText(playlist.getDescription());
                                etTotal.setText(playlist.getNb_tracks() + "");
                                Glide.with(ArtistListActivity.this).load(playlist.getPicture_big()).into(ivPlaylist);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        });
                    }

                });
            }).start();

        }

    }


/*    private void ew() {

        rvPlaylist = findViewById(R.id.playlist);
        albumAdapter = new AlbumAdapter();

        //  rvPlaylist.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rvPlaylist.setLayoutManager(new GridLayoutManager(this, 2));
        rvPlaylist.setAdapter(albumAdapter);
        rvPlaylist.setHasFixedSize(true);

        new Thread(() -> {
            new ServiceManager.GETSearchPlaylist("album", new ServiceManager.GETSearchPlaylist.OnResponseListener() {
                @Override
                public void onResponse(String response) {
                    runOnUiThread(() -> {

                        JSONObject json = null;
                        try {
                            json = new JSONObject(response);
                            JSONArray data = json.getJSONArray("art");

                            String playlist_json = data.toString();
                            ArrayList<SearchPlaylist> playlist = new Gson().fromJson(playlist_json, new TypeToken<List<SearchPlaylist>>() {
                            }.getType());


                            albumAdapter.setData(playlist);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    });
                }
            });
        }).start();

    }*/

        private void list() {

            rvPlaylist = findViewById(R.id.first_recycler_view);
            reccomendsAdapter = new ReccomendsAdapter();

            rvPlaylist.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            //rvPlaylist.setLayoutManager(new GridLayoutManager(this, 2));
            rvPlaylist.setAdapter(reccomendsAdapter);
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

                                reccomendsAdapter.setData(playlist);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        });
                    }
                });
            }).start();

        }

    private void initView() {
        btnBack = findViewById(R.id.btn_back);
        eventClick();
    }


    private void eventClick() {
        //click nút back
        btnBack.setOnClickListener(view -> finish());
    }
}
