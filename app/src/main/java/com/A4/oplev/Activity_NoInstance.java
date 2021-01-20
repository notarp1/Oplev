package com.A4.oplev;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.A4.oplev.Login.Activity_Login;

public class Activity_NoInstance extends AppCompatActivity implements View.OnClickListener {

    Button create;
    TextView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__no_instance);

        create = findViewById(R.id.button_goCreate);
        back = findViewById(R.id.text_back);

        create.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == create){
            Intent i =  new Intent(this, Activity_Login.class);
            startActivity(i);
            finish();
        } else if (view == back){
            finish();
        }
    }
}