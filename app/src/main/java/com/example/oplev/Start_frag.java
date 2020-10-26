package com.example.oplev;

import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class Start_frag extends Fragment implements View.OnClickListener {

    ImageView options, match, user;

    @Override
    public View onCreateView(LayoutInflater i, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View root = i.inflate(R.layout.start_frag, container, false);

        options = root.findViewById(R.id.options_btn);
        match = root.findViewById(R.id.match_btn);
        user = root.findViewById(R.id.user_btn);

        options.setOnClickListener(this);
        match.setOnClickListener(this);
        user.setOnClickListener(this);


        return root;


    }

    @Override
    public void onClick(View v) {
        if(v == options){

        } else if(v == match){
            System.out.println("hello there");
            getFragmentManager().beginTransaction()
                    .replace(R.id.main_frag, new Likeside_frag())
                    .addToBackStack(null)
                    .commit();

        } else if(v == user){

        getFragmentManager().beginTransaction()
                .replace(R.id.main_frag, new User_Settings())
                .addToBackStack(null)
                .commit();

        }
    }
}