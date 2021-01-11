package com.A4.oplev.__Main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.A4.oplev.Activity_NoInstance;
import com.A4.oplev.Like_Hjerte_Side.Activity_Likeside;
import com.A4.oplev.PicassoFunc;
import com.A4.oplev.R;
import com.A4.oplev.SearchFilter.Activity_Search_Filter;
import com.A4.oplev.UserSettings.Activity_U_Settings;

import java.util.ArrayList;
import java.util.List;

import com.A4.oplev._Adapters.Event_Adapter;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

//import DAL.DBAccess;
import DAL.Classes.EventDAO;
import DAL.Interfaces.CallBackEventList;
import DAL.Interfaces.CallBackList;
import DAL.Interfaces.CallbackBitmap;
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

        EventDAO dataA = new EventDAO();

        dataA.getEventIDs(new CallBackList() {
            public void onCallback(List<String> list) {
                eventIni(list);
            }
        },prefs);


        options = findViewById(R.id.options_btn);
        match = findViewById(R.id.match_btn);
        user = findViewById(R.id.user_btn);


        options.setOnClickListener(this);
        match.setOnClickListener(this);
        user.setOnClickListener(this);


    }

    private void eventIni( List<String> ids) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        event_Adapter = Event_Adapter.getInstance(ids, this, height, width);
        rcEvent.setLayoutManager(layoutManager);
        rcEvent.setAdapter(event_Adapter);
        rcEvent.setOnFlingListener(null);
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

    public void onResume() {
        super.onResume();
        Event_Adapter eventA = Event_Adapter.getInstance();
        if(eventA != null) {
            if (eventA.getDataChanged()) {
                eventA.setDataChanged(false);
            }
        }

    }

}