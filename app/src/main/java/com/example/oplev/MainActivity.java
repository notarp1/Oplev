package com.example.oplev;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            Fragment fragment = new Start_frag();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.main_frag, fragment)  // tom container i layout
                    .commit();
        }




    }
}