package com.A4.oplev.__Main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.OnScrollListener;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.A4.oplev.Activity_NoInstance;
import com.A4.oplev.Like_Hjerte_Side.Activity_Likeside;
import com.A4.oplev.R;
import com.A4.oplev.SearchFilter.Activity_Search_Filter;
import com.A4.oplev.UserSettings.Activity_U_Settings;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.A4.oplev._Adapters.Event_Adapter;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

//import DAL.DBAccess;
import DAL.Classes.EventDAO;
import DAL.Interfaces.CallBackEventList;
import DAL.Interfaces.CallBackList;
import DAL.Interfaces.CallbackEvent;
import DTO.EventDTO;
import swipeable.com.layoutmanager.OnItemSwiped;
import swipeable.com.layoutmanager.SwipeableLayoutManager;
import swipeable.com.layoutmanager.SwipeableTouchHelperCallback;
import swipeable.com.layoutmanager.touchelper.ItemTouchHelper;

public class Activity_Main extends AppCompatActivity implements View.OnClickListener{

    ImageView options, match, user;
    RecyclerView rcEvent;
    SharedPreferences prefs;
    Boolean onInstance;
   Event_Adapter event_Adapter;
   Context ctx;

   int lastscrooled = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        this.ctx = this;

        //Tjekker om hvorvidt man er logget ind
        onInstance = prefs.getBoolean("onInstance", false);

        //skal optimeres og ændres til at vi skal hente data ude fra.


        List<EventDTO> eventList = new ArrayList<>();

        rcEvent = findViewById(R.id.eventRecycleView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);


        EventDAO dataA = new EventDAO();


        dataA.getEventIDs(new CallBackList() {
            @Override
            public void onCallback(List<String> list) {

               dataA.getEvents(new CallBackEventList() {
                   @Override
                   public void onCallback(List<EventDTO> events) {
                       Picasso.get().load(events.get(0).getEventPic()).into(new Target() {
                           @Override
                           public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                               // loaded bitmap is here (bitmap)
                                eventIni(bitmap, events, layoutManager, list);
                           }
                           @Override
                           public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                           }
                           @Override
                           public void onPrepareLoad(Drawable placeHolderDrawable) {
                           }
                       });
                   }
               }, list);
            }
        });


        options = findViewById(R.id.options_btn);
        match = findViewById(R.id.match_btn);
        user = findViewById(R.id.user_btn);


        options.setOnClickListener(this);
        match.setOnClickListener(this);
        user.setOnClickListener(this);


    }

    private void eventIni(Bitmap pic,List<EventDTO> eventList, LinearLayoutManager layoutManager, List<String> ids) {
        event_Adapter = new Event_Adapter(pic, eventList, this, ids);
        rcEvent.setLayoutManager(layoutManager);
        rcEvent.setAdapter(event_Adapter);
        rcEvent.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                super.onScrolled(recyclerView, dx, dy);
                System.out.println("scrooled " + dx);

                if (dx > 0 && lastscrooled < 0)  {
                    // Scrolling right
                    event_Adapter.setWay(true);

                } else if(dx < 0 && lastscrooled > 0) {
                    // Scrolling left
                    event_Adapter.setWay(false);

                }
                lastscrooled = dx;
            }
        });


        PagerSnapHelper snap = new PagerSnapHelper();
        snap.attachToRecyclerView(rcEvent);
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