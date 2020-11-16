package com.A4.oplev;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class U_Settings_Main extends Fragment implements View.OnClickListener {

    View visProfil, rediger, indstillinger;
    TextView about;
    Bundle bundle;

    public View onCreateView(LayoutInflater i, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View root = i.inflate(R.layout.u_setting_main_frag, container, false);

        bundle = this.getArguments();

        TextView textview = (TextView)getActivity().findViewById(R.id.topbar_text);
        textview.setText("Profil");

        visProfil = root.findViewById(R.id.box_profil);
        rediger = root.findViewById(R.id.box_rediger);
        indstillinger = root.findViewById(R.id.box_indstillinger);
        about = root.findViewById(R.id.u_profile_name);

        String aboutText = bundle.getString("fName") + ", "+  bundle.getString("age");

        about.setText(aboutText);

        indstillinger.setOnClickListener(this);
        rediger.setOnClickListener(this);
        visProfil.setOnClickListener(this);


        return root;


    }


    @Override
    public void onClick(View v) {

         if (v == indstillinger){
            getFragmentManager().beginTransaction()
                .replace(R.id.mainFragmentBox, new U_settings_opt())
                .addToBackStack(null)
                .commit();

        } else if (v == rediger){
             getFragmentManager().beginTransaction()
                     .replace(R.id.mainFragmentBox, new U_Settings_Edit())
                     .addToBackStack(null)
                     .commit();
         } else if (v == visProfil){
             Intent i = new Intent(getActivity(), Activity_profile.class);
             i.putExtra("fName",bundle.getString("fName"));
             i.putExtra("lName",bundle.getString("lName"));
             i.putExtra("age",bundle.getString("age"));
             i.putExtra("desc",bundle.getString("desc"));
             i.putExtra("city",bundle.getString("city"));
             startActivity(i);
         }
    }
}
