package com.A4.oplev;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.A4.oplev.R;
import com.A4.oplev.__Main.Activity_Main;

import java.io.Serializable;

import Controller.Controller;
import DAL.Interfaces.CallbackUser;
import DTO.UserDTO;

public class Activity_Ini extends AppCompatActivity implements Serializable {
    Controller controller;
    UserDTO userDTO;
    SharedPreferences prefs;
    Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__ini);

        controller = Controller.getInstance();
        ctx = this;
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        controller.getUser(new CallbackUser() {
            @Override
            public void onCallback(UserDTO user) {
                setUserDTO(user);
                prefs.edit().putString("UserID",user.getUserId()).apply();

                Intent i = new Intent(ctx, Activity_Main.class);
                controller.setCurrUser(user);

                startActivity(i);
            }
        }, "KHbc7vhvqHEd2bLjgTZM");



    }

    private void setUserDTO(UserDTO dto){
        this.userDTO = dto;
    }


}