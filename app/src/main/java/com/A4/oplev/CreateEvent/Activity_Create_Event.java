package com.A4.oplev.CreateEvent;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.A4.oplev.CreateEvent.createEvent1_frag;
import com.A4.oplev.R;

public class Activity_Create_Event extends AppCompatActivity implements View.OnClickListener {
    ImageView back;
    static TextView title;

    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_u_settings);


        title = findViewById(R.id.topbar_text);
        back = findViewById(R.id.topbar_arrow);

        back.setOnClickListener(this);

        title.setText("Opret Event");

        getSupportFragmentManager().beginTransaction().replace(R.id.mainFragmentBox,  new createEvent1_frag(), "uSettingMainBox")
                .commit();

    }
    @Override
    public void onClick(View v) {
        if (v == back) {
           finish();
        }
    }
}
