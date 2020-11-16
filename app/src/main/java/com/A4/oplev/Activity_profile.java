package com.A4.oplev;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Activity_profile extends AppCompatActivity implements View.OnClickListener {
    TextView about, city, desc, aboutName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        about = findViewById(R.id.text_information);
        city = findViewById(R.id.text_city);
        desc = findViewById(R.id.text_description);
        aboutName = findViewById(R.id.text_about_name);

        Intent intent = getIntent();
        String aboutText = intent.getStringExtra("fName")+", " + intent.getStringExtra("age");
        about.setText(aboutText);

        String cityText = intent.getStringExtra("city");
        city.setText(cityText);

        String descText = intent.getStringExtra("desc");
        desc.setText(descText);

        String aboutNameText = "Om "+ intent.getStringExtra("fName");
        aboutName.setText(aboutNameText);


    }

    @Override
    public void onClick(View v) {

    }
}