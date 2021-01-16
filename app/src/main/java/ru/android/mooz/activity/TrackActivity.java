package ru.android.mooz.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import ru.android.mooz.R;
import ru.android.mooz.model.Track;
import ru.android.mooz.service.ServiceManager;

public class TrackActivity extends AppCompatActivity implements BillingProcessor.IBillingHandler {

    private ImageView ivCover;
    private TextView etTitle;
    private TextView etAuthor;
    private TextView etAlbum;
    private TextView etDuration;

    private Button btnPlay;

    BillingProcessor bp;
    Button purchaseBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);

        bp = new BillingProcessor(this, "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAlW14Ekm1IsJie+D/67nFRNdaA3trLdSeyQsZtPnie6xF0d6IteLEJY9yPBYcB9Ryet+Nj9Vpgr7j3YdYipEZNeND+PgQtVqTlV1gM4F0p7ZuC8H6qKe4rkJ3eUoffmdO2RwHl9iV7DIz/wqx5OXfwEeCBc2fZzPZgF1tFSA8+WTPNtKvW79H3wwVnp+6GiH7UvE+/EMgWHKhQBMwqCczXwciUiwpTXNvpKl6G3h1uX5wI9owMYzLrCAHzznVa/GvKrhsjRwo5ThFyGv4DXQE+3hw7RKKorwkp0PyPnCapUhBes1ciMUotTpHos9Kwb5ICo7ItU1w8rt7nwUDKgsSKQIDAQAB", this);
        bp.initialize();


        ivCover = findViewById(R.id.track_image);
        etTitle = findViewById(R.id.track_title);
    //    etAuthor = findViewById(R.id.track_author);
        etAlbum = findViewById(R.id.track_album);
    //    etDuration = findViewById(R.id.track_duration);

        btnPlay = findViewById(R.id.btn_play);

        if (getIntent().hasExtra("id")) {
            String id = getIntent().getStringExtra("id");
            new Thread(() -> {
                new ServiceManager.GETTrack(id, new ServiceManager.GETTrack.OnResponseListener() {
                    @Override
                    public void onResponse(String response) {
                        runOnUiThread(() -> {

                            JSONObject json = null;    // create JSON obj from string
                            try {
                                json = new JSONObject(response);


                                String track_json = json.toString();

                                Track track = new Gson().fromJson(track_json, new TypeToken<Track>() {
                                }.getType());

                                etTitle.setText(track.getTitle());
                                etAlbum.setText(track.getAlbum().getTitle());
//                                etAuthor.setText(track.getArtist().getName());
//                                etDuration.setText(track.getDuration() + "");

                                btnPlay.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        bp.purchase(TrackActivity.this, "15_coin");
                                        bp.consumePurchase("15_coin");
                                    }
                                });

                                Glide.with(TrackActivity.this).load(track.getAlbum().getCover_medium()).into(ivCover);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        });
                    }
                });
            }).start();
        }

    }

    public void bay(View view) {
        bp.purchase(TrackActivity.this, "15_coin");
        bp.consumePurchase("15_coin");
    }

    @Override
    public void onProductPurchased(String productId, TransactionDetails details) {

    }

    @Override
    public void onPurchaseHistoryRestored() {

    }

    @Override
    public void onBillingError(int errorCode, Throwable error) {

    }

    @Override
    public void onBillingInitialized() {

    }
}
