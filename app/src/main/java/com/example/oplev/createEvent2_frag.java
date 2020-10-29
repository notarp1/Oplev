package com.example.oplev;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;


public class createEvent2_frag extends Fragment implements View.OnClickListener {
    //topbar element
    TextView topbar_txt;
    //frag element
    TextView info_txt, age_txt, ageVal_txt, sex_txt, female_txt, male_txt;
    SeekBar age_bar;
    Switch femaleSwitch, maleSwitch;
    Button done_btn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_create_event2_frag, container, false);
        //set topbar text
        //topbar_txt = root.findViewById(R.id.topbar_text);
        //topbar_txt.setText("Sidste step");

        //get frag elements
        ageVal_txt = root.findViewById(R.id.create2_ageVal_txt);
        femaleSwitch = root.findViewById(R.id.create2_female_switch);
        maleSwitch = root.findViewById(R.id.create2_male_switch);
        done_btn = root.findViewById(R.id.create2_done_btn);

        done_btn.setOnClickListener(this);


        return root;
    }

    @Override
    public void onClick(View v) {
        if(v == done_btn){
            /*
             * Create an event object and add it to program
             * get values from last frag using getArg as below:
             */
            /*getArguments().getString("title_in");
            getArguments().getString("desc_in");
            getArguments().getString("date_in");
            getArguments().getString("city_in");
            getArguments().getInt("price_in");
            */

            //remove last frag from backstack
            //(shouldnt be able to change event settings when "done" is pressed)
            getFragmentManager().popBackStack();
            //transaction to final create frag
            getFragmentManager().beginTransaction()
                    .replace(R.id.mainFragmentBox, new createEvent3_frag())
                    .commit();
        }
    }
}