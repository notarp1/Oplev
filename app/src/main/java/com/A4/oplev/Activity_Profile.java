package com.A4.oplev;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.A4.oplev.Like_Hjerte_Side.Activity_Likeside;
import com.A4.oplev.Like_Hjerte_Side.LikesideList_frag;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import Controller.UserController;
import DAL.Classes.ChatDAO;
import DAL.Classes.EventDAO;
import DAL.Classes.UserDAO;
import DAL.Interfaces.CallbackEvent;
import DTO.ChatDTO;
import DTO.EventDTO;
import DTO.UserDTO;

public class Activity_Profile extends AppCompatActivity implements View.OnClickListener {
    public TextView about, city, desc, aboutName, job, edu, picNumber, informationText;
    ImageView pb, accept, reject, p1, p2, p3, p4, p5, p6;
    LinearLayout confirmationBox;
    RecyclerView pbtest;
    UserController userController;
    ArrayList<String> pictures, currPics;
    View left, right;
    boolean noPic;
    int height, width, currentPic, maxPic, minPic, maxPicPrint;




    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Intent myIntent = getIntent();

        userController =  userController.getInstance();
        pictures = userController.getUserPictures();


        about = findViewById(R.id.text_information);
        city = findViewById(R.id.text_city);
        desc = findViewById(R.id.text_description);
        aboutName = findViewById(R.id.text_about_name);
        edu = findViewById(R.id.text_edu);
        job = findViewById(R.id.text_job);
        pb = findViewById(R.id.imageView_pb);
        picNumber = findViewById(R.id.text_curPic);
        left = findViewById(R.id.left_but);
        right = findViewById(R.id.right_but);

        reject = findViewById(R.id.btn_reject);
        accept = findViewById(R.id.btn_accept);
        informationText = findViewById(R.id.text_confirmation);
        confirmationBox = findViewById(R.id.confirmationBox);
        confirmationBox.setVisibility(View.GONE);

        left.setOnClickListener(this);
        right.setOnClickListener(this);
        accept.setOnClickListener(this);
        reject.setOnClickListener(this);


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;
        pb.setMaxHeight(height/2 + 200);


        right.setMinimumWidth(width/2);
        left.setMinimumWidth(width/2);

        int j = myIntent.getIntExtra("load", 0);
        System.out.println(j);


        if(j == 1 || j == 2){
            if(j==2) {
                confirmationBox.setVisibility(View.VISIBLE);
                int numbers = myIntent.getIntExtra("numberOfApplicants",0);
                informationText.setText(numbers + " andre ønsker at deltage");
            }
            UserDTO user = (UserDTO) myIntent.getSerializableExtra("user");
            System.out.println(user.getDescription() + " " + user.getfName());
            pictures = user.getPictures();
            getPictures();
            userController.iniPublicProfile(this, user);
        } else {
            getPictures();
            userController.iniProfile(this);
        }


    }



    private void getPictures() {
        currPics = new ArrayList<>();
        maxPic = 0;
        minPic = 0;
        currentPic = 0;
        int flag = 0;
        noPic = true;

        int counter = 0;
        for(int i=0; i<6; i++){
            if(pictures.get(i) != null){
                if(flag == 0){
                    Picasso.get().load(pictures.get(i))
                            .resize(width, height/2 + 200)
                            .centerCrop()
                            .placeholder(R.drawable.load2)
                            .error(R.drawable.question)
                            .into(pb);
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
        if(v == accept){
            Intent i = getIntent();
            UserDTO user = (UserDTO) i.getSerializableExtra("user");
            String header = i.getStringExtra("header");
            ChatDAO chatDAO = new ChatDAO();
            ChatDTO chatDTO = new ChatDTO(null,null,null,null,null,null,header,userController.getCurrUser().getfName(),user.getfName(),userController.getCurrUser().getUserId(),user.getUserId());
            chatDAO.createChat(chatDTO, new ChatDAO.CreateChatCallback() {
                @Override
                public void onCallback(String chatID) {
                    EventDAO eventDAO = new EventDAO();
                    eventDAO.getEvent(new CallbackEvent() {
                        @Override
                        public void onCallback(EventDTO event) {
                            if (event != null) {
                                event.setParticipant(user.getUserId());
                                System.out.println("EVENT ID : " + event.getEventId());
                                eventDAO.updateEvent(event);
                            }
                        }
                    }, i.getStringExtra("eventID"));

                    ArrayList<String> otherUserChatID;
                    if (user.getChatId() == null){
                        otherUserChatID = new ArrayList<>();
                    } else otherUserChatID = user.getChatId();
                    otherUserChatID.add(chatID);


                    ArrayList<String> thisUserChatID;
                    if (userController.getCurrUser().getChatId() == null){
                        thisUserChatID = new ArrayList<>();
                    } else thisUserChatID = userController.getCurrUser().getChatId();
                    thisUserChatID.add(chatID);


                    UserDAO userDAO = new UserDAO();
                    userDAO.updateUser(user);
                    userDAO.updateUser(userController.getCurrUser());
                    finish();
                }
            });
        }
        if(v == reject){
            Intent i = getIntent();
            ArrayList<String> otherApplicants = i.getStringArrayListExtra("applicantList");
            otherApplicants.remove(0);
            if (otherApplicants.size() != 0) {
                userController.getUser(user -> {
                    Intent i12 = new Intent(this, Activity_Profile.class);
                    i12.putExtra("user", user);
                    i12.putExtra("load", 2);
                    i12.putExtra("numberOfApplicants", otherApplicants.size() - 1);
                    i12.putExtra("applicantList", otherApplicants);
                    this.startActivity(i12);
                }, otherApplicants.get(0));
            }
            finish();
        }
        if(!noPic) {
            if(v == right){
                if (currentPic != maxPic) {
                    Picasso.get().load(currPics.get(currentPic + 1))
                            .resize(width, height / 2 + 200)
                            .centerCrop()
                            .placeholder(R.drawable.load2)
                            .into(pb);

                    currentPic = currentPic + 1;
                    String text = currentPic + 1 + "/" + maxPicPrint;
                    picNumber.setText(text);
                }
            }
            else if (v == left){


                if (currentPic != minPic) {
                    Picasso.get().load(currPics.get(currentPic - 1))
                            .resize(width, height / 2 + 200)
                            .centerCrop()
                            .placeholder(R.drawable.load2)
                            .into(pb);

                    String text = currentPic + "/" + maxPicPrint;
                    picNumber.setText(text);
                    currentPic = currentPic - 1;
                }
            }
        }
    }
}