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
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intent = getIntent();

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

/*
        ChatDAO dao = new ChatDAO();
        ChatDTO dto = new ChatDTO();
        ArrayList<Date> dates = new ArrayList<>();
        ArrayList<String> messages = new ArrayList<>();
        ArrayList<String> reciever = new ArrayList<>();
        ArrayList<String> sender = new ArrayList<>();


        for (int i = 0; i<3;i++) {
            messages.add("Hej"+(i+1));
            dates.add(new Date());
            if (i % 2 == 0){
                reciever.add("person2");
                sender.add("person1");
            }
            else {
                reciever.add("person1");
                sender.add("person2");
            }

        }

        dto.setMessages(messages);
        dto.setDates(dates);
        dto.setReceiver(reciever);
        dto.setSender(sender);
        dao.createChat(dto);

 */


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
            i.putExtra("fName",intent.getStringExtra("fName"));
            i.putExtra("lName",intent.getStringExtra("lName"));
            i.putExtra("age",intent.getStringExtra("age"));
            i.putExtra("desc",intent.getStringExtra("desc"));
            i.putExtra("city",intent.getStringExtra("city"));
            startActivity(i);

        }
    }
}