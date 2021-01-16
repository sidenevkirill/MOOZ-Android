package ru.android.mooz.model;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity(tableName = "songs")
public class Song implements Serializable {
    private static final long serialVersionUID = 1L;

    @PrimaryKey
    @SerializedName("id_song")
    @Expose
    private int idSong;
    @SerializedName("id_album")
    @Expose
    private String idAlbum;
    @SerializedName("id_category")
    @Expose
    private String idCategory;
    @SerializedName("id_playlist")
    @Expose
    private String idPlaylist;
    @SerializedName("name_song")
    @Expose
    private String nameSong;
    @SerializedName("image_song")
    @Expose
    private String imageSong;
    @SerializedName("singer_song")

    @Expose
    private String singerSong;
    @SerializedName("link_song")
    @Expose
    private String linkSong;
    @SerializedName("num_song")
    @Expose
    private String numSong;

    private boolean isPlay = false;

    public Song() {

    }

    public int getIdSong() {
        return idSong;
    }

    public void setIdSong(int idSong) {
        this.idSong = idSong;
    }

    public String getIdAlbum() {
        return idAlbum;
    }

    public void setIdAlbum(String idAlbum) {
        this.idAlbum = idAlbum;
    }

    public String getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(String idCategory) {
        this.idCategory = idCategory;
    }

    public String getIdPlaylist() {
        return idPlaylist;
    }

    public void setIdPlaylist(String idPlaylist) {
        this.idPlaylist = idPlaylist;
    }

    public String getNameSong() {
        return nameSong;
    }

    public String num_song() {
        return numSong;
    }

    public void setNameSong(String nameSong) {
        this.nameSong = nameSong;
    }

    public String getImageSong() {
        return imageSong;
    }

    public void setImageSong(String imageSong) {
        this.imageSong = imageSong;
    }


    public String getNumSong() {
        return numSong;
    }

    public void setNumSong(String numSong) {
        this.imageSong = imageSong;
    }


    public String getSingerSong() {
        return singerSong;
    }

    public void setSingerSong(String singerSong) {
        this.singerSong = singerSong;
    }

    public String getLinkSong() {
        return linkSong;
    }

    public void setLinkSong(String linkSong) {
        this.linkSong = linkSong;
    }

    public Boolean getPlay() {
        return isPlay;
    }

    public void setPlay(Boolean play) {
        isPlay = play;
    }
}
