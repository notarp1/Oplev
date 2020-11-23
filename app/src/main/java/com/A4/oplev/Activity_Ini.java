package com.A4.oplev;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;

import com.A4.oplev.R;
import com.A4.oplev.__Main.Activity_Main;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.Serializable;

import Controller.Controller;
import DAL.Interfaces.CallbackUser;
import DTO.UserDTO;

public class Activity_Ini extends AppCompatActivity implements Serializable {
    Controller controller;
    UserDTO userDTO;
    SharedPreferences prefs;
    Context ctx;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__ini);


        mAuth = FirebaseAuth.getInstance();





    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.

        FirebaseUser currentUser = mAuth.getCurrentUser();
        controller = Controller.getInstance();
        ctx = this;
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        if(currentUser == null){
            prefs.edit().putBoolean("onInstance", false).apply();
            Intent i = new Intent(this, Activity_Main.class);
            startActivity(i);
        } else {
            prefs.edit().putBoolean("onInstance", true).apply();
            Intent thisIntent = getIntent();

            controller.getUser(new CallbackUser() {
                @Override
                public void onCallback(UserDTO user) {
                    setUserDTO(user);
                    prefs.edit().putString("UserId",user.getUserId()).apply();

                    Intent i = new Intent(ctx, Activity_Main.class);
                    controller.setCurrUser(user);

                    startActivity(i);
                    finish();
                }
            }, thisIntent.getStringExtra("userId"));

        }

    }

    private void setUserDTO(UserDTO dto){
        this.userDTO = dto;
    }


}