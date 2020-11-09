package com.A4.oplev;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class Activity_Likeside extends AppCompatActivity implements View.OnClickListener{
    ImageView hjerte, besked, tilbage;
    Button opret;


    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.likeside);
        opret = findViewById(R.id.opretOpslag_Knap2);

        hjerte = findViewById(R.id.likeside_hjertbillede);
        besked = findViewById(R.id.likeside_beskedbillede);
        tilbage = findViewById(R.id.topbar_arrow);

        getSupportFragmentManager().beginTransaction().replace(R.id.likeside_frameLayout,new LikesideList_frag())
                .commit();

        hjerte.setOnClickListener(this);
        besked.setOnClickListener(this);
        tilbage.setOnClickListener(this);
        opret.setOnClickListener(this);

        hjerte.setBackgroundColor(Color.LTGRAY);



    }





    @Override
    public void onClick(View v) {
        if (v == hjerte){
            besked.setBackgroundColor(Color.LTGRAY);
            hjerte.setBackgroundColor(Color.CYAN);
            getSupportFragmentManager().beginTransaction().replace(R.id.likeside_frameLayout,new HjerteSide_frag())
                    .commit();
        }
        else if (v == besked){
            besked.setBackgroundColor(Color.CYAN);
            hjerte.setBackgroundColor(Color.LTGRAY);
            getSupportFragmentManager().beginTransaction().replace(R.id.likeside_frameLayout,new LikesideList_frag())
                    .commit();
        }
        else if (v == tilbage){
            finish();
        }
        else if(v == opret){
            Intent o = new Intent(this, Activity_Create_Event.class);
            startActivity(o);
        }

    }
}