package com.A4.oplev;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import DTO.UserDTO;

public class Activity_Profile extends AppCompatActivity implements View.OnClickListener {
    TextView about, city, desc, aboutName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Intent intent = getIntent();
        UserDTO user = (UserDTO) intent.getSerializableExtra("user");


        about = findViewById(R.id.text_information);
        city = findViewById(R.id.text_city);
        desc = findViewById(R.id.text_description);
        aboutName = findViewById(R.id.text_about_name);


        String aboutText = user.getfName() + ", " + user.getAge();
        String cityText = "\uD83D\uDCCD " + user.getCity();
        String descText = user.getDescription();
        String aboutNameText = "Om "+ user.getfName();

        about.setText(aboutText);
        city.setText(cityText);
        desc.setText(descText);
        aboutName.setText(aboutNameText);


    }

    @Override
    public void onClick(View v) {

    }
}