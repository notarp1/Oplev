package com.A4.oplev.UserSettings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.A4.oplev.Activity_Ini;
import com.A4.oplev.R;
import com.google.firebase.auth.FirebaseAuth;

import Controller.UserController;

public class U_Settings_Options extends Fragment implements View.OnClickListener {

    TextView logud;
    SharedPreferences prefs;
    ImageView back;

    public View onCreateView(LayoutInflater i, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View root = i.inflate(R.layout.u_setting_opt_frag, container, false);

        TextView textview = (TextView)getActivity().findViewById(R.id.topbar_text);
        textview.setText("Indstillinger");
        logud = root.findViewById(R.id.logud_txt);
        logud.setOnClickListener(this);

        back = (ImageView) getActivity().findViewById(R.id.topbar_arrow);
        back.setOnClickListener(this);

        return root;


    }


    @Override
    public void onClick(View view) {

        if(view == logud){
            FirebaseAuth.getInstance().signOut();
            PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().clear().apply();
            UserController.getInstance().setCurrUser(null);
            FragmentManager fm = getActivity().getSupportFragmentManager();
            for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                fm.popBackStack();
            }
            Intent i = new Intent(getActivity(), Activity_Ini.class);
            startActivity(i);

        } else if (view == back){
            getActivity().finish();
        }
    }
}
