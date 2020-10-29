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
    ListView listView;
    ImageView hjerte, besked, tilbage;

    public View onCreateView(LayoutInflater i, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View root = i.inflate(R.layout.likeside, container, false);

        hjerte = root.findViewById(R.id.likeside_hjertbillede);
        besked = root.findViewById(R.id.likeside_beskedbillede);
        tilbage = root.findViewById(R.id.topbar_arrow);

        String[] navne = {"John", "abc", "Bente", "AGE", "Yes", "whoDis?", "yubrakit yubotit"};
        String[] dato = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};

        ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.besked_liste_element, R.id.beskeder_overskrift, navne);

        getFragmentManager().beginTransaction().replace(R.id.opretopslag_box, new Create_Button_frag())
                //.addToBackStack(null)
                .commit();


        listView = root.findViewById(R.id.beskedListView);
        //listView.setOnItemClickListener(this);
        listView.setAdapter(adapter);

        hjerte.setOnClickListener(this);
        besked.setOnClickListener(this);
        tilbage.setOnClickListener(this);




        return root;
    }


    @Override
    public void onClick(View v) {
        if (v == hjerte){
            listView.setAdapter(null);
            getFragmentManager().beginTransaction().replace(R.id.likeside_frameLayout,new HjerteSide_frag())
                    .addToBackStack(null)
                    .commit();
        }
        else if (v == besked){
            getFragmentManager().beginTransaction().replace(R.id.likeside_frameLayout,new Likeside_frag())
                    .commit();
        }
        else if (v == tilbage){
            getFragmentManager().popBackStack();
        }

    }
}
