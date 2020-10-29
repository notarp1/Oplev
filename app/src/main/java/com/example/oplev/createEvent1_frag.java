package com.example.oplev;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class createEvent1_frag extends Fragment implements View.OnClickListener {
    //topbar text
    TextView topbar_txt;
    //fragment elements
    ImageView pic;
    EditText title_in, desc_in, price_in, date_in, city_in;
    TextView price_txt, date_txt, city_txt;
    Button next_btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_create_event1_frag, container, false);
        //fix top bar text:
        //topbar_txt = root.findViewById(R.id.topbar_text);
        //topbar_txt.setText("Lav nyt opslag");

        //get elements
        pic = root.findViewById(R.id.create_pic);
        title_in = root.findViewById(R.id.create_title_input);
        desc_in = root.findViewById(R.id.create_desc_input);
        price_in = root.findViewById(R.id.create_price_input);
        date_in = root.findViewById(R.id.create_date_input);
        city_in = root.findViewById(R.id.create__city_input);
        next_btn = root.findViewById(R.id.create_next_btn);

        //set onclick listeners
        pic.setOnClickListener(this);
        next_btn.setOnClickListener(this);


        return root;
    }

    @Override
    public void onClick(View v) {
        if(v == pic){
            //gør noget smart for at kunne udskifte billedet
        }
        else if(v == next_btn){
            Bundle b = new Bundle();
            //set strings
            b.putString("title_in", title_in.getText().toString());
            b.putString("desc_in", desc_in.getText().toString());
            b.putString("date_in", date_in.getText().toString());
            b.putString("city_in", city_in.getText().toString());
            //set int (price)
            b.putInt("price_in", Integer.parseInt(price_in.getText().toString()));

            //create fragment and add bundle to arguments
            Fragment create2_frag = new createEvent2_frag();
            create2_frag.setArguments(b);

            //transaction to next
            getFragmentManager().beginTransaction()
                    .replace(R.id.mainFragmentBox, create2_frag, "deleteme")
                    .addToBackStack("deleteme")
                    .commit();
        }
    }
}