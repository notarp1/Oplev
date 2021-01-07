package com.A4.oplev;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.A4.oplev.__Main.Activity_Main;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.Serializable;
import java.util.ArrayList;

import Controller.UserController;
import DAL.Interfaces.CallbackUser;
import DTO.UserDTO;

public class Activity_Ini extends AppCompatActivity implements Serializable {
    UserController userController;
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
        setContentView(R.layout.activity__ini);
        mAuth = FirebaseAuth.getInstance();

        //KØR NEDENSTÅENDE FOR AT RESETTE OG UDKOMMENTER EFTER
        //FirebaseAuth.getInstance().signOut();
        //PreferenceManager.getDefaultSharedPreferences(ctx).edit().clear().apply();



    }

    @Override
    public void onStart() {
        super.onStart();

        createNotificationChannel();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        userController = UserController.getInstance();
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

                userController.getUser(new CallbackUser() {
                    @Override
                    public void onCallback(UserDTO user) {
                        setUserDTO(user);
                        try {
                            prefs.edit().putString("userId",user.getUserId()).apply();
                        }catch (Exception e){
                            FirebaseAuth.getInstance().signOut();
                            PreferenceManager.getDefaultSharedPreferences(ctx).edit().clear().apply();
                        }
                        Intent i = new Intent(ctx, Activity_Main.class);
                        userController.setCurrUser(user);
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

    private void createNotificationChannel() {
        final String CHANNEL_ID = "0";
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}