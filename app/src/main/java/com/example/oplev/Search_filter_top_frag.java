package com.example.oplev;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class Search_filter_top_frag extends Fragment implements View.OnClickListener {
    ImageView back;
    static TextView title;

    public Search_filter_top_frag() {
    }

    public View onCreateView(LayoutInflater i, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View root = i.inflate(R.layout.u_settings_frag, container, false);

        title = root.findViewById(R.id.topbar_text);
        back = root.findViewById(R.id.topbar_arrow);

        back.setOnClickListener(this);

        getFragmentManager().beginTransaction().replace(R.id.mainFragmentBox, new Search_filter_bottom_frag(), "uSettingMainBox")
                .commit();

        return root;

    }


    @Override
    public void onClick(View v) {
        if (v == back) {
            getFragmentManager().popBackStack();

        }
    }
}
