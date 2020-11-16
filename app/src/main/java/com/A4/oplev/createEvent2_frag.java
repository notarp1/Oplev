package com.A4.oplev;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import io.apptik.widget.MultiSlider;

public class createEvent2_frag extends Fragment implements View.OnClickListener {
    //topbar element
    TextView topbar_txt;
    //frag element
    TextView info_txt, age_txt, ageVal_txt, sex_txt, female_txt, male_txt;
    Switch femaleSwitch, maleSwitch;
    Button done_btn;
    MultiSlider age_bar;

    String currMinAge = "18", currMaxAge = "99";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.create_event2_frag, container, false);

        //get frag elements

        femaleSwitch = root.findViewById(R.id.create2_female_switch);
        maleSwitch = root.findViewById(R.id.create2_male_switch);
        done_btn = root.findViewById(R.id.create2_done_btn);

        done_btn.setOnClickListener(this);

        //
        ageVal_txt = root.findViewById(R.id.create2_ageInterval_txt);
        ageVal_txt.setText(currMinAge + " - " + currMaxAge);
        age_bar = root.findViewById(R.id.create2_rangebar);
        age_bar.setMin(Integer.parseInt(currMinAge));
        age_bar.setMax(Integer.parseInt(currMaxAge));
        age_bar.setOnThumbValueChangeListener(new MultiSlider.OnThumbValueChangeListener() {
            @Override
            public void onValueChanged(MultiSlider multiSlider,
                                       MultiSlider.Thumb thumb,
                                       int thumbIndex,
                                       int value)
            {
                if (thumbIndex == 0) {
                    currMinAge = (String.valueOf(value));
                } else {
                    currMaxAge = (String.valueOf(value));
                }
                ageVal_txt.setText(currMinAge + " - " + currMaxAge);
            }
        });

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


            //transaction to final create frag
            getFragmentManager().beginTransaction()
                    .replace(R.id.mainFragmentBox, new createEvent3_frag())
                    .addToBackStack(null)
                    .commit();


        }
    }
}