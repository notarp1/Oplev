package com.example.oplev;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

public class U_setting extends AppCompatActivity implements View.OnClickListener{
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

    public static void changeTitle(String name){
        title.setText(name);
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
