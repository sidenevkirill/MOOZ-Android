package ru.android.mooz.model;

public class SearchPlaylist {

    private String id;
    private String title;
    private User user;
    private int nb_tracks;
    private String picture_small;

    public SearchPlaylist() {
    }

    public SearchPlaylist(String id, String title, int nb_tracks, String picture_small, User user) {
        this.id = id;
        this.title = title;
        this.user = user;
        this.nb_tracks = nb_tracks;
        this.picture_small = picture_small;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getNb_tracks() {
        return nb_tracks;
    }

    public void setNb_tracks(int nb_tracks) {
        this.nb_tracks = nb_tracks;
    }

    public String getPicture_small() {
        return picture_small;
    }

    public void setPicture_small(String picture_small) {
        this.picture_small = picture_small;
    }
}