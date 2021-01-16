package ru.android.mooz;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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

import java.util.ArrayList;
import java.util.List;

import am.appwise.components.ni.NoInternetDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.android.mooz.activity.PlayActivity;
import ru.android.mooz.adapter.AlbumAdapter;
import ru.android.mooz.adapter.PlayListAdapter;
import ru.android.mooz.adapter.ReccomendsAdapter;
import ru.android.mooz.adapter.SongAdapter;
import ru.android.mooz.adapter.SongRecAdapter;
import ru.android.mooz.fragment.RecommendationFragment;
import ru.android.mooz.fragment.TopChartFragment;
import ru.android.mooz.model.SearchPlaylist;
import ru.android.mooz.model.Song;
import ru.android.mooz.model.Users;
import ru.android.mooz.service.ServiceManager;
import ss.com.bannerslider.Slider;

public class RecicleActivity extends Fragment implements SongRecAdapter.ItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    private RelativeLayout titlePlay;
    private SongRecAdapter songRecAdapter;
    private SongAdapter songAdapter;
    private RecyclerView recycler;
    private SwipeRefreshLayout swipeRefresh;
    private NoInternetDialog noInternetDialog;
    private RecyclerView rvPlaylist;
    private RecyclerView toPlaylist;
    private PlayListAdapter playlistAdapter;
    private ReccomendsAdapter reccomendsAdapter;
    private AlbumAdapter albumAdapter;
    CircleImageView profile_image;
    TextView username;

    private Slider slider;
    ImageView ivImageFromUrl;
    FirebaseUser firebaseUser;
    DatabaseReference reference;

    private static final String TAG = "RecicleActivity";

    //vars
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();


    public static RecicleActivity newInstance() {
        Bundle args = new Bundle();

        RecicleActivity fragment = new RecicleActivity();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_recicle, container, false);
        initList(root);
        initView(root);
        initArt(root);
//        initAlbum(root);
        //      it(root);

        Button newPage = (Button) root.findViewById(R.id.btnOpen);
        newPage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
              //  CategoriesFragment nextFrag = new CategoriesFragment();
                RecommendationFragment nextFrag = new RecommendationFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, nextFrag, "findThisFragment")
                        .addToBackStack(null)
                        .commit();
            }

        });

        Button ChartPage = (Button) root.findViewById(R.id.top_chart);
        ChartPage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                TopChartFragment nextFrag = new TopChartFragment();
                //   RecicleActivity nextFrag= new RecicleActivity();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, nextFrag, "findThisFragment")
                        .addToBackStack(null)
                        .commit();
            }

        });

        Button RecPage = (Button) root.findViewById(R.id.btn);
        RecPage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                TopChartFragment nextFrag = new TopChartFragment();
                //   RecicleActivity nextFrag= new RecicleActivity();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, nextFrag, "findThisFragment")
                        .addToBackStack(null)
                        .commit();
            }

        });

        Button HitPage = (Button) root.findViewById(R.id.bt);
        HitPage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                TopChartFragment nextFrag = new TopChartFragment();
                //   RecicleActivity nextFrag= new RecicleActivity();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, nextFrag, "findThisFragment")
                        .addToBackStack(null)
                        .commit();
            }

        });


        return root;

    }

  /*  private void it(View root) {

    ivImageFromUrl = (ImageView) root.findViewById(R.id.single_playlist_image);

    Picasso.with(getContext())
            .load("https://images.unsplash.com/photo-1483412033650-1015ddeb83d1?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1953&q=80")
            .into(ivImageFromUrl);

    }*/

    private void initView(View root) {
        titlePlay = root.findViewById(R.id.title_play);
        recycler = root.findViewById(R.id.first_recycler_view);
        profile_image = root.findViewById(R.id.profile_image);
        username = root.findViewById(R.id.username);


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users user = dataSnapshot.getValue(Users.class);
                //        username.setText(user.getUsername());
                if (user.getImageURL().equals("default")) {
                    profile_image.setImageResource(R.drawable.person_image_empty);
                } else {
                    Glide.with(getContext()).load(user.getImageURL()).into(profile_image);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recycler.setLayoutManager(layoutManager);
        recycler.setNestedScrollingEnabled(false);
        recycler.setHasFixedSize(true);

        // диалоговое окно
        noInternetDialog = new NoInternetDialog.Builder(getActivity()).build();
        noInternetDialog.setOnDismissListener(dialogInterface -> {
            // Когда сеть будет доступна, перезагрузить API
            if (songRecAdapter == null) getAllSongs();
        });

        getCachedSongs();
        getAllSongs();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        noInternetDialog.onDestroy();
    }

    private void createAdapter(List<Song> songs) {
        songRecAdapter = new SongRecAdapter(getActivity(), songs, RecicleActivity.this);
        recycler.setAdapter(songRecAdapter);
    }

    private void getCachedSongs() {
        AppContext.database.songs().get()
                .observe(this, this::createAdapter);
    }

    private void getAllSongs() {
//        swipeRefresh.setRefreshing(true);
        AppContext.api.listSongAll()
                .enqueue(new Callback<List<Song>>() {
                    @Override
                    public void onResponse(Call<List<Song>> call, Response<List<Song>> response) {
                        List<Song> songs = response.body();
                        AppContext.database.songs().insert(songs);

//                        swipeRefresh.setRefreshing(false);
                    }

                    @Override
                    public void onFailure(Call<List<Song>> call, Throwable t) {
//                        swipeRefresh.setRefreshing(false);
                    }
                });
    }

    @Override
    public void onItemSongClick(Song song, int position) {
        PlayActivity.start(getActivity(), songRecAdapter.getSongs(), position);
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

    private void initArt(View root) {
        rvPlaylist = root.findViewById(R.id.rv);
        reccomendsAdapter = new ReccomendsAdapter();

        // LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvPlaylist.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        //  rvPlaylist.setLayoutManager(layoutManager);
        rvPlaylist.setAdapter(reccomendsAdapter);
        rvPlaylist.setHasFixedSize(true);

        new Thread(() -> {
            new ServiceManager.GETSearchPlaylist("artist", new ServiceManager.GETSearchPlaylist.OnResponseListener() {
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

                            reccomendsAdapter.setData(playlist);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    });
                }
            });
        }).start();
    }

/*    private void initAlbum(View root) {
        rvPlaylist = root.findViewById(R.id.rv_album);
        albumAdapter = new AlbumAdapter();

        // LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvPlaylist.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        //rvPlaylist.setLayoutManager(new GridLayoutManager(getContext(), 2));
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
    }*/

}