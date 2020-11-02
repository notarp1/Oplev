package com.example.oplev;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class Likeside_frag extends Fragment implements View.OnClickListener{
    ImageView hjerte, besked, tilbage;
    Button opret;

    public View onCreateView(LayoutInflater i, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View root = i.inflate(R.layout.likeside, container, false);
        opret = root.findViewById(R.id.opretOpslag_Knap2);

        hjerte = root.findViewById(R.id.likeside_hjertbillede);
        besked = root.findViewById(R.id.likeside_beskedbillede);
        tilbage = root.findViewById(R.id.topbar_arrow);

        getFragmentManager().beginTransaction().replace(R.id.likeside_frameLayout,new LikesideList_frag())
                .commit();

        hjerte.setOnClickListener(this);
        besked.setOnClickListener(this);
        tilbage.setOnClickListener(this);
        opret.setOnClickListener(this);

        hjerte.setBackgroundColor(Color.LTGRAY);


        return root;
    }


    @Override
    public void onClick(View v) {
        if (v == hjerte){
            besked.setBackgroundColor(Color.LTGRAY);
            hjerte.setBackgroundColor(Color.CYAN);
            getFragmentManager().beginTransaction().replace(R.id.likeside_frameLayout,new HjerteSide_frag())
                    .commit();
        }
        else if (v == besked){
            besked.setBackgroundColor(Color.CYAN);
            hjerte.setBackgroundColor(Color.LTGRAY);
            getFragmentManager().beginTransaction().replace(R.id.likeside_frameLayout,new LikesideList_frag())
                    .commit();
        }
        else if (v == tilbage){
            getFragmentManager().popBackStack();
        }
        else if(v == opret){
            Intent o = new Intent(getActivity(), Activity_Create_Event.class);
            startActivity(o);
        }

    }
}
