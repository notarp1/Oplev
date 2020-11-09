package com.A4.oplev;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class U_Settings_Edit extends Fragment implements View.OnClickListener {


    public View onCreateView(LayoutInflater i, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View root = i.inflate(R.layout.u_setting_edit_frag, container, false);



        TextView textview = (TextView)getActivity().findViewById(R.id.topbar_text);
        textview.setText("Rediger Profil");


        return root;


    }


    @Override
    public void onClick(View view) {

    }
}
