package ru.android.mooz.fragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.android.mooz.AppContext;
import ru.android.mooz.R;
import ru.android.mooz.activity.PlayActivity;
import ru.android.mooz.adapter.PlayListAdapter;
import ru.android.mooz.adapter.SongAdapter;
import ru.android.mooz.model.SearchPlaylist;
import ru.android.mooz.model.Song;

import java.util.ArrayList;
import java.util.List;

import am.appwise.components.ni.NoInternetDialog;
import ru.android.mooz.model.Users;
import ru.android.mooz.service.ServiceManager;

public class AudiosFragment extends Fragment implements SongAdapter.ItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    private RelativeLayout titlePlay;
    private SongAdapter adapter;
    private RecyclerView recycler;
    private SwipeRefreshLayout swipeRefresh;
    private NoInternetDialog noInternetDialog;
    private RecyclerView rvPlaylist;
    private PlayListAdapter playlistAdapter;
    CircleImageView profile_image;
    TextView username;

    FirebaseUser firebaseUser;
    DatabaseReference reference;

    public static AudiosFragment newInstance() {
        Bundle args = new Bundle();

        AudiosFragment fragment = new AudiosFragment();
        fragment.setArguments(args);
        return fragment;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        initList(root);
        initView(root);
        return root;

    }

    private void initView(View root) {
        titlePlay = root.findViewById(R.id.title_play);
        recycler = root.findViewById(R.id.rcv_list_song);
        profile_image = root.findViewById(R.id.profile_image);
        username = root.findViewById(R.id.username);
        swipeRefresh = root.findViewById(R.id.refresh);
        swipeRefresh.setColorSchemeColors(
                Color.BLACK, Color.RED, Color.BLUE, Color.YELLOW
        );
        swipeRefresh.setOnRefreshListener(this);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users user = dataSnapshot.getValue(Users.class);
                //        username.setText(user.getUsername());
                if (user.getImageURL().equals("default")) {
                    profile_image.setImageResource(R.mipmap.ic_launcher);
                } else {
                    Glide.with(getContext()).load(user.getImageURL()).into(profile_image);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recycler.setLayoutManager(layoutManager);
        recycler.setNestedScrollingEnabled(false);
        recycler.setHasFixedSize(true);

        // диалоговое окно
        noInternetDialog = new NoInternetDialog.Builder(getActivity()).build();
        noInternetDialog.setOnDismissListener(dialogInterface -> {
            // Когда сеть будет доступна, перезагрузить API
            if (adapter == null) getAllSongs();
        });

        swipeRefresh.setRefreshing(true);
        getCachedSongs();
        getAllSongs();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        noInternetDialog.onDestroy();
    }

    private void createAdapter(List<Song> songs) {
        adapter = new SongAdapter(getActivity(), songs, AudiosFragment.this);
        recycler.setAdapter(adapter);
    }

    private void getCachedSongs() {
        AppContext.database.songs().get()
                .observe(this, this::createAdapter);
    }

    private void getAllSongs() {
        swipeRefresh.setRefreshing(true);
        AppContext.api.listSongAll()
                .enqueue(new Callback<List<Song>>() {
                    @Override
                    public void onResponse(Call<List<Song>> call, Response<List<Song>> response) {
                        List<Song> songs = response.body();
                        AppContext.database.songs().insert(songs);

                        swipeRefresh.setRefreshing(false);
                    }

                    @Override
                    public void onFailure(Call<List<Song>> call, Throwable t) {
                        swipeRefresh.setRefreshing(false);
                    }
                });
    }

    @Override
    public void onItemSongClick(Song song, int position) {
        PlayActivity.start(getActivity(), adapter.getSongs(), position);
    }

    @Override
    public void onRefresh() {
        getAllSongs();
    }

    private void initList(View root) {

        rvPlaylist = root.findViewById(R.id.rv_playlist);
        playlistAdapter = new PlayListAdapter();

        // LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvPlaylist.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        //  rvPlaylist.setLayoutManager(layoutManager);
        rvPlaylist.setAdapter(playlistAdapter);
        rvPlaylist.setHasFixedSize(true);

        new Thread(() -> {
            new ServiceManager.GETSearchPlaylist("queen", new ServiceManager.GETSearchPlaylist.OnResponseListener() {
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

                            playlistAdapter.setData(playlist);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    });
                }
            });
        }).start();
    }

}