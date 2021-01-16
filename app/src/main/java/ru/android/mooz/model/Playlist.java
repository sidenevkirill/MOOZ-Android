package ru.android.mooz.model;

public class Playlist {

    private String id;
    private String title;
    private String description;
    private String picture_big;
    private int nb_tracks;
    private Creator creator;

    public Playlist() {
    }

    public Playlist(String id, String title, String description, String picture_big, int nb_tracks, Creator creator) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.picture_big = picture_big;
        this.nb_tracks = nb_tracks;
        this.creator = creator;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicture_big() {
        return picture_big;
    }

    public void setPicture_big(String picture_big) {
        this.picture_big = picture_big;
    }

    public int getNb_tracks() {
        return nb_tracks;
    }

    public void setNb_tracks(int nb_tracks) {
        this.nb_tracks = nb_tracks;
    }

    public Creator getCreator() {
        return creator;
    }

    public void setCreator(Creator creator) {
        this.creator = creator;
    }
}
