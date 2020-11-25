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
        // uses the same topbar with empty frag below as u_settings...
        setContentView(R.layout.activity_u_settings);
        // get elements from activity
        title = findViewById(R.id.topbar_text);
        back = findViewById(R.id.topbar_arrow);
        //set onlick listner
        back.setOnClickListener(this);

        //set value of title text in topbar
        title.setText("Opret Event");

        //fill fragment holder with createvent 1
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
