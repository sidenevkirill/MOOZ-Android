package ru.android.mooz.model;

public class Album {
    private String id;
    private String title;
    private String cover_medium;

    public Album() {
    }

    public Album(String id, String title, String cover_medium) {
        this.id = id;
        this.title = title;
        this.cover_medium = cover_medium;
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

    public String getCover_medium() {
        return cover_medium;
    }

    public void setCover_medium(String cover_medium) {
        this.cover_medium = cover_medium;
    }
}
