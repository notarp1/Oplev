package com.example.oplev;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class Search_filter_bottom_frag extends Fragment implements View.OnClickListener {

    Search_filter_bottom_frag(){
    }

    public View onCreateView(LayoutInflater i, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View root = i.inflate(R.layout.search_filter_frag, container, false);

        return root;
    }

        @Override
    public void onClick(View v) {

    }
}
