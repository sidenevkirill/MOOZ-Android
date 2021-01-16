package ru.android.mooz.model;

public class Track {
    private String id;
    private String title;
    private Artist artist;
    private int duration;
    private Album album;

    public Track() {
    }

    public Track(String id, String title, Artist artist, int duration, Album album) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.duration = duration;
        this.album = album;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }
}
