package com.example.oplev;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

public class Activity_U_Settings extends AppCompatActivity implements View.OnClickListener{
    ImageView back;
    static TextView title;


    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_u_settings);
        title = findViewById(R.id.topbar_text);
        back = findViewById(R.id.topbar_arrow);

        back.setOnClickListener(this);


        getSupportFragmentManager().beginTransaction().replace(R.id.mainFragmentBox, new U_Settings_Main(), "uSettingMainBox")
                .commit();

    }


    @Override
    public void onClick(View v) {
        if(v == back){
          if(getSupportFragmentManager().getBackStackEntryCount() > 0){
              getSupportFragmentManager().popBackStack();
          } else finish();
        }

    }
}
