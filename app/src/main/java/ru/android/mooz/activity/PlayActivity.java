package ru.android.mooz.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.StrictMode;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ru.android.mooz.R;
import ru.android.mooz.model.Song;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import am.appwise.components.ni.NoInternetDialog;

public class PlayActivity extends AppCompatActivity {
    NoInternetDialog noInternetDialog;
    Button btnPlay, btnNext, btnPre, btnRefesh, btnSuffle, btnStop;
    ImageView imgSong, btnBack;
    TextView tvName, tvNameGroup;
    SeekBar positionBar;
    String gsonlistSong;
    TextView elapsedTimeLabel;
    TextView remainingTimeLabel;
    MediaPlayer mediaPlayer;
    ArrayList<Song> songList = new ArrayList<>();
    int position = 0;
    boolean repeat = false;
    boolean checkrandom = false;
    boolean next = false;
    boolean isStop = false;

    public static void start(Context context, List<Song> songs, int position) {
        Intent starter = new Intent(context, PlayActivity.class);
        starter.putExtra("songs", new Gson().toJson(songs));
        starter.putExtra("position", position);
        context.startActivity(starter);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(threadPolicy);

        TypeToken<List<Song>> token = new TypeToken<List<Song>>() {};
        songList = new Gson().fromJson(getIntent().getStringExtra("songs"), token.getType());
        position = getIntent().getIntExtra("position", 0);
        initView();
    }

    //ánh xạ các view
    private void initView() {
        btnPlay = findViewById(R.id.btnPlay);
        btnNext = findViewById(R.id.btnNext);
        btnPre = findViewById(R.id.btnPre);
        btnStop = findViewById(R.id.btnStop);
        btnRefesh = findViewById(R.id.btnAround);
        btnSuffle = findViewById(R.id.btnSuffer);
        btnBack = findViewById(R.id.btn_back);
        imgSong = findViewById(R.id.img_ava);
        elapsedTimeLabel = findViewById(R.id.elapsedTimeLabel);
        remainingTimeLabel = findViewById(R.id.remainingTimeLabel);
        tvName = findViewById(R.id.tv_name);
        tvNameGroup = findViewById(R.id.tv_name_group);
        // Position Bar
        positionBar = findViewById(R.id.positionBar);
//        for (int i = 0; i < songList.size(); i++) {
//            if (songList.get(i).getPlay()) {
//                position = i;
//                break;
//            }
//        }
/*        noInternetDialog = new NoInternetDialog.Builder(PlayActivity.this).build();
        noInternetDialog.setOnDismissListener(dialogInterface -> {
            if (mediaPlayer == null) {
                Glide.with(this)
                        .load(songList.get(position).getImageSong())
                        .into(imgSong);
                PlaySongs(songList.get(position).getLinkSong());
                eventClick();
            }

        });*/
        // tải ảnh nền của bài hát
        Glide.with(this)
                .load(songList.get(position).getImageSong())
                .into(imgSong);
        tvName.setText(songList.get(position).getNameSong());
        tvNameGroup.setText(songList.get(position).getNameSong());
        btnPlay.setBackgroundResource(R.drawable.pause);
        btnStop.setClickable(false);
        // khởi tạo mediapayer
        PlaySongs(songList.get(position).getLinkSong());
        //bắt sự kiện khi click các nút
        eventClick();


    }

