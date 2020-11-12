package com.A4.oplev;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class createEvent3_frag extends Fragment implements View.OnClickListener {
    TextView congrat_txt, askfriend_txt, shareby_txt;
    Button done_btn;
    ImageView fb_img, fbmsg_img, sms_img, email_img;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.create_event3_frag, container, false);
        done_btn = root.findViewById(R.id.create3_skip_btn);

        fb_img = root.findViewById(R.id.create3_fb_img);
        fbmsg_img = root.findViewById(R.id.create3_fbmsg_img);
        sms_img = root.findViewById(R.id.create3_sms_img);
        email_img = root.findViewById(R.id.create3_email_img);

        fb_img.setOnClickListener(this);
        done_btn.setOnClickListener(this);

        return root;
    }

    @Override
    public void onClick(View v) {
        if(v == done_btn){
            // go back to main fragment
            getActivity().finish();
        }
        else if(v == fb_img){
            //start sharing by facebook
        }
        else if(v==fbmsg_img){
            //start sharing by messenger
        }
        else if(v == sms_img){
            //start sharing by sms
        }
        else if(v == email_img){
            //start sharing on email
        }
    }
}