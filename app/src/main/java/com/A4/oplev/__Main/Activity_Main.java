package com.A4.oplev.__Main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;

import com.A4.oplev.Activity_NoInstance;
import com.A4.oplev.Like_Hjerte_Side.Activity_Likeside;
import com.A4.oplev.R;
import com.A4.oplev.SearchFilter.Activity_Search_Filter;
import com.A4.oplev.UserSettings.Activity_U_Settings;

import java.util.ArrayList;
import java.util.List;

import com.A4.oplev._Adapters.Event_Adapter;

//import DAL.DBAccess;
import DTO.EventDTO;
import swipeable.com.layoutmanager.OnItemSwiped;
import swipeable.com.layoutmanager.SwipeableLayoutManager;
import swipeable.com.layoutmanager.SwipeableTouchHelperCallback;
import swipeable.com.layoutmanager.touchelper.ItemTouchHelper;

public class Activity_Main extends AppCompatActivity implements View.OnClickListener{

    ImageView options, match, user;
    Intent intent;
    RecyclerView rcEvent;
    SharedPreferences prefs;
    Boolean onInstance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        //Tjekker om hvorvidt man er logget ind
        onInstance = prefs.getBoolean("onInstance", false);

        //skal optimeres og ændres til at vi skal hente data ude fra.
        List<EventDTO> eventList = new ArrayList<>();
        EventDTO dto1,dto2, dto3;
        dto1 = new EventDTO().setOwnerId("12").setTitle("Spis is i Nyhavn");
        dto2 = new EventDTO().setOwnerId("13").setTitle("Svøm i tsørvejr");
        dto3 = new EventDTO().setOwnerId("14").setTitle("Slå en mink ned");
        eventList.add(dto1);
        eventList.add(dto2);
        eventList.add(dto3);
        rcEvent = findViewById(R.id.eventRecycleView);

        final Event_Adapter Event_Adapter = new Event_Adapter(eventList);
        SwipeableTouchHelperCallback swipeableTouchHelperCallback =
                new SwipeableTouchHelperCallback(new OnItemSwiped() {
                    //Called after swiping view, place to remove top item from your recyclerview adapter
                    @Override public void onItemSwiped() {
                        Event_Adapter.removeTopItem();
                    }

                    @Override public void onItemSwipedLeft() {

                    }

                    @Override public void onItemSwipedRight() {

                    }

                    @Override
                    public void onItemSwipedUp() {

                    }

                    @Override
                    public void onItemSwipedDown() {

                    }
                });

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeableTouchHelperCallback);
        itemTouchHelper.attachToRecyclerView(rcEvent);

        rcEvent.setLayoutManager(new SwipeableLayoutManager());
        rcEvent.setAdapter(Event_Adapter);


        options = findViewById(R.id.options_btn);
        match = findViewById(R.id.match_btn);
        user = findViewById(R.id.user_btn);


        options.setOnClickListener(this);
        match.setOnClickListener(this);
        user.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if(!onInstance){
            Intent i = new Intent(this, Activity_NoInstance.class);
            startActivity(i);


        } else if(v == options){
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