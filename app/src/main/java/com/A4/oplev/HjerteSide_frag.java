package com.A4.oplev;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

public class HjerteSide_frag extends Fragment implements View.OnClickListener{
    ListView listView;


    public View onCreateView(LayoutInflater i, ViewGroup container, Bundle savedInstanceState) {
        View root = i.inflate(R.layout.hjerteside_frag,container,false);

        String[] navne = {"John", "abc", "Bente", "AGE", "Yes", "whoDis?", "yubrakit yubotit"};
        String[] dato = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};

        ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.hjerteside_liste_element, R.id.hjertside_overskrift, navne);

        listView = root.findViewById(R.id.hjerteside_listview);
        //listView.setOnItemClickListener(this);
        listView.setAdapter(adapter);


        return root;
    }

        @Override
    public void onClick(View v) {

    }
}
