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
    View left, right, editBtn, backBtn, settingBtn, selection1, selection2, selection3, selection4, selection5, selection6, currentSelection;
    boolean noPic;
    int height, width, currentPic, maxPic, minPic, maxPicPrint, j;




    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Intent myIntent = getIntent();

        j = myIntent.getIntExtra("load", 0);

        userController =  userController.getInstance();
        if(j==0) pictures = userController.getUserPictures();

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


        System.out.println(j);

        currentSelection = selection1;
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

    @Override
    protected void onResume() {
        super.onResume();


        if (j == 0) {
            if(currentSelection == null) currentSelection = selection1;

            currentSelection.setBackground(getResources().getDrawable(R.drawable.picselection));
            pictures = userController.getUserPictures();

            getPictures();
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
        System.out.println(newCount);

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
        //Nulstiller billede visibility
        for(int i = 0; i<6; i++){

            switch (i){
                case 0:
                    selection1.setVisibility(View.INVISIBLE);
                    break;
                case 1:
                    selection2.setVisibility(View.INVISIBLE);
                    break;
                case 2:
                    selection3.setVisibility(View.INVISIBLE);
                    break;
                case 3:
                    selection4.setVisibility(View.INVISIBLE);

                    break;
                case 4:
                    selection5.setVisibility(View.INVISIBLE);
                    break;
                case 5:
                    selection6.setVisibility(View.INVISIBLE);
                    break;
            }
        }
        //Opdatere billede visibility
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
        currentSelection = selection1;
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
                currentSelection = selection2;
                break;
            case 2:
                selection2.setBackground(getResources().getDrawable(R.drawable.picselection));
                selection3.setBackground(getResources().getDrawable(R.drawable.picselectionfill));
                currentSelection = selection3;
                break;
            case 3:
                selection3.setBackground(getResources().getDrawable(R.drawable.picselection));
                selection4.setBackground(getResources().getDrawable(R.drawable.picselectionfill));
                currentSelection = selection4;
                break;
            case 4:
                selection4.setBackground(getResources().getDrawable(R.drawable.picselection));
                selection5.setBackground(getResources().getDrawable(R.drawable.picselectionfill));
                currentSelection = selection5;
                break;
            case 5:
                selection5.setBackground(getResources().getDrawable(R.drawable.picselection));
                selection6.setBackground(getResources().getDrawable(R.drawable.picselectionfill));
                currentSelection = selection6;
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
                currentSelection = selection1;
                break;
            case 2:
                selection3.setBackground(getResources().getDrawable(R.drawable.picselection));
                selection2.setBackground(getResources().getDrawable(R.drawable.picselectionfill));
                currentSelection = selection2;
                break;
            case 3:
                selection4.setBackground(getResources().getDrawable(R.drawable.picselection));
                selection3.setBackground(getResources().getDrawable(R.drawable.picselectionfill));
                currentSelection = selection3;
                break;
            case 4:
                selection5.setBackground(getResources().getDrawable(R.drawable.picselection));
                selection4.setBackground(getResources().getDrawable(R.drawable.picselectionfill));
                currentSelection = selection4;
                break;
            case 5:
                selection6.setBackground(getResources().getDrawable(R.drawable.picselection));
                selection5.setBackground(getResources().getDrawable(R.drawable.picselectionfill));
                currentSelection = selection5;
                break;
        }

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
            String eventId = i.getStringExtra("eventID");
            userController.onClickAccept(this, header, eventId, user);


        }
        if(v == reject){
            // hvis brugeren afviser en applicant
            Intent i = getIntent();
            ArrayList<String> otherApplicants = i.getStringArrayListExtra("applicantList");
            String eventId = i.getStringExtra("eventID");
            String header = i.getStringExtra("header");
            userController.onClickReject(this, otherApplicants, eventId, header);
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
        if (v == editBtn){
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