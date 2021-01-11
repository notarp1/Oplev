package com.A4.oplev.SearchFilter;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.A4.oplev.R;
import com.A4.oplev.SearchFilter.Search_filter_frag;
import com.A4.oplev._Adapters.Event_Adapter;

import java.util.List;

import DAL.Classes.EventDAO;
import DAL.Interfaces.CallBackList;

public class Activity_Search_Filter extends AppCompatActivity implements View.OnClickListener {
    ImageView back;
    static TextView title;

    public void onCreate(Bundle saveInstanceState) {

        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_u_settings);
        title = findViewById(R.id.topbar_text);
        back = findViewById(R.id.topbar_arrow);

        back.setOnClickListener(this);

        title.setText("Søgefilter");


        getSupportFragmentManager().beginTransaction().replace(R.id.mainFragmentBox,  new Search_filter_frag(), "uSettingMainBox")
                .commit();

    }


    @Override
    public void onClick(View v) {
        if (v == back) {
            finish();
        }
    }

}
