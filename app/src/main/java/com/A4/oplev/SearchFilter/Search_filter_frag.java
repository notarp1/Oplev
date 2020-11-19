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

public class Search_filter_frag extends Fragment implements View.OnClickListener {
    // todo Beslut hvordan searchfilter data skal vidergives til resten af appen. (kostruktor, indholdende boolean værdier?)
    boolean motion, underholdning, madDrikke, kultur, musikUnderholdning, blivKlogere, gratis;

    public Search_filter_frag() {
    }

    SeekBar distanceBar;
    MultiSlider ageBar;
    Switch allSwitch, motionSwitch, underholdningSwitch, madDrikkeSwitch, kulturSwitch, musikNattelivSwitch, blivKlogereSwitch, gratisSwitch;
    TextView ageText, distanceText;
    String currDistance = "30 km";
    String currMinAge = "18", currMaxAge = "99";

    public View onCreateView(LayoutInflater i, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View root = i.inflate(R.layout.search_filter_frag2, container, false);

        distanceBar = root.findViewById(R.id.sf_seekBarDistance);
        ageBar = root.findViewById(R.id.sf_seekBarAge);
        allSwitch = root.findViewById(R.id.sf_switchALLActivities);
        motionSwitch = root.findViewById(R.id.sf_switchMotion);
        underholdningSwitch = root.findViewById(R.id.sf_switchUnderholdning);
        madDrikkeSwitch = root.findViewById(R.id.sf_switchMad);
        kulturSwitch = root.findViewById(R.id.sf_switchKultur);
        musikNattelivSwitch = root.findViewById(R.id.sf_switchMusikogNatteliv);
        blivKlogereSwitch = root.findViewById(R.id.sf_switchBlivKlogere);
        gratisSwitch = root.findViewById(R.id.sf_switchGratis);
        ageText = root.findViewById(R.id.sf_textViewAgeVal);
        distanceText = root.findViewById(R.id.sf_textViewDistanceVal);


        distanceText.setText(currDistance);
        distanceBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                distanceText.setText(progress + "km");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        ageText.setText(currMinAge + " - " + currMaxAge);
        ageBar.setMin(Integer.parseInt(currMinAge));
        ageBar.setMax(Integer.parseInt(currMaxAge));
        ageBar.setOnThumbValueChangeListener(new MultiSlider.OnThumbValueChangeListener() {
            @Override
            public void onValueChanged(MultiSlider multiSlider,
                                       MultiSlider.Thumb thumb,
                                       int thumbIndex,
                                       int value) {
                if (thumbIndex == 0) {
                    currMinAge = (String.valueOf(value));
                } else {
                    currMaxAge = (String.valueOf(value));
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




        underholdningSwitch.setOnCheckedChangeListener((CompoundButton.OnCheckedChangeListener) this);
        madDrikkeSwitch.setOnCheckedChangeListener((CompoundButton.OnCheckedChangeListener) this);
        kulturSwitch.setOnCheckedChangeListener((CompoundButton.OnCheckedChangeListener) this);
        musikNattelivSwitch.setOnCheckedChangeListener((CompoundButton.OnCheckedChangeListener) this);
        blivKlogereSwitch.setOnCheckedChangeListener((CompoundButton.OnCheckedChangeListener) this);
        gratisSwitch.setOnCheckedChangeListener((CompoundButton.OnCheckedChangeListener) this);


            return root;
    }


    @Override
    public void onClick(View v) {

    }
}
