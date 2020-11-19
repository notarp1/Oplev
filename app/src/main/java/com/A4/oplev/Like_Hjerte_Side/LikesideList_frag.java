package com.A4.oplev.Like_Hjerte_Side;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.A4.oplev.Chat.Activity_Chat;
import Controller.Listeners.OnSwipeTouchListener;
import DAL.Classes.ChatDAO;
import DTO.ChatDTO;

import com.A4.oplev.R;
import com.A4.oplev._Adapters.LikeSide_Adapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LikesideList_frag extends Fragment{
    ListView listView;
    ChatDTO dto;

    @SuppressLint("ClickableViewAccessibility")
    public View onCreateView(LayoutInflater i, ViewGroup container, Bundle savedInstanceState) {
        View root = i.inflate(R.layout.likeside_frag,container,false);

        ArrayList<String> names = new ArrayList<>(), dates = new ArrayList<>(), lastMessage = new ArrayList<>(), headerList = new ArrayList<>();
        final String[] navneArray = {"John", "abc", "Bente", "AGE", "Yes", "whoDis?", "yubrakit yubotit"};
        final String[] datoArray = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        final String[] lastMessagesArray = {"Ja", "Okay", "321", "Whoops", "Hej", "blabla", "Davs"};
        final String[] headerListArray = {"Kanotur", "Spise is", "Tivoli", "Bjergbestigning", "Kakkerlakspisning", "Sovsekonkurrence", "Hospitalet"};


        for (int j = 0; j < navneArray.length; j++) {
            names.add(navneArray[j]);
            dates.add(datoArray[j]);
            headerList.add(headerListArray[j]);
            lastMessage.add(lastMessagesArray[j]);
        }





        ChatDAO dao = new ChatDAO();
        dao.readChat(new ChatDAO.FirestoreCallback() {
            @Override
            public void onCallback(ChatDTO dto) {
                setChatDTO(dto);
            }
        },"60V6EddGhhZdY7pTGYRF");


        LikeSide_Adapter adapter = new LikeSide_Adapter(getContext(), names, dates, lastMessage,headerList);

        listView = root.findViewById(R.id.beskedListView);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getActivity(), Activity_Chat.class);
                i.putExtra("navn",navneArray[position]);
                i.putExtra("dato",datoArray[position]);
                startActivity(i);
            }
        });

        listView.setOnTouchListener(new OnSwipeTouchListener(getContext()){
            @Override
            public void onSwipeLeft() {
                getFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right,
                        R.anim.exit_to_left).replace(R.id.likeside_frameLayout,new HjerteSide_frag())
                        .commit();
            }
        });


        return root;
    }

    private void setChatDTO(ChatDTO dto){
        this.dto = dto;
    }

}
