package com.example.oplev;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class Activity_Main extends AppCompatActivity implements View.OnClickListener {

    ImageView options, match, user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        options = findViewById(R.id.options_btn);
        match = findViewById(R.id.match_btn);
        user = findViewById(R.id.user_btn);


        options.setOnClickListener(this);
        match.setOnClickListener(this);
        user.setOnClickListener(this);




    }

    @Override
    public void onClick(View v) {
        if(v == options){
            Intent i = new Intent(this, Activity_Search_Filter.class);
            startActivity(i);

        } else if(v == match){

            Intent i = new Intent(this, Activity_Likeside.class);
            startActivity(i);

        } else if(v == user){

            Intent i = new Intent(this, Activity_U_Settings.class);
            startActivity(i);

        }
    }
}