package com.A4.oplev.SearchFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.A4.oplev.R;
import com.A4.oplev._Adapters.Event_Adapter;

import java.util.List;

import DAL.Classes.EventDAO;
import DAL.Interfaces.CallBackList;
import io.apptik.widget.MultiSlider;

public class Search_filter_frag extends Fragment{

    private SeekBar distanceBar;
    private MultiSlider ageBar;
    private Switch allSwitch, motionSwitch, underholdningSwitch, madDrikkeSwitch, kulturSwitch, musikNattelivSwitch, blivKlogereSwitch, gratisSwitch;
    private TextView ageText, distanceText;
    private int currDistance = 30;
    private int currMinAge = 18;
    private int currMaxAge = 99;

    public View onCreateView(LayoutInflater i, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View root = i.inflate(R.layout.search_filter_frag, container, false);

        // Load data from previous instance of app (age and distance) with SharedPreferences.
        loadData();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

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

        // Load switches (settings basically) from previous instances.
        updateSwitches();

        // Instantiating the distanceBar and distanceText in accordance with the method loadData().
        distanceText.setText(Integer.toString(currDistance)+ " km");
        distanceBar.setProgress(currDistance);


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
                prefs.edit().putInt("distance",currDistance).apply();
                onUpdate(prefs);
            }
        });

        ageText.setText(currMinAge + " - " + currMaxAge + " år");
        //update ageBar UI thumbs
        ageBar.getThumb(0).setValue(currMinAge);
        ageBar.getThumb(1).setValue(currMaxAge);
        ageBar.setOnThumbValueChangeListener(new MultiSlider.OnThumbValueChangeListener() {
            @Override
            public void onValueChanged(MultiSlider multiSlider,
                                       MultiSlider.Thumb thumb,
                                       int thumbIndex,
                                       int value) {
                if (thumbIndex == 0) {
                    currMinAge = (value);
                    prefs.edit().putInt("minAge",currMinAge).apply();

                } else {
                    currMaxAge = (value);
                    prefs.edit().putInt("maxAge",currMaxAge).apply();
                }
                ageText.setText(currMinAge + " - " + currMaxAge + " år");
            }
        });


        // Set onCheckedListener
        // Following listeners are all saving a boolean (search settings basiclly) which can be accessed through SharedPreferences.
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

                    prefs.edit().putBoolean("Motionswitch", motionSwitch.isChecked()).apply();
                    prefs.edit().putBoolean("underholdningSwitch", underholdningSwitch.isChecked()).apply();
                    prefs.edit().putBoolean("madDrikkeSwitch", madDrikkeSwitch.isChecked()).apply();
                    prefs.edit().putBoolean("kulturSwitch", kulturSwitch.isChecked()).apply();
                    prefs.edit().putBoolean("musikNattelivSwitch", musikNattelivSwitch.isChecked()).apply();
                    prefs.edit().putBoolean("blivKlogereSwitch", blivKlogereSwitch.isChecked()).apply();
                    prefs.edit().putBoolean("gratisSwitch", gratisSwitch.isChecked()).apply();
                    onUpdate(prefs);
                }
            }
        });

        motionSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                prefs.edit().putBoolean("motionswitch", motionSwitch.isChecked()).apply();
                onUpdate(prefs);
            }
        });

        underholdningSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                prefs.edit().putBoolean("underholdningSwitch", underholdningSwitch.isChecked()).apply();
                onUpdate(prefs);
            }
        });

        madDrikkeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                prefs.edit().putBoolean("madDrikkeSwitch", madDrikkeSwitch.isChecked()).apply();
                onUpdate(prefs);
            }
        });


        kulturSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                prefs.edit().putBoolean("kulturSwitch", kulturSwitch.isChecked()).apply();
                onUpdate(prefs);
            }
        });

        musikNattelivSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                prefs.edit().putBoolean("musikNattelivSwitch", musikNattelivSwitch.isChecked()).apply();
                onUpdate(prefs);
            }
        });


        blivKlogereSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                prefs.edit().putBoolean("blivKlogereSwitch", blivKlogereSwitch.isChecked()).apply();
                onUpdate(prefs);
            }
        });

        gratisSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                prefs.edit().putBoolean("gratisSwitch", gratisSwitch.isChecked()).apply();
                onUpdate(prefs);
            }
        });

            return root;
    }

    private void onUpdate(SharedPreferences prefs){
        EventDAO dataA = new EventDAO();
        dataA.getEventIDs(new CallBackList() {
            @Override
            public void onCallback(List<String> list) {
                Event_Adapter event_adapter = Event_Adapter.getInstance();
                event_adapter.refreshData(list);
            }
        },prefs);
    }

    public void loadData(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        currDistance = prefs.getInt("distance",150);
        currMinAge = prefs.getInt("minAge", 18);
        currMaxAge = prefs.getInt("maxAge", 99);
    }

    public void updateSwitches(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        motionSwitch.setChecked(prefs.getBoolean("motionswitch",true));
        underholdningSwitch.setChecked(prefs.getBoolean("underholdningSwitch",true));
        madDrikkeSwitch.setChecked(prefs.getBoolean("madDrikkeSwitch",true));
        kulturSwitch.setChecked(prefs.getBoolean("kulturSwitch",true));
        musikNattelivSwitch.setChecked(prefs.getBoolean("musikNattelivSwitch",true));
        blivKlogereSwitch.setChecked(prefs.getBoolean("blivKlogereSwitch",true));
        gratisSwitch.setChecked(prefs.getBoolean("gratisSwitch",true));

        System.out.println(prefs.getBoolean("gratisSwitch",true));
    }
}

