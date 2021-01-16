package ru.android.mooz.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import ru.android.mooz.R;
import ru.android.mooz.activity.ArtistListActivity;
import ru.android.mooz.model.SearchPlaylist;

public class ArtistAdapter extends RecyclerView.Adapter<PlayListAdapter.CustomViewHolder> {

    ArrayList<SearchPlaylist> data;

    public static class CustomViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout root;

        public CustomViewHolder(LinearLayout v) {
            super(v);
            root = v;
        }
    }

    public void showAllFriends(ArrayList<SearchPlaylist> allFriends) {
        for (int i = 0; i < allFriends.size(); i++) {
            if (!data.contains(allFriends.get(i))) data.add(allFriends.get(i));
        }
        notifyDataSetChanged();
    }

    public void setData(ArrayList<SearchPlaylist> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public void addPlayList(SearchPlaylist playList) {
        data.add(playList);
        notifyDataSetChanged();
    }

    public ArtistAdapter() {
        data = new ArrayList<>();
    }

    @Override
    public PlayListAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_artist, parent, false);
        PlayListAdapter.CustomViewHolder vh = new PlayListAdapter.CustomViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(PlayListAdapter.CustomViewHolder holder, final int position) {
        ((TextView) holder.root.findViewById(R.id.playlist_name)).setText(data.get(position).getTitle());
        ((TextView) holder.root.findViewById(R.id.playlist_total)).setText(data.get(position).getUser().getName());
  //      ((TextView) holder.root.findViewById(R.id.playlist_total)).setText(data.get(position).getNb_tracks() + "");
        Glide.with(holder.root.getContext()).load(data.get(position).getPicture_small()).into((ImageView) holder.root.findViewById(R.id.playlist_image));
        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.root.getContext(), ArtistListActivity.class);
                intent.putExtra("id", data.get(position).getId());
                holder.root.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

}

