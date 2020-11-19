package com.A4.oplev.SearchFilter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.A4.oplev.R;

import io.apptik.widget.MultiSlider;

public class Search_filter_frag extends Fragment{
    // todo Beslut hvordan searchfilter data skal vidergives til resten af appen. (kostruktor, indholdende boolean værdier?)
    boolean motion, underholdning, madDrikke, kultur, musikNatteliv, blivKlogere, gratis;

    public Search_filter_frag() {}

    SeekBar distanceBar;
    MultiSlider ageBar;
    Switch allSwitch, motionSwitch, underholdningSwitch, madDrikkeSwitch, kulturSwitch, musikNattelivSwitch, blivKlogereSwitch, gratisSwitch;
    TextView ageText, distanceText;
    int currDistance = 30;
    int currMinAge = 18;
    int currMaxAge = 99;

    public View onCreateView(LayoutInflater i, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View root = i.inflate(R.layout.search_filter_frag2, container, false);

        distanceBar = root.findViewById(R.id.sf_seekBarDistance);
        ageBar = root.findViewById(R.id.sf_seekBarAge);
        allSwitch = root.findViewById(R.id.sf_switchALLActivities);
        motionSwitch = root.findViewById(R.id.sf_switchMotion);
        underholdningSwitch = root.findViewById(R.id.sf_switchUnderholdning);
        madDrikkeSwitch = root.findViewById(R.id.sf_switchMadDrikke);
        kulturSwitch = root.findViewById(R.id.sf_switchKultur);
        musikNattelivSwitch = root.findViewById(R.id.sf_switchMusikogNatteliv);
        blivKlogereSwitch = root.findViewById(R.id.sf_switchBlivKlogere);
        gratisSwitch = root.findViewById(R.id.sf_switchGratis);
        ageText = root.findViewById(R.id.sf_textViewAgeVal);
        distanceText = root.findViewById(R.id.sf_textViewDistanceVal);

        distanceText.setText(Integer.toString(currDistance)+ "km");

        distanceBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                distanceText.setText(progress + "km");
                currDistance = progress;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                distanceBar.setProgress(currDistance);
                System.out.println(currDistance);
            }
        });

        ageText.setText(currMinAge + " - " + currMaxAge + "år");
        ageBar.setMin(currMinAge);
        ageBar.setMax(currMaxAge);
        ageBar.setOnThumbValueChangeListener(new MultiSlider.OnThumbValueChangeListener() {
            @Override
            public void onValueChanged(MultiSlider multiSlider,
                                       MultiSlider.Thumb thumb,
                                       int thumbIndex,
                                       int value) {
                if (thumbIndex == 0) {
                    currMinAge = (value);
                } else {
                    currMaxAge = (value);
                }
                ageText.setText(currMinAge + " - " + currMaxAge);

            }
        });



        // set onCheckedListener
        allSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if(isChecked){
                    motionSwitch.setChecked(true);
                    underholdningSwitch.setChecked(true);
                    madDrikkeSwitch.setChecked(true);
                    kulturSwitch.setChecked(true);
                    musikNattelivSwitch.setChecked(true);
                    blivKlogereSwitch.setChecked(true);
                    gratisSwitch.setChecked(true);
                }
            }
        });

        motionSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if(isChecked){
                    motion = true;
                }
                else motion = false;
            }
        });

        underholdningSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if(isChecked){
                    underholdning = true;
                }
                else underholdning = false;
            }
        });

        madDrikkeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if(isChecked){
                    madDrikke = true;
                }
                else madDrikke = false;
            }
        });


        kulturSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if(isChecked){
                    kultur = true;
                }
                else kultur = false;
            }
        });

        musikNattelivSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if(isChecked){
                     musikNatteliv = true;
                }
                else musikNatteliv = false;
            }
        });


        blivKlogereSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if(isChecked){
                    blivKlogere = true;
                }
                else blivKlogere = false;
            }
        });

        gratisSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if(isChecked){
                    gratis = true;
                }
                else gratis = false;
            }
        });


            return root;
    }
}
