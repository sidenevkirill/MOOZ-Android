package ru.android.mooz.service;


import java.io.IOException;

import ru.android.mooz.utils.HTTPSWebUtil;

public class ServiceManager {

    public static final String SEARCH_PLAYLIST = "https://api.deezer.com/search/playlist?q=";
    public static final String SEARCH_PLAYLIST_URL = "https://raw.githubusercontent.com/sidenevkirill/MOOZ/master/";
    public static final String SEARCH_MUSIC = "http://api.deezer.com/search?q=";
    public static final String PLAYLIST_URL = "https://raw.githubusercontent.com/sidenevkirill/MOOZ/master/";
    public static final String TRACK_URL = "https://raw.githubusercontent.com/sidenevkirill/MOOZ/master/trackstracks";

    public static class SimpleGET {
        OnResponseListener listener;

        public SimpleGET(OnResponseListener listener) {
            this.listener = listener;
            HTTPSWebUtil util = new HTTPSWebUtil();
            try {
                String response = util.GETrequest(PLAYLIST_URL);
                listener.onResponse(response);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public interface OnResponseListener {
            void onResponse(String response);
        }
    }

    public static class GETSearchPlaylist {
        OnResponseListener listener;

        public GETSearchPlaylist(String search, OnResponseListener listener) {
            this.listener = listener;
            HTTPSWebUtil util = new HTTPSWebUtil();
            try {
                String response = util.GETrequest(SEARCH_PLAYLIST_URL + search);
                listener.onResponse(response);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public interface OnResponseListener {
            void onResponse(String response);
        }
    }

    public static class GETSearchSong {
        OnResponseListener listener;

        public GETSearchSong(String search, OnResponseListener listener) {
            this.listener = listener;
            HTTPSWebUtil util = new HTTPSWebUtil();
            try {
                String response = util.GETrequest(SEARCH_PLAYLIST_URL + search);
                listener.onResponse(response);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public interface OnResponseListener {
            void onResponse(String response);
        }
    }


    public static class GETPlaylist {
        OnResponseListener listener;

        public GETPlaylist(String id, OnResponseListener listener) {
            this.listener = listener;
            HTTPSWebUtil util = new HTTPSWebUtil();
            try {
                String response = util.GETrequest(PLAYLIST_URL + id);
                listener.onResponse(response);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public interface OnResponseListener {
            void onResponse(String response);
        }
    }

    public static class GETTrack {
        OnResponseListener listener;

        public GETTrack(String id, OnResponseListener listener) {
            this.listener = listener;
            HTTPSWebUtil util = new HTTPSWebUtil();
            try {
                String response = util.GETrequest(TRACK_URL + id);
                listener.onResponse(response);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public interface OnResponseListener {
            void onResponse(String response);
        }
    }

}
