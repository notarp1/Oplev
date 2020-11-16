package com.A4.oplev;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;

import com.google.firebase.FirebaseApp;

import java.util.ArrayList;
import java.util.List;

import Adapters.eventAdapter;
import DAL.Classes.UserDAO;
import DAL.DBAccess;
import DTO.EventDTO;
import DTO.UserDTO;

public class Activity_Main extends AppCompatActivity implements View.OnClickListener {

    ImageView options, match, user;
    RecyclerView rcEvent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //skal optimeres og ændres til at vi skal hente data ude fra.
        List<EventDTO> eventList = new ArrayList<>();
        EventDTO dto1,dto2;
        dto1 = new EventDTO().setOwner(12).setName("Stjæl en grusgrav");
        dto2 = new EventDTO().setOwner(13).setName("Svøm i tørvejr");
        eventList.add(dto1);
        eventList.add(dto2);
        eventAdapter eventAdapter = new eventAdapter(eventList);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        rcEvent = findViewById(R.id.eventRecycleView);
        rcEvent.setAdapter(eventAdapter);
        rcEvent.setLayoutManager(layoutManager);


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
        //getSupportFragmentManager().beginTransaction().replace(R.id.main_frag, new Startside_billede_frag())
         //       .commit();


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