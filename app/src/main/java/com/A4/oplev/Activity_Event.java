package com.A4.oplev;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.A4.oplev.CreateEvent.Activity_Create_Event;
import com.A4.oplev.Login.Activity_Login;
import com.google.firebase.firestore.auth.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import Controller.EventController;
import Controller.UserController;
import DAL.Classes.ChatDAO;
import DAL.Classes.EventDAO;
import DAL.Classes.UserDAO;
import DAL.Interfaces.CallbackUser;
import DTO.EventDTO;
import DTO.UserDTO;

public class Activity_Event extends AppCompatActivity implements View.OnClickListener {
    public TextView eventName, eCity, eDate, ePrice, eAbout, eUname, eUabout, picNumber, eventPname, eDistance;
    ImageView eventPic, profilePic, repost, join, like;
    EventController eventController;
    LinearLayout box;
    ArrayList<String> pictures, currPics;
    UserDTO user;
    EventDTO event;
    View back;
    UserController userController;
    String eventId;
    int height, width, currentPic, maxPic, minPic, maxPicPrint;
    public SharedPreferences prefs;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Intent myIntent = getIntent();
        userController = UserController.getInstance();
        eventName = findViewById(R.id.text_event_information);
        eCity = findViewById(R.id.text_e_city);
        eDistance = findViewById(R.id.text_distance);
        eDate = findViewById(R.id.text_date);
        ePrice = findViewById(R.id.text_price);
        eAbout = findViewById(R.id.text_e_about);
        eUname = findViewById(R.id.text_e_u_name);
        eUabout = findViewById(R.id.text_u_about);
        picNumber = findViewById(R.id.cur_picEvent);
        eventPic = findViewById(R.id.imageView_e_pb);
        profilePic = findViewById(R.id.event_profile_picture);
        back = findViewById(R.id.back_btn_event);
        repost = findViewById(R.id.btn_repost);
        join = findViewById(R.id.btn_join);
        eventPname = findViewById(R.id.event_person_name);
        box = findViewById(R.id.buttons_on_event);
        like = findViewById(R.id.btn_like);


        //Knapper til billeder, repost og join
        profilePic.setOnClickListener(this);
        repost.setOnClickListener(this);
        join.setOnClickListener(this);
        back.setOnClickListener(this);
        like.setOnClickListener(this);

        eventController =  eventController.getInstance();
        pictures = eventController.getEventPictures();






        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;
        eventPic.setMaxHeight(height/2 + 200);

        user = (UserDTO) myIntent.getSerializableExtra("user");
        event = (EventDTO) myIntent.getSerializableExtra("event");
        eventId = myIntent.getStringExtra("eventId");

        Picasso.get().load(event.getEventPic())
                .resize(width, height/2 + 200)
                .centerCrop()
                .placeholder(R.drawable.load2)
                .into(eventPic);

        Picasso.get().load(user.getUserPicture())
                .placeholder(R.drawable.load2)
                .into(profilePic);



        int j = myIntent.getIntExtra("load", 0);

        if(j == 1){
            box.setVisibility(View.GONE);
        } else   box.setVisibility(View.VISIBLE);
        eventController.iniEvents(this, event, user, j);

        if (UserController.getInstance().getCurrUser() != null) {
            UserDTO user = UserController.getInstance().getCurrUser();
            ArrayList<String> likedeEvents;
            if (user.getLikedeEvents() == null) {
                likedeEvents = new ArrayList<>();
            } else likedeEvents = user.getLikedeEvents();
            if (!likedeEvents.contains(event.getEventId())) {
                like.setImageResource(R.drawable.heart);

            } else {
                like.setImageResource(R.drawable.hearthfill);

            }
        }
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
        else if (v == repost){
            if (UserController.getInstance().getCurrUser() != null) {
                Intent i = new Intent(this, Activity_Create_Event.class);
                i.putExtra("event", event);
                startActivity(i);
            } else {
                Intent i = new Intent(this, Activity_Login.class);
                startActivity(i);
            }
        }
        else if (v == join) {
            if (UserController.getInstance().getCurrUser() != null) {
                EventDAO dao = new EventDAO();
                ArrayList<String> applicants = event.getApplicants();



                if (!applicants.contains(UserController.getInstance().getCurrUser().getUserId())) {
                    applicants.add(UserController.getInstance().getCurrUser().getUserId());
                    event.setApplicants(applicants);
                    dao.updateEvent(event);


                    UserDAO userDAO = new UserDAO();
                    userDAO.getUser(new CallbackUser() {
                        @Override
                        public void onCallback(UserDTO user) {
                            ArrayList<String> requested = user.getRequestedEvents();
                            if(requested == null){
                                requested = new ArrayList<>();
                            }
                            requested.add(eventId);

                            userController.getCurrUser().setRequestedEvents(requested);
                            userController.getCurrUser().setChatId(user.getChatId());
                            userController.updateUserSimple(userController.getCurrUser());
                        }
                    }, userController.getCurrUser().getUserId());

                    finish();
                } else {
                    Toast.makeText(this, "Du har allerede ansøgt om at deltage", Toast.LENGTH_SHORT).show();
                }
            } else {
                Intent i = new Intent(this, Activity_Login.class);
                startActivity(i);
            }
        }
        else if (v == like) {
            if (UserController.getInstance().getCurrUser() != null) {
                UserDTO user = UserController.getInstance().getCurrUser();
                ArrayList<String> likedeEvents;
                if (user.getLikedeEvents() == null) {
                    likedeEvents = new ArrayList<>();
                } else likedeEvents = user.getLikedeEvents();
                if (!likedeEvents.contains(event.getEventId())) {
                    like.setImageResource(R.drawable.hearthfill);
                    likedeEvents.add(event.getEventId());
                    UserDAO dao = new UserDAO();
                    dao.updateUser(user);
                    Toast.makeText(this,"Event liket",Toast.LENGTH_SHORT).show();
                } else {
                    like.setImageResource(R.drawable.heart);
                    likedeEvents.remove(event.getEventId());
                    UserDAO dao = new UserDAO();
                    dao.updateUser(user);
                    Toast.makeText(this,"Event fjernet fra likes",Toast.LENGTH_SHORT).show();
                }
            }
        }
        else if(v == back){
            finish();
        }
    }
}