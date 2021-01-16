package ru.android.mooz.adapter;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ru.android.mooz.R;
import ru.android.mooz.model.Song;

public class SongRecAdapter extends RecyclerView.Adapter<SongRecAdapter.ViewHolder> {

    private List<Song> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Activity activity;

    public SongRecAdapter(Activity activity, List<Song> data, ItemClickListener itemClickListener) {
        this.mInflater = LayoutInflater.from(activity);
        this.mData = data;
        this.activity = activity;
        this.mClickListener = itemClickListener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_top, parent, false);
        return new ViewHolder(view);
    }

    public List<Song> getSongs() {
        return mData;
    }

    public void setData() {
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Song song = mData.get(position);
        holder.tvName.setText(song.getNameSong());

        holder.tvNum.setText(String.valueOf(position + 1));

        holder.tvDes.setText(song.getSingerSong());
        Glide.with(activity)
                .load(song.getImageSong())
                .into(holder.ivSong);

//        holder.overflow.setOnClickListener(view -> createverflowMenu(holder.overflow, song));

        holder.itemView.setOnClickListener(v -> {
            if (mClickListener != null) mClickListener.onItemSongClick(song, position);
        });
    }

    private void download(Song song) {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), song.getNameSong() + ".mp3");

        DownloadManager dm = (DownloadManager) activity.getSystemService(Context.DOWNLOAD_SERVICE);
        String url =
                song.getLinkSong().trim();
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(
                url));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setTitle(song.getNameSong());
        request.setDescription("Загрузка аудио");
        request.setDestinationUri(Uri.fromFile(file));
        request.allowScanningByMediaScanner();
        request.setMimeType("audio/mpeg");

        System.out.println("url: " + url);
        dm.enqueue(request);
    }

    private void createverflowMenu(View v, Song song) {
        PopupMenu menu = new PopupMenu(v.getContext(), v);
        menu.inflate(R.menu.menu_main);

        menu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_download:
                    download(song);
            }
            return true;
        });
        menu.show();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public Song getItem(int id) {
        return mData.get(id);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivSong;
        TextView tvName, tvNum, tvDes;
        ImageButton overflow;

        ViewHolder(View itemView) {
            super(itemView);
            ivSong = itemView.findViewById(R.id.iv_ava);
            tvName = itemView.findViewById(R.id.tv_name);
            tvDes = itemView.findViewById(R.id.tv_des);
            tvNum = itemView.findViewById(R.id.tvNum);
            overflow = itemView.findViewById(R.id.settings_item);
        }
    }


    public interface ItemClickListener {
        void onItemSongClick(Song song, int position);
    }

    public void filterList(ArrayList<Song> filteredList) {
        mData = filteredList;
        notifyDataSetChanged();
    }


}




