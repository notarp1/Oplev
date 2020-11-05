package com.example.oplev;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

public class Startside_billede_frag extends Fragment{
    ImageView billede;

    public View onCreateView(LayoutInflater i, ViewGroup container, Bundle savedInstanceState) {
        View rod = i.inflate(R.layout.startside_billede_element,container,false);

        billede = rod.findViewById(R.id.startSide_Billede);
        billede.setImageResource(R.drawable.icecrim);


        return rod;
    }
}
