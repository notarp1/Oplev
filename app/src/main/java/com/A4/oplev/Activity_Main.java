package com.A4.oplev;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.FirebaseApp;

import java.util.ArrayList;
import java.util.Date;

import DAL.Classes.ChatDAO;
import DAL.Classes.UserDAO;
import DAL.DBAccess;
import DTO.ChatDTO;
import DTO.UserDTO;

public class Activity_Main extends AppCompatActivity implements View.OnClickListener {

    ImageView options, match, user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        options = findViewById(R.id.options_btn);
        match = findViewById(R.id.match_btn);
        user = findViewById(R.id.user_btn);


        options.setOnClickListener(this);
        match.setOnClickListener(this);
        user.setOnClickListener(this);

        /*
        UserDAO dao = new UserDAO();
        UserDTO test = new UserDTO();
        test.setAge(18);
        test.setCity("Odense");
        test.setDescription("Jeg er en sød gut");
        test.setEmail("chrisi@gmail.com");
        test.setfName("Jonas");
        test.setlName("Henriksen");
        test.setPhone("83827312");
        dao.createUser(test); */

        ChatDAO dao = new ChatDAO();
        ArrayList<Date> dates = new ArrayList<>();
        dates.add(new Date());
        dates.add(new Date());
        dates.add(new Date());

        ArrayList<String> messages = new ArrayList<>();
        messages.add("hejsa");
        messages.add("hejsa2");
        messages.add("hejsa3");
        ChatDTO dto = new ChatDTO();
        dto.setDates(dates);
        dto.setChatId(1);
        dto.setMessages(messages);
        dto.setReceiver("johnny");
        dto.setSender("benny");
        dao.createChat(dto);


        getSupportFragmentManager().beginTransaction().replace(R.id.main_frag, new Startside_billede_frag())
                .commit();




    }

    @Override
    public void onClick(View v) {
        if(v == options){
            Intent i = new Intent(this, Activity_Search_Filter.class);
            startActivity(i);

        } else if(v == match){

            Intent i = new Intent(this, Activity_Likeside.class);
            startActivity(i);

        } else if(v == user){

            Intent i = new Intent(this, Activity_U_Settings.class);
            startActivity(i);

        }
    }
}