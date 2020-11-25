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

import Controller.Controller;
import Controller.Listeners.OnSwipeTouchListener;
import DAL.Classes.ChatDAO;
import DTO.ChatDTO;
import DTO.UserDTO;

import com.A4.oplev.R;
import com.A4.oplev._Adapters.LikeSide_Adapter;

import java.util.ArrayList;
import java.util.Date;


public class LikesideList_frag extends Fragment{
    private ListView listView;
    private ChatDAO chatDAO;
    private UserDTO userDTO;
    private Controller controller;
    private String currentUser;

    // Den her klasse bruges til at få lave chatlisten ude fra likesiden af (hvor man kan vælge den chat man vil ind i)
    @SuppressLint("ClickableViewAccessibility")
    public View onCreateView(LayoutInflater i, ViewGroup container, Bundle savedInstanceState) {
        View root = i.inflate(R.layout.likeside_frag,container,false);
        controller = Controller.getInstance();
        userDTO = controller.getCurrUser();
        chatDAO = new ChatDAO();

        listView = root.findViewById(R.id.beskedListView);

        // Lige nu bliver de her auto-genereret men skal senere hen hentes ind fra firestore af
        ArrayList<String> names = new ArrayList<>(), lastMessage = new ArrayList<>(), headerList = new ArrayList<>(), lastSender = new ArrayList<>(), isInitialized = new ArrayList<>();
        ArrayList<Date> dates = new ArrayList<>();


        if (userDTO.getChatId() != null) {

            for (int j = 0; j < userDTO.getChatId().size(); j++) {
                chatDAO.readChat(new ChatDAO.FirestoreCallback() {
                    @Override
                    public void onCallback(ChatDTO dto) {
                        if (dto.getSender() != null) {
                            dates.add(dto.getDates().get(dto.getDates().size() - 1));
                            lastMessage.add(dto.getMessages().get(dto.getMessages().size() - 1));
                            lastSender.add(dto.getSender().get(dto.getSender().size() - 1));
                            isInitialized.add("true");
                        } else {
                            isInitialized.add("false");
                            dates.add(new Date());
                            lastMessage.add("blabla");
                            lastSender.add("blabla");
                        }
                        if (dto.getUser1().equals(userDTO.getfName())){
                            currentUser = dto.getUser1();
                            names.add(dto.getUser2());
                        } else{
                            currentUser = dto.getUser2();
                            names.add(dto.getUser1());
                        }
                        headerList.add(dto.getHeader());
                        // Vi laver adapteren der laver vores listview over de chats man har
                        LikeSide_Adapter adapter = new LikeSide_Adapter(getContext(), names, dates, lastMessage,headerList, lastSender, isInitialized);
                        listView.setAdapter(adapter);
                    }
                }, userDTO.getChatId().get(j));
            }
        }


        // Vi laver en itemclicklistener for at kunne differentiere med hvilket listview objekt man har trykket på
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Her ville chatId'et også sendes med senere hen
                Intent i = new Intent(getActivity(), Activity_Chat.class);
                i.putExtra("currentUser",currentUser);
                i.putExtra("otherUser",names.get(position));
                i.putExtra("chatId", userDTO.getChatId().get(position));
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
