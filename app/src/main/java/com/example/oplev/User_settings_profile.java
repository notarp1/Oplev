package com.example.oplev;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

public class User_settings_profile extends Fragment implements View.OnClickListener {
    static Button btn_insta;
    public View onCreateView(LayoutInflater i, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //btn_insta.setOnClickListener(this);

        return  i.inflate(R.layout.u_settings_profile, container, false);


    }

    @Override
    public void onClick(View view) {
        if(view == btn_insta){

        }
    }
}
