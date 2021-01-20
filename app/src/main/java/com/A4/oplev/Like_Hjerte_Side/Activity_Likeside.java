package com.A4.oplev.Like_Hjerte_Side;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.A4.oplev.CreateEvent.Activity_Create_Event;
import Controller.Listeners.OnSwipeTouchListener;
import com.A4.oplev.R;

public class Activity_Likeside extends AppCompatActivity implements View.OnClickListener{
    private static int position;
    private ImageView hjerte, besked, tilbage, backhjerte, backbesked, events, backevents;
    private Button opret;


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

        


        getWindow().getDecorView().getRootView().setOnTouchListener(new OnSwipeTouchListener(this){
            @Override
            public void onSwipeLeft() {
                // Swiper til venstre fra ownevent siden skifter til likesiden
                if (position==0) {
                    Log.d("pos", position+"");
                    getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right,
                            R.anim.exit_to_left).replace(R.id.likeside_frameLayout, new LikesideList_frag())
                            .commit();
                    backbesked.setVisibility(View.VISIBLE);
                    backhjerte.setVisibility(View.INVISIBLE);
                    backevents.setVisibility(View.INVISIBLE);
                }
                // Swiper til venstre fra likesiden siden skifter til hjertesiden
                if (position==1){
                    Log.d("pos", position+"");
                    getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right,
                            R.anim.exit_to_left).replace(R.id.likeside_frameLayout, new HjerteSide_frag())
                            .commit();
                    backbesked.setVisibility(View.INVISIBLE);
                    backevents.setVisibility(View.INVISIBLE);
                    backhjerte.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onSwipeRight(){
                // Swiper til højre fra likesiden siden skifter til ownevent
                if (position==1){
                    Log.d("pos", position+"");
                    getSupportFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.slide_in_left,
                            android.R.anim.slide_out_right).replace(R.id.likeside_frameLayout, new OwnEvent_frag())
                            .commit();
                    backbesked.setVisibility(View.INVISIBLE);
                    backhjerte.setVisibility(View.INVISIBLE);
                    backevents.setVisibility(View.VISIBLE);
                    position = 0;
                }
                // Swiper til højre fra hjertesiden siden skifter til likesiden
                if (position==2) {
                    Log.d("pos", position+"");
                    getSupportFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.slide_in_left,
                            android.R.anim.slide_out_right).replace(R.id.likeside_frameLayout, new LikesideList_frag())
                            .commit();
                    backbesked.setVisibility(View.VISIBLE);
                    backevents.setVisibility(View.INVISIBLE);
                    backhjerte.setVisibility(View.INVISIBLE);
                    position=1;
                }
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
            getSupportFragmentManager().beginTransaction().replace(R.id.likeside_frameLayout,new OwnEvent_frag())
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

    // for at sætte positionen til swipe funktionen (0 = ownevent, 1 = likeside, 2 = hjerteside)
    public static void setPosition(int position1){
        position = position1;
    }
}
