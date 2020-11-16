package com.A4.oplev.UserSettings;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.A4.oplev.R;

import Controller.Controller;
import DTO.UserDTO;

public class U_Settings_Edit extends Fragment implements View.OnClickListener {

    TextView textview;
    ImageView accept;
    public EditText about, city, job, education;
    Controller controller;



    public View onCreateView(LayoutInflater i, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View root = i.inflate(R.layout.u_setting_edit_frag, container, false);

        controller =  Controller.getInstance();

        textview = (TextView)getActivity().findViewById(R.id.topbar_text);
        textview.setText("Rediger Profil");

        accept = (ImageView) getActivity().findViewById(R.id.imageView_checkmark);
        accept.setVisibility(View.VISIBLE);
        accept.setOnClickListener(this);

        about = root.findViewById(R.id.editText_description);
        city = root.findViewById(R.id.editText_city);
        job = root.findViewById(R.id.editText_job);
        education = root.findViewById(R.id.editText_edu);

        controller.iniEditProfil(this);



        return root;


    }


    @Override
    public void onClick(View v) {

        if(v == accept){
            controller.updateUser(this);
            accept.setVisibility(View.INVISIBLE);
            getActivity().getSupportFragmentManager().popBackStack();


        }

    }
}
