package com.A4.oplev.UserSettings;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

    TextView logud, deleteAccount;
    SharedPreferences prefs;
    ImageView back;
    UserController userController;

    public View onCreateView(LayoutInflater i, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View root = i.inflate(R.layout.u_setting_opt_frag, container, false);

        userController = UserController.getInstance();

        TextView textview = (TextView)getActivity().findViewById(R.id.topbar_text);
        textview.setText("Indstillinger");
        logud = root.findViewById(R.id.logud_txt);
        deleteAccount = root.findViewById(R.id.deleteAcc_txt);
        back = (ImageView) getActivity().findViewById(R.id.topbar_arrow);

        deleteAccount.setOnClickListener(this);
        logud.setOnClickListener(this);
        back.setOnClickListener(this);


        return root;


    }


    @Override
    public void onClick(View view) {

        if(view == logud){
            FirebaseAuth.getInstance().signOut();
            PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().clear().apply();
            FragmentManager fm = getActivity().getSupportFragmentManager();
            for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                fm.popBackStack();
            }
            Intent i = new Intent(getActivity(), Activity_Ini.class);
            startActivity(i);

        } else if (view == back){
            getActivity().finish();
        } else if (view == deleteAccount){

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

            builder.setTitle("Slet konto");
            builder.setMessage("Er du sikker på du vil slette din konto? Alle indstillinger, events samt din brugerprofil vil blive slettet. ");

            builder.setPositiveButton("JA, SLET KONTO.", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    // Do nothing but close the dialog
                    userController.deleteUser(userController.getCurrUser());
                    dialog.dismiss();
                }
            });

            builder.setNegativeButton("NEJ, BEHOLD KONTO.", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {


                    dialog.dismiss();
                }
            });

            AlertDialog alert = builder.create();
            alert.show();



        }
    }
}
