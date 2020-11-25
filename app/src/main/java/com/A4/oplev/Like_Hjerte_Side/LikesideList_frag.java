package com.A4.oplev.Like_Hjerte_Side;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.A4.oplev.Chat.Activity_Chat;
import Controller.Listeners.OnSwipeTouchListener;
import DAL.Classes.ChatDAO;
import DTO.ChatDTO;

import com.A4.oplev.R;
import com.A4.oplev._Adapters.LikeSide_Adapter;

import java.util.ArrayList;


public class LikesideList_frag extends Fragment{
    ListView listView;
    ChatDTO dto;

    // Den her klasse bruges til at få lave chatlisten ude fra likesiden af (hvor man kan vælge den chat man vil ind i)
    @SuppressLint("ClickableViewAccessibility")
    public View onCreateView(LayoutInflater i, ViewGroup container, Bundle savedInstanceState) {
        View root = i.inflate(R.layout.likeside_frag,container,false);

        // Lige nu bliver de her auto-genereret men skal senere hen hentes ind fra firestore af
        ArrayList<String> names = new ArrayList<>(), dates = new ArrayList<>(), lastMessage = new ArrayList<>(), headerList = new ArrayList<>(), lastSender = new ArrayList<>();
        final String[] navneArray = {"John", "abc", "Bente", "AGE", "Yes", "whoDis?", "yubrakit yubotit"};
        final String[] datoArray = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        final String[] lastMessagesArray = {"Ja", "Okay", "321", "Whoops", "Hej", "blabla", "Davs"};
        final String[] headerListArray = {"Kanotur", "Spise is", "Tivoli", "Bjergbestigning", "Kakkerlakspisning", "Sovsekonkurrence", "Hospitalet"};
        final String[] lastSenders = {"John", "123", "Bente", "123", "Yes", "whoDis?", "123"};


        for (int j = 0; j < navneArray.length; j++) {
            names.add(navneArray[j]);
            dates.add(datoArray[j]);
            headerList.add(headerListArray[j]);
            lastMessage.add(lastMessagesArray[j]);
            lastSender.add(lastSenders[j]);
        }

        // Vi laver adapteren der laver vores listview over de chats man har
        LikeSide_Adapter adapter = new LikeSide_Adapter(getContext(), names, dates, lastMessage,headerList, lastSender);

        listView = root.findViewById(R.id.beskedListView);
        listView.setAdapter(adapter);

        // Vi laver en itemclicklistener for at kunne differentiere med hvilket listview objekt man har trykket på
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Her ville chatId'et også sendes med senere hen
                Intent i = new Intent(getActivity(), Activity_Chat.class);
                i.putExtra("navn",navneArray[position]);
                i.putExtra("dato",datoArray[position]);
                startActivity(i);
            }
        });

        // Vi har lavet en swipe listener for egentlig bare at kunne swipe til siden for at komme til hjertesiden
        listView.setOnTouchListener(new OnSwipeTouchListener(getContext()){
            @SuppressLint("ResourceAsColor")
            @Override
            public void onSwipeLeft() {
                // Sæt farven på billederne i toppen af skærmen
                getActivity().findViewById(R.id.likeside_hjertbillede).setBackgroundColor(getContext().getResources().getColor(R.color.likesideBilleder));
                getActivity().findViewById(R.id.likeside_beskedbillede).setBackgroundColor(getContext().getResources().getColor(R.color.chatColorGrey));

                // Kreer fragmentet over til hjertesiden
                getFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right,
                        R.anim.exit_to_left).replace(R.id.likeside_frameLayout,new HjerteSide_frag())
                        .commit();
            }
        });


        return root;
    }
}
