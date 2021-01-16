package ru.android.mooz.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ru.android.mooz.R;
import ru.android.mooz.activity.TrackActivity;
import ru.android.mooz.model.Track;


public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.CustomViewHolder> {

    ArrayList<Track> data;

    public static class CustomViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout root;

        public CustomViewHolder(LinearLayout v) {
            super(v);
            root = v;
        }
    }

    public void setData(ArrayList<Track> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public void addPlayList(Track track) {
        data.add(track);
        notifyDataSetChanged();
    }

    public TrackAdapter() {
        data = new ArrayList<>();
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_playlist, parent, false);
        CustomViewHolder vh = new CustomViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, final int position) {
        ((TextView) holder.root.findViewById(R.id.playlist_name)).setText(data.get(position).getTitle());
        //((TextView) holder.root.findViewById(R.id.playlist_creator)).setText(data.get(position).);
        ((TextView) holder.root.findViewById(R.id.playlist_total)).setText(data.get(position).getDuration() + "");
//        Glide.with(holder.root.getContext()).load(data.get(position).getAlbum().getCover_medium()).into((ImageView) holder.root.findViewById(R.id.playlist_image));
        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.root.getContext(), TrackActivity.class);
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
