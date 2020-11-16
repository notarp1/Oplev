package com.A4.oplev;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import DTO.UserDTO;

public class U_settings_profile extends Fragment implements View.OnClickListener {
    static Button btn_insta;
    public View onCreateView(LayoutInflater i, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //btn_insta.setOnClickListener(this);



        return  i.inflate(R.layout.u_settings_profile, container, false);


    }

    @Override
    public void onClick(View view) {
        if(view == btn_insta){

        }
    }
}
