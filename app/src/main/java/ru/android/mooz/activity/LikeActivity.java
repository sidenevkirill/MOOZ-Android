package ru.android.mooz.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import ru.android.mooz.R;

public class LikeActivity extends AppCompatActivity {
    ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like);
        initView();

    }

    private void initView() {
        btnBack = findViewById(R.id.btn_back);
        eventClick();
    }


    private void eventClick() {
        //click nút back
        btnBack.setOnClickListener(view -> finish());
    }
}
