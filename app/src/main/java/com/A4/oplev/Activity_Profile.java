package com.A4.oplev;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
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
import com.A4.oplev.SearchFilter.Activity_Search_Filter;
import com.A4.oplev.UserSettings.Activity_U_Settings;
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
    public TextView about, city, desc, aboutName, job, edu, informationText;
    ImageView pb, accept, reject, p1, p2, p3, p4, p5, p6;
    LinearLayout confirmationBox;
    RecyclerView pbtest;
    UserController userController;
    ArrayList<String> pictures, currPics;
    ConstraintLayout edit;
    View left, right, editBtn, backBtn, settingBtn, selection1, selection2, selection3, selection4, selection5, selection6;
    boolean noPic;
    int height, width, currentPic, maxPic, minPic, maxPicPrint, currentSelection;




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
        left = findViewById(R.id.left_but);
        right = findViewById(R.id.right_but);
        editBtn = findViewById(R.id.on_edit);
        reject = findViewById(R.id.btn_reject);
        accept = findViewById(R.id.btn_accept);
        edit = findViewById(R.id.constrain_edit);

        informationText = findViewById(R.id.text_confirmation);
        confirmationBox = findViewById(R.id.confirmationBox);
        backBtn = findViewById(R.id.btn_back_profile);
        settingBtn = findViewById(R.id.settings_btn_pro);
        selection1 = findViewById(R.id.selection1);
        selection2 = findViewById(R.id.selection2);
        selection3 = findViewById(R.id.selection3);
        selection4 = findViewById(R.id.selection4);
        selection5 = findViewById(R.id.selection5);
        selection6 = findViewById(R.id.selection6);
        confirmationBox.setVisibility(View.GONE);

        left.setOnClickListener(this);
        right.setOnClickListener(this);
        accept.setOnClickListener(this);
        reject.setOnClickListener(this);
        editBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        settingBtn.setOnClickListener(this);


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
            edit.setVisibility(View.INVISIBLE);
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
        int newCount = 0;

        int counter = 0;

        for(int i=0; i<6; i++){
            if(pictures.get(i) != null)newCount++;
        }

        if(newCount == 0){
            selection1.setVisibility(View.VISIBLE);
            selection1.setBackground(getResources().getDrawable(R.drawable.picselectionfill));
        } else iniView(newCount);

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


    }

    private void iniView(int count){

        for(int i = 0; i<count; i++){

            switch (i){
                case 0:
                    selection1.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    selection2.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    selection3.setVisibility(View.VISIBLE);
                    break;
                case 3:
                    selection4.setVisibility(View.VISIBLE);

                    break;
                case 4:
                    selection5.setVisibility(View.VISIBLE);
                    break;
                case 5:
                    selection6.setVisibility(View.VISIBLE);
                    break;
            }
        }
        currentSelection = 0;
        selection1.setBackground(getResources().getDrawable(R.drawable.picselectionfill));
    }

    private void updateViewRight(int currentPic){

        switch (currentPic){
            case 0:
              //  selection1.setBackground(getResources().getDrawable(R.drawable.picselection));
             //   selection2.setBackground(getResources().getDrawable(R.drawable.picselectionfill));
                break;
            case 1:
                selection1.setBackground(getResources().getDrawable(R.drawable.picselection));
                selection2.setBackground(getResources().getDrawable(R.drawable.picselectionfill));
                break;
            case 2:
                selection2.setBackground(getResources().getDrawable(R.drawable.picselection));
                selection3.setBackground(getResources().getDrawable(R.drawable.picselectionfill));
                break;
            case 3:
                selection3.setBackground(getResources().getDrawable(R.drawable.picselection));
                selection4.setBackground(getResources().getDrawable(R.drawable.picselectionfill));
                break;
            case 4:
                selection4.setBackground(getResources().getDrawable(R.drawable.picselection));
                selection5.setBackground(getResources().getDrawable(R.drawable.picselectionfill));
                break;
            case 5:
                selection5.setBackground(getResources().getDrawable(R.drawable.picselection));
                selection6.setBackground(getResources().getDrawable(R.drawable.picselectionfill));
                break;
        }

    }

    private void updateViewLeft(int currentPic){

        switch (currentPic){
            case 0:
                //  selection1.setBackground(getResources().getDrawable(R.drawable.picselection));
                //   selection2.setBackground(getResources().getDrawable(R.drawable.picselectionfill));
                break;
            case 1:
                selection2.setBackground(getResources().getDrawable(R.drawable.picselection));
                selection1.setBackground(getResources().getDrawable(R.drawable.picselectionfill));
                break;
            case 2:
                selection3.setBackground(getResources().getDrawable(R.drawable.picselection));
                selection2.setBackground(getResources().getDrawable(R.drawable.picselectionfill));
                break;
            case 3:
                selection4.setBackground(getResources().getDrawable(R.drawable.picselection));
                selection3.setBackground(getResources().getDrawable(R.drawable.picselectionfill));
                break;
            case 4:
                selection5.setBackground(getResources().getDrawable(R.drawable.picselection));
                selection4.setBackground(getResources().getDrawable(R.drawable.picselectionfill));
                break;
            case 5:
                selection6.setBackground(getResources().getDrawable(R.drawable.picselection));
                selection5.setBackground(getResources().getDrawable(R.drawable.picselectionfill));
                break;
        }

    }

    @Override
    public void onStart() {
        super.onStart();



    }


    @Override
    public void onClick(View v) {
        if(v == backBtn){
            finish();
        } else if(v == accept){
            // hvis man accepterer en person til et event
            Intent i = getIntent();
            // hent brugeren
            UserDTO user = (UserDTO) i.getSerializableExtra("user");
            String header = i.getStringExtra("header");
            ChatDAO chatDAO = new ChatDAO();
            // vi laver et chatobjekt med de nødvendige informationer
            ChatDTO chatDTO = new ChatDTO(null,null,null,null,null,null,header,userController.getCurrUser().getfName(),user.getfName(),userController.getCurrUser().getUserId(),user.getUserId());
            chatDAO.createChat(chatDTO, chatID -> {
                // vi skal opdatere eventet til at have en participant
                EventDAO eventDAO = new EventDAO();
                eventDAO.getEvent(event -> {
                    if (event != null) {
                        event.setParticipant(user.getUserId());
                        eventDAO.updateEvent(event);
                    }
                }, i.getStringExtra("eventID"));

                // vi skal indsætte chatid'et på begge brugeres lister
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

                // vi opdaterer begge brugere i databasen
                UserDAO userDAO = new UserDAO();
                userDAO.updateUser(user);
                userDAO.updateUser(userController.getCurrUser());
                finish();
            });
        }
        if(v == reject){
            // hvis brugeren afviser en applicant
            Intent i = getIntent();
            ArrayList<String> otherApplicants = i.getStringArrayListExtra("applicantList");
            // hvis der stadigvæk er nogle applicants tilbage i listen
            if (otherApplicants.size() != 0) {
                EventDAO eventDAO = new EventDAO();
                // vi henter eventet for at kunne opdatere det
                eventDAO.getEvent(event -> {
                    if (event != null) {
                        // vi fjerner den applicant man har afvist og opdaterer det i databasen
                        ArrayList<String> newApplicants = event.getApplicants();
                        newApplicants.remove(0);
                        event.setApplicants(newApplicants);
                        eventDAO.updateEvent(event);
                    }
                }, i.getStringExtra("eventID"));

                // vi fjerner den man har afvist i vores liste
                otherApplicants.remove(0);
                // hvis der er flere personer tilbage
                if (otherApplicants.size() != 0) {
                    // vi indlæser den næste applicant
                    userController.getUser(user -> {
                        // start nyt intent med den næste applicant
                        Intent i12 = new Intent(this, Activity_Profile.class);
                        i12.putExtra("user", user);
                        i12.putExtra("load", 2);
                        i12.putExtra("numberOfApplicants", otherApplicants.size() - 1);
                        i12.putExtra("applicantList", otherApplicants);
                        i12.putExtra("header", i.getStringExtra("header"));
                        i12.putExtra("eventID", i.getStringExtra("eventID"));
                        this.startActivity(i12);
                    }, otherApplicants.get(0));
                }
            }
            // afslut aktiviteten så man ikke kan gå tilbage
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
                    updateViewRight(currentPic);

                }
            }
            else if (v == left){


                if (currentPic != minPic) {
                    Picasso.get().load(currPics.get(currentPic - 1))
                            .resize(width, height / 2 + 200)
                            .centerCrop()
                            .placeholder(R.drawable.load2)
                            .into(pb);

                    updateViewLeft(currentPic);
                    currentPic = currentPic - 1;
                }
            }
        }
        if (v == editBtn || v == settingBtn){
            Intent i = new Intent(this, Activity_U_Settings.class);
            i.putExtra("selection", 1);
            startActivity(i);
        }
        if(v == settingBtn){
            Intent i = new Intent(this, Activity_U_Settings.class);
            i.putExtra("selection", 2);
            startActivity(i);

        }
    }
}