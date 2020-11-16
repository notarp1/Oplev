package com.A4.oplev;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class Activity_U_Settings extends AppCompatActivity implements View.OnClickListener{
    ImageView back;
    static TextView title;


    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_u_settings);
        title = findViewById(R.id.topbar_text);
        back = findViewById(R.id.topbar_arrow);

        back.setOnClickListener(this);

        Intent intent = getIntent();


        Bundle bundle = new Bundle();
        bundle.putString("fName",intent.getStringExtra("fName"));
        bundle.putString("lName",intent.getStringExtra("lName"));
        bundle.putString("age",intent.getStringExtra("age"));
        bundle.putString("desc",intent.getStringExtra("desc"));
        bundle.putString("city",intent.getStringExtra("city"));

        Fragment fragment = new U_Settings_Main();
        fragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction().replace(R.id.mainFragmentBox, fragment, "uSettingMainBox")
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
