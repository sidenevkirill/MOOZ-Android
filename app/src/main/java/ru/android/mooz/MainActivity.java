package ru.android.mooz;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;
import ru.android.mooz.activity.ArtistActivity;
import ru.android.mooz.fragment.AccountsFragment;
import ru.android.mooz.fragment.AlbumFragment;
import ru.android.mooz.fragment.AudiosFragment;
import ru.android.mooz.fragment.NewFragment;
import ru.android.mooz.fragment.PopularFragment;
import ru.android.mooz.fragment.SearchFragment;


public class MainActivity extends AppCompatActivity {
    protected Toolbar toolbar;
    protected LayoutInflater inflater;
    Fragment selectedFragment = null;
    private AudiosFragment audiosFragment;
    private AlbumFragment albumFragment;

    CircleImageView profile_image;
    TextView username;

    FirebaseUser firebaseUser;
    DatabaseReference reference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

/*        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        profile_image = findViewById(R.id.profile_image);
        username = findViewById(R.id.username);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users user = dataSnapshot.getValue(Users.class);
        //        username.setText(user.getUsername());
                if (user.getImageURL().equals("default")) {
                    profile_image.setImageResource(R.mipmap.ic_launcher);
                } else {
                    Glide.with(getApplicationContext()).load(user.getImageURL()).into(profile_image);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/

        audiosFragment = new AudiosFragment();
        albumFragment = new AlbumFragment();

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                item -> {
                    switch (item.getItemId()) {
                        case R.id.navigation_home:
                            changeFragment(new RecicleActivity());
                            //   changeFragment(AudiosFragment.newInstance());
                            break;
                        case R.id.navigation_dashboard:
                            changeFragment(new NewFragment());
                            break;
                        case R.id.navigation_album:
                            changeFragment(new AlbumFragment());
                            break;
                        case R.id.navigation_notifications:
                            //Intent intent = new Intent(this, ActivityProfile.class);
                            //startActivity(intent);
                            //   changeFragment(new RecicleActivity());
                            changeFragment(new SearchFragment());
                            break;
                    }
                    return true;
                });

        changeFragment(new RecicleActivity());
        //changeFragment(AudiosFragment.newInstance());
    }

    private void changeFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit();

    }

    public void onClick(View v) {
        PopularFragment nextFrag = new PopularFragment();
        //   RecicleActivity nextFrag= new RecicleActivity();
        MainActivity.this.getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, nextFrag, "findThisFragment")
                .addToBackStack(null)
                .commit();
    }

    public void onProfile(View v) {
        AccountsFragment nextFrag = new AccountsFragment();
        //   RecicleActivity nextFrag= new RecicleActivity();
        MainActivity.this.getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, nextFrag, "findThisFragment")
                .addToBackStack(null)
                .commit();
    }

    public void onAtist(View v) {
        Intent intent = new Intent(MainActivity.this, ArtistActivity.class);
        startActivity(intent);
    }

    public void onAlbum(View v) {
        AlbumFragment nextFrag = new AlbumFragment();
        MainActivity.this.getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, nextFrag, "findThisFragment")
                .addToBackStack(null)
                .commit();
    }

/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case  R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                return true;

        }
        return false;
    }

    */

}
