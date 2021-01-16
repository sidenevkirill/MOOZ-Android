package ru.android.mooz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import ru.android.mooz.R;

public class TestActivity extends AppCompatActivity {

    String EmailHolder;
    TextView Email;
    Button LogOUT ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        Email = (TextView)findViewById(R.id.textView1);
        LogOUT = (Button)findViewById(R.id.button1);

        Intent intent = getIntent();

        // Receiving User Email Send By MainActivity.

        // Setting up received email to TextView.
        Email.setText(Email.getText().toString()+ EmailHolder);

        // Adding click listener to Log Out button.
        LogOUT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Finishing current DashBoard activity on button click.
                finish();

                Toast.makeText(TestActivity.this,"Log Out Successful", Toast.LENGTH_LONG).show();

            }
        });

    }
}