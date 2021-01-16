package ru.android.mooz.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.List;

import am.appwise.components.ni.NoInternetDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.android.mooz.AppContext;
import ru.android.mooz.R;
import ru.android.mooz.activity.PlayActivity;
import ru.android.mooz.adapter.SongAdapter;
import ru.android.mooz.model.Song;

public class PopularFragment extends Fragment implements SongAdapter.ItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    private RelativeLayout titlePlay;
    private SongAdapter adapter;
    private RecyclerView recycler;
    private SwipeRefreshLayout swipeRefresh;
    private NoInternetDialog noInternetDialog;

    public static AudiosFragment newInstance() {
        Bundle args = new Bundle();

        AudiosFragment fragment = new AudiosFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_popular, container, false);

        initView(root);
        return root;
    }

    private void initView(View root) {
        titlePlay = root.findViewById(R.id.title_play);
        recycler = root.findViewById(R.id.rcv_list_song);
        swipeRefresh = root.findViewById(R.id.refresh);
        swipeRefresh.setColorSchemeColors(
                Color.BLACK, Color.RED, Color.BLUE, Color.YELLOW
        );
        swipeRefresh.setOnRefreshListener(this);

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
        adapter = new SongAdapter(getActivity(), songs, PopularFragment.this);
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

}