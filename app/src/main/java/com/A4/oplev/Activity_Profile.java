package com.A4.oplev;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import Controller.Listeners.OnSwipeTouchListener;
import Controller.UserController;

public class Activity_Profile extends AppCompatActivity implements View.OnClickListener {
    public TextView about, city, desc, aboutName, job, edu, picNumber;
    ImageView pb;
    UserController userController;
    ArrayList<String> pictures, currPics;

    int height, width, currentPic, maxPic, minPic, maxPicPrint;




    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

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

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;
        pb.setMaxHeight(height/2 + 200);

        getPictures();


        pb.setOnTouchListener(new OnSwipeTouchListener(getApplicationContext()){
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

        userController.iniProfile(this);


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

    }
}