package com.example.oplev;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class User_Settings_Opt extends Fragment implements View.OnClickListener {


    public View onCreateView(LayoutInflater i, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View root = i.inflate(R.layout.u_setting_setting_frag, container, false);


        return root;


    }


    @Override
    public void onClick(View view) {

    }
}
