package com.A4.oplev;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.A4.oplev.__Main.Activity_Main;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.Serializable;
import java.util.ArrayList;

import Controller.Controller;
import DAL.Interfaces.CallbackUser;
import DTO.UserDTO;

public class Activity_Ini extends AppCompatActivity implements Serializable {
    Controller controller;
    UserDTO userDTO;
    SharedPreferences prefs;
    Context ctx;
    boolean onInstance;
    ArrayList<String> pictures;
    PicassoFunc picasso;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        picasso = PicassoFunc.getInstance();
        setContentView(R.layout.activity__ini);
        mAuth = FirebaseAuth.getInstance();

        //KØR NEDENSTÅENDE FOR AT RESETTE OG UDKOMMENTER EFTER
        //FirebaseAuth.getInstance().signOut();
        //PreferenceManager.getDefaultSharedPreferences(this).edit().clear().apply();



    }

    @Override
    public void onStart() {
        super.onStart();

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        controller = Controller.getInstance();
        ctx = this;
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        onInstance = prefs.getBoolean("onInstance", false);

        if(currentUser == null){
            prefs.edit().putBoolean("onInstance", false).apply();
            Intent i = new Intent(this, Activity_Main.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);

        }else {
            prefs.edit().putBoolean("onInstance", true).apply();
            controller.getUser(new CallbackUser() {
                @Override
                public void onCallback(UserDTO user) {
                    setUserDTO(user);
                    prefs.edit().putString("userId",user.getUserId()).apply();
                    Intent i = new Intent(ctx, Activity_Main.class);
                    controller.setCurrUser(user);
                    picasso.getUserPictures(ctx);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                }
            }, currentUser.getUid());

        }

    }
/*
    private void getUserPictures() {
        pictures = controller.getUserPictures();
        for(int i = 0; i<pictures.size(); i++){

            if(pictures.get(i) != null){
                Picasso.get().load(pictures.get(i)).into(picassoFunc.picassoImageTarget(getApplicationContext(), "imageDir", "ppic"+i+".png"));
            } else System.out.println("hejTESST");

        }

    } */


    private void setUserDTO(UserDTO dto){
        this.userDTO = dto;
    }




}