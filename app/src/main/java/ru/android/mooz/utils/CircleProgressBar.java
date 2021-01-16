package ru.android.mooz.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.Window;

import ru.android.mooz.R;


public class CircleProgressBar {
    public Activity activity;
    public static CircleProgressBar instance;
    private String TAG = CircleProgressBar.class.getName();
    private Runnable runnable;
    private Handler handler;
    private long timer = 20000;


    public CircleProgressBar(Activity activity) {
        this.activity = activity;
        runnable = this::dismissProgress;
        handler = new Handler();
    }


    public static CircleProgressBar newInstance(Activity activity) {
        return new CircleProgressBar(activity);
    }

    public static CircleProgressBar getInstance(Activity activity) {
        if (instance != null) {
            return instance;
        } else return newInstance(activity);
    }

    public static ProgressDialog progressBar;

    public void showProgress() {
        try {
            handler.postDelayed(runnable, timer);
            if (activity == null) {
                Log.d(TAG, "showProgress: Cannot show progress because activity null");
                return;
            }
            if (progressBar == null) {
                        progressBar = new ProgressDialog(activity, R.style.infinite_progress_horizontal);

            }
            Window window = progressBar.getWindow();
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressBar.setIndeterminate(true);
            progressBar.setCancelable(false);
            progressBar.setTitle(null);
            progressBar.setMessage(null);
            progressBar.setProgress(0);
            progressBar.setMax(100);
            try {
                Log.d(TAG, "showProgress: almost true");
                if (progressBar.isShowing()) return;
                progressBar.show();
                Log.d(TAG, "showProgress: true");
            } catch (Exception e) {
                e.printStackTrace();
            }

            progressBar.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        dismissProgress();
                    }
                    return keyCode == KeyEvent.KEYCODE_BACK;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Error at:" + e.getMessage());
        }
    }

    public void dismissProgress() {
        try {
            if (progressBar != null && progressBar.isShowing()) {
                progressBar.dismiss();
            }
            progressBar = null;
            handler.removeCallbacks(runnable);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Error at:" + e.getMessage());
        }
    }

    public boolean isShowing() {
        if (progressBar != null)
            return progressBar.isShowing();
        else return false;
    }

}
