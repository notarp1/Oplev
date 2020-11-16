package com.A4.oplev.UserSettings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.A4.oplev.R;

public class U_settings_opt extends Fragment implements View.OnClickListener {


    public View onCreateView(LayoutInflater i, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View root = i.inflate(R.layout.u_setting_opt_frag, container, false);

        TextView textview = (TextView)getActivity().findViewById(R.id.topbar_text);
        textview.setText("Indstillinger");



        return root;


    }


    @Override
    public void onClick(View view) {

    }
}
