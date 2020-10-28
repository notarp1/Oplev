package com.example.oplev;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

public class Likeside_frag extends Fragment implements View.OnClickListener{
    ListView listView;

    public View onCreateView(LayoutInflater i, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View root = i.inflate(R.layout.likeside, container, false);

        String[] navne = {"John", "abc", "Bente", "AGE", "Yes", "whoDis?", "yubrakit yubotit"};
        String[] dato = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};

        ArrayAdapter adapter = new ArrayAdapter(getContext(), R.layout.besked_liste_element, R.id.beskeder_overskrift, navne);

        listView = root.findViewById(R.id.beskedListView);
        //listView.setOnItemClickListener(this);
        listView.setAdapter(adapter);

        return root;
    }


    @Override
    public void onClick(View v) {

    }
}
