package com.example.oplev;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

public class Create_Button_frag extends Fragment implements View.OnClickListener {
    Button opret;

    public View onCreateView(LayoutInflater i, ViewGroup container, Bundle savedInstanceState) {
        View rod = i.inflate(R.layout.nytopslag_knap, container, false);

        opret = rod.findViewById(R.id.opretOpslag_Knap);

        opret.setOnClickListener(this);

        return rod;
    }



        @Override
    public void onClick(View v) {
        if (v == opret){
            // do something
            getFragmentManager().beginTransaction()
                    .replace(R.id.main_frag, new createEvent1_frag())
                    .addToBackStack(null)
                    .commit();
        }

    }
}
