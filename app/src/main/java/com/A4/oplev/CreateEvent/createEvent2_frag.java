package com.A4.oplev.CreateEvent;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.A4.oplev.R;

import Controller.EventController;
import io.apptik.widget.MultiSlider;

public class createEvent2_frag extends Fragment implements View.OnClickListener {
    //topbar element
    TextView topbar_txt;
    //frag element
    TextView info_txt, age_txt, ageVal_txt, sex_txt, female_txt, male_txt;
    Switch femaleSwitch, maleSwitch;
    Button done_btn;
    MultiSlider age_bar;
    //min and max values for age is set here
    String currMinAge = "18", currMaxAge = "99";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.create_event2_frag, container, false);

        //get fragment elements
        femaleSwitch = root.findViewById(R.id.create2_female_switch);
        maleSwitch = root.findViewById(R.id.create2_male_switch);
        done_btn = root.findViewById(R.id.create2_done_btn);

        done_btn.setOnClickListener(this);

        //setup the rangebar and its textview.
        ageVal_txt = root.findViewById(R.id.create2_ageInterval_txt);
        ageVal_txt.setText(currMinAge + " - " + currMaxAge);
        age_bar = root.findViewById(R.id.create2_rangebar);
        age_bar.setMin(Integer.parseInt(currMinAge));
        age_bar.setMax(Integer.parseInt(currMaxAge));
        //following listener will run when either of the thumbs on range bar is moved
        age_bar.setOnThumbValueChangeListener(new MultiSlider.OnThumbValueChangeListener() {
            @Override
            public void onValueChanged(MultiSlider multiSlider,
                                       MultiSlider.Thumb thumb,
                                       int thumbIndex,
                                       int value)
            {
                if (thumbIndex == 0) {
                    // the left thumb was moved - set value
                    currMinAge = (String.valueOf(value));
                } else {
                    //the right thumb was moved - set value
                    currMaxAge = (String.valueOf(value));
                }
                //update textview in UI to show new values.
                ageVal_txt.setText(currMinAge + " - " + currMaxAge);
            }
        });



        return root;
    }

    @Override
    public void onClick(View v) {
        if(v == done_btn){
            if(maleSwitch.isChecked() || femaleSwitch.isChecked()) { //make sure gender chosen
                /*
                 * Parse values to controller which will create an event object and add it to program/database
                 * get values from last frag using getArg as below:
                 */
                EventController.getInstance().createEvent(
                        getArguments().getString("title_in"),
                        getArguments().getString("desc_in"),
                        getArguments().getString("price_in"),
                        getArguments().getString("date_in"),
                        getArguments().getString("time_in"),
                        getArguments().getString("city_in"),
                        getArguments().getString("type_in"),
                        currMinAge,
                        currMaxAge,
                        maleSwitch.isChecked(),
                        femaleSwitch.isChecked(),
                        ((Activity_Create_Event) getActivity()).getPickedImgUri()
                );


                //remove last frag from backstack
                //(shouldnt be able to change event settings when "done" is pressed)


                //transaction to final create frag
                getFragmentManager().beginTransaction()
                        .replace(R.id.mainFragmentBox, new createEvent3_frag())
                        .addToBackStack(null)
                        .commit();
            }
            else{
                //no gender chosen
                Toast.makeText(getActivity(), "Vælg mindst et køn", Toast.LENGTH_SHORT).show();
            }

        }
    }
}