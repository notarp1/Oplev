package com.example.oplev;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class Likeside_frag extends Fragment implements View.OnClickListener{
    ImageView hjerte, besked, tilbage;

    public View onCreateView(LayoutInflater i, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View root = i.inflate(R.layout.likeside, container, false);

        hjerte = root.findViewById(R.id.likeside_hjertbillede);
        besked = root.findViewById(R.id.likeside_beskedbillede);
        tilbage = root.findViewById(R.id.topbar_arrow);

        getFragmentManager().beginTransaction().replace(R.id.likeside_frameLayout,new LikesideList_frag())
                .commit();

        hjerte.setOnClickListener(this);
        besked.setOnClickListener(this);
        tilbage.setOnClickListener(this);




        return root;
    }


    @Override
    public void onClick(View v) {
        if (v == hjerte){
            getFragmentManager().beginTransaction().replace(R.id.likeside_frameLayout,new HjerteSide_frag())
                    .addToBackStack(null)
                    .commit();
        }
        else if (v == besked){
            getFragmentManager().beginTransaction().replace(R.id.likeside_frameLayout,new LikesideList_frag())
                    .addToBackStack(null)
                    .commit();
        }
        else if (v == tilbage){
            getFragmentManager().popBackStack();
        }

    }
}