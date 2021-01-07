package com.A4.oplev;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import Controller.EventController;
import DTO.EventDTO;
import DTO.UserDTO;

public class Activity_Event extends AppCompatActivity implements View.OnClickListener {
    public TextView eventName, eCity, eDate, ePrice, eAbout, eUname, eUabout, picNumber;
    ImageView eventPic, profilePic;
    EventController eventController;
    ArrayList<String> pictures, currPics;
    UserDTO user;
    EventDTO event;
    int height, width, currentPic, maxPic, minPic, maxPicPrint;




    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        Intent myIntent = getIntent();

        eventName = findViewById(R.id.text_event_information);
        eCity = findViewById(R.id.text_e_city);
        eDate = findViewById(R.id.text_date);
        ePrice = findViewById(R.id.text_price);
        eAbout = findViewById(R.id.text_e_about);
        eUname = findViewById(R.id.text_e_u_name);
        eUabout = findViewById(R.id.text_u_about);
        picNumber = findViewById(R.id.cur_picEvent);
        eventPic = findViewById(R.id.imageView_e_pb);
        profilePic = findViewById(R.id.event_profile_picture);

        profilePic.setOnClickListener(this);

        eventController =  eventController.getInstance();
        pictures = eventController.getEventPictures();





        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;
        eventPic.setMaxHeight(height/2 + 200);

        user = (UserDTO) myIntent.getSerializableExtra("user");
        event = (EventDTO) myIntent.getSerializableExtra("event");

        Picasso.get().load(event.getEventPic())
                .resize(width, height/2 + 200)
                .centerCrop()
                .placeholder(R.drawable.load2)
                .into(eventPic);

        Picasso.get().load(user.getUserPicture())
                .placeholder(R.drawable.load2)
                .into(profilePic);

        eventController.iniEvents(this, event, user);




        //getPictures();



     /*   pb.setOnTouchListener(new OnSwipeTouchListener(getApplicationContext()){
            @SuppressLint("ResourceAsColor")
            @Override
            public void onSwipeLeft() {
                if(currentPic != maxPic){
                    Picasso.get().load(currPics.get(currentPic+1))
                            .resize(width, height/2 + 200)
                            .centerCrop()
                            .placeholder(R.drawable.load2)
                            .into(pb);
                    currentPic = currentPic + 1;
                    String text = currentPic+1 + "/" + maxPicPrint;
                    picNumber.setText(text);
                }
            }
            @Override
            public void onSwipeRight(){
                if(currentPic != minPic){
                    Picasso.get().load(currPics.get(currentPic-1))
                            .resize(width, height/2 + 200)
                            .centerCrop()
                            .placeholder(R.drawable.load2)
                            .into(pb);

                    String text = currentPic + "/" + maxPicPrint;
                    picNumber.setText(text);
                    currentPic = currentPic - 1;
                }

            }
        });
           */



    }

    private void getPictures() {
        currPics = new ArrayList<>();
        maxPic = 0;
        minPic = 0;
        currentPic = 0;
        int flag = 0;
        boolean noPic = true;

        int counter = 0;
        for(int i=0; i<6; i++){
            if(pictures.get(i) != null){
                if(flag == 0){
                    Picasso.get().load(pictures.get(i))
                            .resize(width, height/2 + 200)
                            .centerCrop()
                            .placeholder(R.drawable.load2)
                            .error(R.drawable.question)
                            .into(eventPic);
                    flag = 1;
                    noPic = false;

                }
                counter += 1;
                currPics.add(pictures.get(i));
            }
        }
        maxPic = counter-1;
        maxPicPrint = maxPic +1;

        String text = currentPic+1 + "/" + maxPicPrint;
        if(noPic) picNumber.setText("1/1");
        else picNumber.setText(text);

    }

    @Override
    public void onStart() {
        super.onStart();



    }


    @Override
    public void onClick(View v) {
        if(v == profilePic){
            Intent i = new Intent(this, Activity_Profile.class);
            i.putExtra("user", user);
            i.putExtra("load", 1);
            startActivity(i);
        }
    }
}