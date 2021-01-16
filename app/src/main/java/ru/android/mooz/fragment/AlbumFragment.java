package ru.android.mooz.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ru.android.mooz.R;
import ru.android.mooz.adapter.AlbumAdapter;
import ru.android.mooz.model.SearchPlaylist;
import ru.android.mooz.service.ServiceManager;


public class AlbumFragment extends Fragment {

    private EditText textSearch;
    private ImageView btnSearch;
    private RecyclerView rvPlaylist;
    private AlbumAdapter albumAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_album, container, false);
        initView(root);
        return root;
    }

    private void initView(View root) {
        textSearch = root.findViewById(R.id.et_search);
        btnSearch = root.findViewById(R.id.btn_search);

        rvPlaylist = root.findViewById(R.id.rv_playlist);
        albumAdapter = new AlbumAdapter();

      //  rvPlaylist.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rvPlaylist.setLayoutManager(new GridLayoutManager(getContext(), 2));
        rvPlaylist.setAdapter(albumAdapter);
        rvPlaylist.setHasFixedSize(true);

        new Thread(() -> {
            new ServiceManager.GETSearchPlaylist("album", new ServiceManager.GETSearchPlaylist.OnResponseListener() {
                @Override
                public void onResponse(String response) {
                    getActivity().runOnUiThread(() -> {

                        JSONObject json = null;    // create JSON obj from string
                        try {
                            json = new JSONObject(response);
                            JSONArray data = json.getJSONArray("data");

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

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String search = textSearch.getText().toString();
                if (!search.isEmpty()) {
                    new Thread(() -> {
                        new ServiceManager.GETSearchPlaylist(search, new ServiceManager.GETSearchPlaylist.OnResponseListener() {
                            @Override
                            public void onResponse(String response) {
                                getActivity().runOnUiThread(() -> {

                                    JSONObject json = null;    // create JSON obj from string
                                    try {
                                        json = new JSONObject(response);
                                        JSONArray data = json.getJSONArray("data");

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
                }
            }
        });

    }

}
