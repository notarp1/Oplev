package com.example.oplev;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Search_filter extends AppCompatActivity implements View.OnClickListener {
    ImageView back;
    static TextView title;

    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_u_settings);
        title = findViewById(R.id.topbar_text);
        back = findViewById(R.id.topbar_arrow);

        back.setOnClickListener(this);

        title.setText("Søgefilter");

        getSupportFragmentManager().beginTransaction().replace(R.id.mainFragmentBox,  new Search_filter_bottom_frag(), "uSettingMainBox")
                .commit();

    }



    @Override
    public void onClick(View v) {
        if (v == back) {
            getFragmentManager().popBackStack();

        }
    }
}
