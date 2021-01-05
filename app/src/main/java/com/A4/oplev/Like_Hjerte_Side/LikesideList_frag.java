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
import Controller.UserController;
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
    private UserController userController;
    private String currentUser;
    private ArrayList<String> names = new ArrayList<>(), lastMessage = new ArrayList<>(), headerList = new ArrayList<>(), lastSender = new ArrayList<>(), isInitialized = new ArrayList<>(), chatIds = new ArrayList<>();
    private ArrayList<Date> dates = new ArrayList<>();

    // Den her klasse bruges til at få lave chatlisten ude fra likesiden af (hvor man kan vælge den chat man vil ind i)
    @SuppressLint("ClickableViewAccessibility")
    public View onCreateView(LayoutInflater i, ViewGroup container, Bundle savedInstanceState) {
        View root = i.inflate(R.layout.likeside_frag,container,false);
        // Vi henter nogle informationer fra userControlleren så vi ved hvilken person vi er i gang med at sætte listen op for
        userController = userController.getInstance();
        userDTO = userController.getCurrUser();
        chatDAO = new ChatDAO();

        listView = root.findViewById(R.id.beskedListView);

        //chatDAO.createChat(new ChatDTO(null,null,null,null,null,null,"Istur","John","Karl"));

        // Tjek om personen har nogle chatId's ellers så gå videre
        if (userDTO.getChatId() != null) {
            // Vi looper over alle chatId's og opbygger dens view og indsætter den i listviewet
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
                        chatIds.add(dto.getChatId());
                        if (userDTO.getChatId().size() == headerList.size()){
                            setListView(chatIds,names,dates,lastMessage,headerList,lastSender,isInitialized);
                        }
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
                getActivity().findViewById(R.id.besked_back).setVisibility(View.INVISIBLE);
                getActivity().findViewById(R.id.hjerte_back).setVisibility(View.VISIBLE);

                // Kreer fragmentet over til hjertesiden
                getFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right,
                        R.anim.exit_to_left).replace(R.id.likeside_frameLayout,new HjerteSide_frag())
                        .commit();
            }
        });


        return root;
    }

    public void setListView(ArrayList<String> chatIds, ArrayList<String> names, ArrayList<Date> dates, ArrayList<String> lastMessage, ArrayList<String> headerList, ArrayList<String> lastSender, ArrayList<String> isInitialized){
        ArrayList<String> tempNames = new ArrayList<>(), tempLastmessage = new ArrayList<>(), tempHeaderList = new ArrayList<>(), tempLastSender = new ArrayList<>(), tempIsInitialized = new ArrayList<>(), tempChatIds = new ArrayList<>();
        ArrayList<Date> tempDates = new ArrayList<>();

//        for (int i = 0; i < chatIds.size(); i++) {
//            for (int j = 0; j < chatIds.size(); j++) {
//                if (userDTO.getChatId().get(i).equals(chatIds.get(j))){
//                    tempNames.add(names.get(j));
//                    tempLastmessage.add(lastMessage.get(j));
//                    tempDates.add(dates.get(j));
//                    tempHeaderList.add(headerList.get(j));
//                    tempIsInitialized.add(isInitialized.get(j));
//                    tempLastSender.add(lastSender.get(j));
//                }
//            }
//        }

        for (int i = 0; i < dates.size(); i++) {
            if (i == 0){
                tempChatIds.add(chatIds.get(i));
                tempNames.add(names.get(i));
                tempLastmessage.add(lastMessage.get(i));
                tempDates.add(dates.get(i));
                tempHeaderList.add(headerList.get(i));
                tempIsInitialized.add(isInitialized.get(i));
                tempLastSender.add(lastSender.get(i));
            }
            else if (tempDates.get(i-1).after(dates.get(i))){
                tempChatIds.add(chatIds.get(i));
                tempNames.add(names.get(i));
                tempLastmessage.add(lastMessage.get(i));
                tempDates.add(dates.get(i));
                tempHeaderList.add(headerList.get(i));
                tempIsInitialized.add(isInitialized.get(i));
                tempLastSender.add(lastSender.get(i));
            }
            else {
                for (int j = 0; j < i; j++) {
                    if (dates.get(i).after(tempDates.get(j))){
                        for (int k = i; k >= j; k--) {
                            if (k == i){
                                tempChatIds.add(tempChatIds.get(i-1));
                                tempNames.add(tempNames.get(i-1));
                                tempLastmessage.add(tempLastmessage.get(i-1));
                                tempDates.add(tempDates.get(i-1));
                                tempHeaderList.add(tempHeaderList.get(i-1));
                                tempIsInitialized.add(tempIsInitialized.get(i-1));
                                tempLastSender.add(tempLastSender.get(i-1));
                            }
                            else if (k != 0 ){
                                tempChatIds.set(k, tempChatIds.get(Math.max(k - 1,0)));
                                tempNames.set(k, tempNames.get(Math.max(k - 1,0)));
                                tempLastmessage.set(k, tempLastmessage.get(Math.max(k - 1,0)));
                                tempDates.set(k, tempDates.get(Math.max(k - 1,0)));
                                tempHeaderList.set(k, tempHeaderList.get(Math.max(k - 1,0)));
                                tempIsInitialized.set(k, tempIsInitialized.get(Math.max(k - 1,0)));
                                tempLastSender.set(k, tempLastSender.get(Math.max(k - 1,0)));
                            }
                        }
                        tempChatIds.set(j,chatIds.get(i));
                        tempNames.set(j,names.get(i));
                        tempLastmessage.set(j,lastMessage.get(i));
                        tempDates.set(j,dates.get(i));
                        tempHeaderList.set(j,headerList.get(i));
                        tempIsInitialized.set(j,isInitialized.get(i));
                        tempLastSender.set(j,lastSender.get(i));
                    }
                    break;
                }
            }

        }
        userDTO.setChatId(tempChatIds);

        // Vi laver adapteren der laver vores listview over de chats man har
        LikeSide_Adapter adapter = new LikeSide_Adapter(getContext(), tempNames, tempDates, tempLastmessage,tempHeaderList, tempLastSender, tempIsInitialized);
        listView.setAdapter(adapter);
        this.dates = tempDates;
        this.names = tempNames;
        this.lastMessage = tempLastmessage;
        this.headerList = tempHeaderList;
        this.lastSender = lastSender;
        this.isInitialized = isInitialized;
    }
}
