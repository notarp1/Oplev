package com.A4.oplev.Like_Hjerte_Side;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import Controller.Listeners.OnSwipeTouchListener;
import com.A4.oplev.R;

public class HjerteSide_frag extends Fragment implements View.OnClickListener{
    ListView listView;

    @SuppressLint("ClickableViewAccessibility")
    public View onCreateView(LayoutInflater i, ViewGroup container, Bundle savedInstanceState) {
        View root = i.inflate(R.layout.hjerteside_frag,container,false);

        // Auto genereret information som senere hen skal hentes ind fra firestore af
        String[] navne = {"John", "abc", "Bente", "AGE", "Yes", "whoDis?", "yubrakit yubotit"};
        String[] dato = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};

        // opsætter adapteren af det listview der skal blive vist på skærmen
        ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.hjerteside_liste_element, R.id.hjertside_overskrift, navne);

        listView = root.findViewById(R.id.hjerteside_listview);
        //listView.setOnItemClickListener(this);
        listView.setAdapter(adapter);

        // Sætter en swipelistener op for at kunne swipe til siden og kunne gå tilbage til likesiden (med chatsne)
        listView.setOnTouchListener(new OnSwipeTouchListener(getContext()){
            @Override
            public void onSwipeRight() {
                getActivity().findViewById(R.id.besked_back).setVisibility(View.VISIBLE);
                getActivity().findViewById(R.id.hjerte_back).setVisibility(View.INVISIBLE);

                getFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.slide_in_left,
                        android.R.anim.slide_out_right).replace(R.id.likeside_frameLayout,new LikesideList_frag())
                        .commit();
            }
        });

        return root;
    }

        @Override
    public void onClick(View v) {

    }
}