    private void eventClick() {
        //click nút back
        btnBack.setOnClickListener(view -> finish());
        //click nút play
        btnPlay.setOnClickListener(view -> {
            if (!isStop) {
                PlaySongs(songList.get(position).getLinkSong());
                btnPlay.setBackgroundResource(R.drawable.pause);
            } else {
                if (mediaPlayer.isPlaying()) {
                    // pause nếu đang play
                    mediaPlayer.pause();
                    btnPlay.setBackgroundResource(R.drawable.play);
                } else {
                    //play nếu đang pause
                    mediaPlayer.start();
                    btnPlay.setBackgroundResource(R.drawable.pause);
                }
            }

        });
        //click nút stop
        btnStop.setOnClickListener(view -> {
            if (isStop) {
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = null;
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
                    elapsedTimeLabel.setText(simpleDateFormat.format(0));
                    positionBar.setProgress(0);
                    btnStop.setBackgroundResource(R.drawable.stops);
                    btnPlay.setBackgroundResource(R.drawable.play);
                    btnStop.setClickable(false);
                    isStop = false;
                }
            }
        });
        //click nút lặp lại
        btnRefesh.setOnClickListener(view -> {
            if (!repeat) {
                if (checkrandom) {
                    checkrandom = false;
                    btnRefesh.setBackgroundResource(R.drawable.refresh);
                    btnSuffle.setBackgroundResource(R.drawable.shuffle_gray);
                }
                btnRefesh.setBackgroundResource(R.drawable.refresh_gray);
                repeat = true;
            } else {
                btnRefesh.setBackgroundResource(R.drawable.refresh);
                repeat = false;
            }
        });
        // click nút đảo bài
        btnSuffle.setOnClickListener(view -> {

      /*      String[] projetion = {SongsFavoriteTable.ID_PROVIDER};
            if (new PlayActivity().getContentResolver().query(Uri.parse(SongsFavoriteTable.TABLE_NAME), projetion,
                    SongsFavoriteTable.ID_PROVIDER + " = ?",
                    new String[]{String.valueOf(tvName)}, null) != null) {
                btnRefesh.setBackgroundResource(R.drawable.heart_outline);
            } else {
                btnRefesh.setBackgroundResource(R.drawable.search);
            } */

            if (!checkrandom) {
                if (repeat) {
                    repeat = false;
                    btnRefesh.setBackgroundResource(R.drawable.refresh_gray);
                    btnSuffle.setBackgroundResource(R.drawable.shuffle);
                }
                btnSuffle.setBackgroundResource(R.drawable.shuffle);
                checkrandom = true;
            } else {
                btnSuffle.setBackgroundResource(R.drawable.shuffle_gray);
                checkrandom = false;
            }
        });
        // click nút next
        btnNext.setOnClickListener(view -> {
            if (songList.size() > 0) {
                if (mediaPlayer != null) {
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        mediaPlayer = null;
                    }
                }
                if (position < songList.size()) {
                    btnPlay.setBackgroundResource(R.drawable.pause);
                    position++;
                    if (repeat) {
                        if (position == 0) {
                            position = songList.size();
                        }
                        position -= 1;
                    }
                    if (checkrandom) {
                        Random random = new Random();
                        int iRandom = random.nextInt(songList.size());
                        if (iRandom == position) {
                            position = iRandom - 1;
                        }
                        position = iRandom;
                    }
                    if (position > songList.size() - 1) {
                        position = 0;
                    }
                    Glide.with(this)
                            .load(songList.get(position).getImageSong())
                            .into(imgSong);
                    tvName.setText(songList.get(position).getNameSong());
                    tvNameGroup.setText(songList.get(position).getNameSong());
                    btnPlay.setBackgroundResource(R.drawable.pause);
                    PlaySongs(songList.get(position).getLinkSong());
                    updateTime();
                }
            }

            btnPre.setClickable(false);
            btnNext.setClickable(false);
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                btnPre.setClickable(true);
                btnNext.setClickable(true);
            }, 5000);
        });
        // click nút previous
        btnPre.setOnClickListener(view -> {
            if (songList.size() > 0) {
                if (mediaPlayer != null) {
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        mediaPlayer = null;
                    }
                }
                if (position < songList.size()) {
                    btnPlay.setBackgroundResource(R.drawable.pause);
                    position--;
                    if (position < 0) {
                        position = songList.size() - 1;
                    }
                    if (repeat) {
                        position += 1;
                    }
                    if (checkrandom) {
                        Random random = new Random();
                        int iRandom = random.nextInt(songList.size());
                        if (iRandom == position) {
                            position = iRandom - 1;
                        }
                        position = iRandom;
                    }
                    Glide.with(this)
                            .load(songList.get(position).getImageSong())
                            .into(imgSong);
                    tvName.setText(songList.get(position).getNameSong());
                    tvNameGroup.setText(songList.get(position).getNameSong());
                    btnPlay.setBackgroundResource(R.drawable.pause);
                    PlaySongs(songList.get(position).getLinkSong());
                    updateTime();
                }
            }

            btnPre.setClickable(false);
            btnNext.setClickable(false);
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                btnPre.setClickable(true);
                btnNext.setClickable(true);
            }, 5000);
        });

        positionBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) mediaPlayer.stop();
        songList.clear();
    }

    private void PlaySongs(String linkSong) {
        try {
            // tạo mới mediaplayer
            mediaPlayer = new MediaPlayer();
            //truyền streamType cho mp
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            //khi mp hoàn thành thì xóa đi để khởi tạo mp mới
            mediaPlayer.setOnCompletionListener(mediaPlayer -> {
                mediaPlayer.stop();
                mediaPlayer.reset();
            });
            //truyền url bài hát vào mp
            mediaPlayer.setDataSource(linkSong);
            // load bài hát
            mediaPlayer.prepareAsync();
            btnStop.setBackgroundResource(R.drawable.stops);
            btnStop.setClickable(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //listener khi load xong bài hát
        mediaPlayer.setOnPreparedListener(mediaPlayer1 -> {
            btnStop.setClickable(true);
            btnStop.setBackgroundResource(R.drawable.stops);
            isStop = true;
            // phát bài hát
            mediaPlayer.start();
            //set time bài hát vào positionBar
            timeSong();
            //xử lý chạy positionBar và thời gian khi đang phát
            updateTime();
        });
    }

    private void timeSong() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        remainingTimeLabel.setText(simpleDateFormat.format(mediaPlayer.getDuration()));
        positionBar.setMax(mediaPlayer.getDuration());
    }

    private void updateTime() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null) {
                    positionBar.setProgress(mediaPlayer.getCurrentPosition());
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
                    elapsedTimeLabel.setText(simpleDateFormat.format(mediaPlayer.getCurrentPosition()));
                    handler.postDelayed(this, 300);
                    mediaPlayer.setOnCompletionListener(mediaPlayer -> {
                        next = true;
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        }, 300);
        Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                // nếu next set lại position, icon nút
                if (next) {
                    if (songList.size() > 0) {
                        if (position < songList.size()) {
                            btnPlay.setBackgroundResource(R.drawable.pause);
                            position++;
                            if (repeat) {
                                if (position == 0) {
                                    position = songList.size();
                                }
                                position -= 1;
                            }
                            if (checkrandom) {
                                Random random = new Random();
                                int iRandom = random.nextInt(songList.size());
                                if (iRandom == position) {
                                    position = iRandom - 1;
                                }
                                position = iRandom;
                            }
                            if (position > songList.size() - 1) {
                                position = 0;
                            }
                            Glide.with(PlayActivity.this)
                                    .load(songList.get(position).getImageSong())
                                    .into(imgSong);
                            tvName.setText(songList.get(position).getNameSong());
                            tvNameGroup.setText(songList.get(position).getNameSong());
                            btnPlay.setBackgroundResource(R.drawable.pause);
                            PlaySongs(songList.get(position).getLinkSong());
                        }
                    }

                    btnPre.setClickable(false);
                    btnNext.setClickable(false);
                    Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        btnPre.setClickable(true);
                        btnNext.setClickable(true);
                    }, 5000);
                    next = false;
                    handler1.removeCallbacks(this);
                } else {
                    handler1.postDelayed(this, 1000);
                }
            }
        }, 1000);
    }
}