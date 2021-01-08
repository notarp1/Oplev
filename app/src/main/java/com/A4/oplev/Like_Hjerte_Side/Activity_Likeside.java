package com.A4.oplev.Like_Hjerte_Side;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.A4.oplev.CreateEvent.Activity_Create_Event;
import Controller.Listeners.OnSwipeTouchListener;
import com.A4.oplev.R;

public class Activity_Likeside extends AppCompatActivity implements View.OnClickListener{
    ImageView hjerte, besked, tilbage, backhjerte, backbesked, events, backevents;
    Button opret;


    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_likeside);
        opret = findViewById(R.id.opretOpslag_Knap2);

        hjerte = findViewById(R.id.likeside_hjertbillede);
        besked = findViewById(R.id.likeside_beskedbillede);
        events = findViewById(R.id.likeside_eventbillede);
        tilbage = findViewById(R.id.topbar_arrow);
        backbesked = findViewById(R.id.besked_back);
        backhjerte = findViewById(R.id.hjerte_back);
        backevents = findViewById(R.id.event_back);

        getSupportFragmentManager().beginTransaction().replace(R.id.likeside_frameLayout,new LikesideList_frag())
                .commit();

        hjerte.setOnClickListener(this);
        besked.setOnClickListener(this);
        events.setOnClickListener(this);
        tilbage.setOnClickListener(this);
        opret.setOnClickListener(this);

        backhjerte.setVisibility(View.INVISIBLE);
        backevents.setVisibility(View.INVISIBLE);

        // Sætter en swipe listener op på skærmen for at kunne swipe til siden
        getWindow().getDecorView().getRootView().setOnTouchListener(new OnSwipeTouchListener(this){
            @Override
            public void onSwipeLeft() {
                getSupportFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.slide_in_left,
                        android.R.anim.slide_out_right).replace(R.id.likeside_frameLayout,new HjerteSide_frag())
                        .commit();
            }
        });

    }





    @Override
    public void onClick(View v) {
        if (v == hjerte){
            backbesked.setVisibility(View.INVISIBLE);
            backevents.setVisibility(View.INVISIBLE);

            backhjerte.setVisibility(View.VISIBLE);
            getSupportFragmentManager().beginTransaction().replace(R.id.likeside_frameLayout,new HjerteSide_frag())
                    .commit();
        }
        else if (v == besked){

            backbesked.setVisibility(View.VISIBLE);
            backhjerte.setVisibility(View.INVISIBLE);
            backevents.setVisibility(View.INVISIBLE);
            getSupportFragmentManager().beginTransaction().replace(R.id.likeside_frameLayout,new LikesideList_frag())
                    .commit();
        } else if (v == events){
            backbesked.setVisibility(View.INVISIBLE);
            backhjerte.setVisibility(View.INVISIBLE);
            backevents.setVisibility(View.VISIBLE);
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
