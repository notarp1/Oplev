package com.A4.oplev;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import Controller.Controller;
import DTO.UserDTO;

public class Activity_Profile extends AppCompatActivity implements View.OnClickListener {
    public TextView about, city, desc, aboutName, job, edu;
    Controller controller;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        controller =  Controller.getInstance();

        about = findViewById(R.id.text_information);
        city = findViewById(R.id.text_city);
        desc = findViewById(R.id.text_description);
        aboutName = findViewById(R.id.text_about_name);
        edu = findViewById(R.id.text_edu);
        job = findViewById(R.id.text_job);

        controller.iniProfile(this);


    }

    @Override
    public void onClick(View v) {

    }
}