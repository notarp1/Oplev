package com.A4.oplev;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import DTO.UserDTO;

public class U_Settings_Edit extends Fragment implements View.OnClickListener {

    TextView textview;
    EditText about, city, job, education;

    public View onCreateView(LayoutInflater i, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View root = i.inflate(R.layout.u_setting_edit_frag, container, false);


        textview = (TextView)getActivity().findViewById(R.id.topbar_text);
        textview.setText("Rediger Profil");

        about = root.findViewById(R.id.editText_description);
        city = root.findViewById(R.id.editText_city);
        job = root.findViewById(R.id.editText_job);
        education = root.findViewById(R.id.editText_edu);




        return root;


    }


    @Override
    public void onClick(View view) {

    }
}
