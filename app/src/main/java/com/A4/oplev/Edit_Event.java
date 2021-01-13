package com.A4.oplev;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Date;

import DAL.Classes.EventDAO;
import DTO.EventDTO;

public class Edit_Event extends AppCompatActivity implements View.OnClickListener {
    TextView date, time, desc, titel, city, price;
    Spinner type;
    Button btn_save;
    EventDTO eventDTO;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__event);

        date = findViewById(R.id.edit_event_date_input);
        time = findViewById(R.id.edit_event_time_input);
        desc = findViewById(R.id.edit_event_desc_input);
        titel = findViewById(R.id.edit_event_title_input);
        city = findViewById(R.id.edit_event_city_input);
        price = findViewById(R.id.edit_event_price_input);
        btn_save = findViewById(R.id.edit_event_next_btn);
        //type = findViewById(R.id.);
        eventDTO = (EventDTO)getIntent().getSerializableExtra("EventDTO");




       // type.setText(eventDTO.getType());
        price.setText(eventDTO.getPrice());
        desc.setText(eventDTO.getDescription());
        titel.setText(eventDTO.getTitle());
        city.setText(eventDTO.getTitle());
        date.setText(eventDTO.getDate().getDate() + "/"+eventDTO.getDate().getMonth() + "/" + eventDTO.getDate().getYear());
        time.setText(eventDTO.getDate().getHours() + ":"+ eventDTO.getDate().getMinutes());

        //ArrayAdapter<CharSequence> dropDownAdapter = ArrayAdapter.createFromResource(getContext(),R.array.createDropDown, R.layout.support_simple_spinner_dropdown_item);
       // dropDownAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);



        btn_save.setOnClickListener(this);
    }




    @Override
    public void onClick(View view) {
        if(view == btn_save){
            eventDTO.setPrice(Integer.parseInt(price.getText().toString()));
            eventDTO.setDescription(desc.getText().toString());
            eventDTO.setTitle(titel.getText().toString());
            eventDTO.setCity(city.getText().toString());

            Date date_ = new Date();
            String[] sHM = time.getText().toString().split(":");
            String[] sDate = date.getText().toString().split("/");
            date_.setDate(Integer.parseInt(sDate[0]));
            date_.setMonth(Integer.parseInt(sDate[1]));
            date_.setYear(Integer.parseInt(sDate[2]));
            date_.setHours(Integer.parseInt(sHM[0]));
            date_.setMinutes(Integer.parseInt(sHM[1]));
            eventDTO.setDate(date_);
           // eventDTO.setType((String) type.getText());
            EventDAO eventDAO = new EventDAO();
            eventDAO.updateEvent(eventDTO);

        }
    }
}