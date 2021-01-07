package com.A4.oplev.Like_Hjerte_Side;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.fragment.app.Fragment;

import com.A4.oplev.Activity_Ini;
import com.A4.oplev.Chat.Activity_Chat;

import Controller.Listeners.OnSwipeTouchListener;
import Controller.UserController;
import DAL.Classes.ChatDAO;
import DAL.Classes.EventDAO;
import DAL.Interfaces.CallbackEvent;
import DAL.Interfaces.CallbackUser;
import DTO.ChatDTO;
import DTO.EventDTO;
import DTO.UserDTO;

import com.A4.oplev.R;
import com.A4.oplev._Adapters.LikeSide_Adapter;
import com.A4.oplev._Adapters.LikeSide_Event_Adapter;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class LikesideList_frag extends Fragment{
    private ListView chat_listView, tilmeldinger_listView;
    private ChatDAO chatDAO;
    private EventDAO eventDAO;
    private UserDTO userDTO;
    private UserController userController;
    private String currentUser;
    private ArrayList<String> names = new ArrayList<>(), lastMessage = new ArrayList<>(), headerList = new ArrayList<>(), lastSender = new ArrayList<>(), isInitialized = new ArrayList<>(), chatIds = new ArrayList<>(), otherPersonPic = new ArrayList<>();
    private ArrayList<Date> dates = new ArrayList<>();
    private ArrayList<Integer> eventApplicantsSize = new ArrayList<>();
    private ArrayList<String> eventHeaders = new ArrayList<>(), eventEventPic = new ArrayList<>(), eventApplicantPic = new ArrayList<>(), eventOwnerPic = new ArrayList<>(), eventFirstApplicants = new ArrayList<>();
    private Context mContext;
    private static ArrayList<View> footerViews = new ArrayList<>();

    // Den her klasse bruges til at få lave chatlisten ude fra likesiden af (hvor man kan vælge den chat man vil ind i)
    @SuppressLint("ClickableViewAccessibility")
    public View onCreateView(LayoutInflater i, ViewGroup container, Bundle savedInstanceState) {
        View root = i.inflate(R.layout.likeside_frag,container,false);
        // Vi henter nogle informationer fra userControlleren så vi ved hvilken person vi er i gang med at sætte listen op for
        userController = UserController.getInstance();
        userDTO = userController.getCurrUser();
        chatDAO = new ChatDAO();
        eventDAO = new EventDAO();

        //chat_listView = root.findViewById(R.id.beskedListView_chats);
        tilmeldinger_listView = root.findViewById(R.id.beskedListView_tilmeldinger);

        //chatDAO.createChat(new ChatDTO(null,null,null,null,null,null,"Spasser","John dillermand","Aben"));

        // Tjek om personen har nogle chatId's ellers så gå videre
        if (userDTO == null) userDTO = userController.getCurrUser();
        if (userDTO != null) {
            if (userDTO.getChatId() != null) {
                // Vi looper over alle chatId's og opbygger dens view og indsætter den i listviewet
                for (int j = 0; j < userDTO.getChatId().size(); j++) {
                    chatDAO.readChat(dto -> {
                        String otherUserID = dto.getUser1ID().equals(userDTO.getUserId()) ? dto.getUser2ID() : dto.getUser1ID();
                        userController.getUser(user -> {
                            if (user != null){
                                otherPersonPic.add(user.getUserPicture());
                                if (userDTO.getChatId().size() == otherPersonPic.size()) {
                                    setListView_chats(chatIds, names, dates, lastMessage, headerList, lastSender, isInitialized, otherPersonPic);
                                    setChangeListeners_chats();
                                }
                            }
                        }, otherUserID);
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
                        if (dto.getUser1().equals(userDTO.getfName())) {
                            currentUser = dto.getUser1();
                            names.add(dto.getUser2());
                        } else {
                            currentUser = dto.getUser2();
                            names.add(dto.getUser1());
                        }
                        headerList.add(dto.getHeader());
                        chatIds.add(dto.getChatId());
                    }, userDTO.getChatId().get(j));
                }
            }
        }


        tilmeldinger_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println(position + "\t" + eventHeaders.size());
                if (position < eventHeaders.size()) {
                    sendNoti();
                    Toast.makeText(mContext, "Notifikation sendt", Toast.LENGTH_SHORT).show();
                    // Gør noget her som går ind på den andens profil
                    if (eventFirstApplicants.get(position).equals("")) {
                        System.out.println(eventHeaders.get(position));
                    } else {
                        userController.getUser(new CallbackUser() {
                            @Override
                            public void onCallback(UserDTO user) {

                            }
                        }, eventFirstApplicants.get(position));
                    }
                }
                else {
                    // Her ville chatId'et også sendes med senere hen
                    Intent i1 = new Intent(getActivity(), Activity_Chat.class);
                    i1.putExtra("currentUser",currentUser);
                    i1.putExtra("otherUser",names.get(position-eventHeaders.size()));
                    i1.putExtra("chatId", userDTO.getChatId().get(position-eventHeaders.size()));
                    startActivity(i1);
                }
            }
        });

        tilmeldinger_listView.setOnTouchListener(new OnSwipeTouchListener(mContext){
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


        /**
         * Det her er til ens egne events som andre prøver på at ansøge om at joine
         */
        if (userDTO == null) userDTO = userController.getCurrUser();
        if (userDTO != null){
            if (userDTO.getEvents() != null){
                for (int j = 0; j < userDTO.getEvents().size(); j++) {
                    eventDAO.getEvent(event -> {
                        if (event != null) {
                            eventApplicantsSize.add(event.getApplicants().size());
                            eventHeaders.add(event.getTitle());
                            eventOwnerPic.add(event.getOwnerPic());
                            eventEventPic.add(event.getEventPic());
                            String firstApplicant = event.getApplicants().size() == 0 ? "" : event.getApplicants().get(0);
                            eventFirstApplicants.add(firstApplicant);
                            if (firstApplicant.equals("")) {
                                eventApplicantPic.add("");
                                if (eventApplicantPic.size() == userDTO.getEvents().size()) {
                                    setListView_applicants(eventEventPic, eventHeaders, eventOwnerPic, eventFirstApplicants, eventApplicantPic, eventApplicantsSize);
                                }
                            } else {
                                userController.getUser(user -> {
                                    eventApplicantPic.add(user.getUserPicture());
                                    if (eventApplicantPic.size() == userDTO.getEvents().size()) {
                                        setListView_applicants(eventEventPic, eventHeaders, eventOwnerPic, eventFirstApplicants, eventApplicantPic, eventApplicantsSize);
                                    }
                                }, event.getApplicants().get(0));
                            }
                        }
                    }, userDTO.getEvents().get(j));
                }
            }
        }
        return root;
    }

    public void setListView_chats(ArrayList<String> chatIds, ArrayList<String> names, ArrayList<Date> dates, ArrayList<String> lastMessage, ArrayList<String> headerList, ArrayList<String> lastSender, ArrayList<String> isInitialized, ArrayList<String> otherPersonPic){
        ArrayList<String> tempNames = new ArrayList<>(), tempLastmessage = new ArrayList<>(), tempHeaderList = new ArrayList<>(), tempLastSender = new ArrayList<>(), tempIsInitialized = new ArrayList<>(), tempChatIds = new ArrayList<>(), tempOtherPersonPic = new ArrayList<>();
        ArrayList<Date> tempDates = new ArrayList<>();

        for (int i = 0; i < dates.size(); i++) {
            if (i == 0){
                tempChatIds.add(chatIds.get(i));
                tempNames.add(names.get(i));
                tempLastmessage.add(lastMessage.get(i));
                tempDates.add(dates.get(i));
                tempHeaderList.add(headerList.get(i));
                tempIsInitialized.add(isInitialized.get(i));
                tempLastSender.add(lastSender.get(i));
                tempOtherPersonPic.add(otherPersonPic.get(i));
            }
            else if (tempDates.get(i-1).after(dates.get(i))){
                tempChatIds.add(chatIds.get(i));
                tempNames.add(names.get(i));
                tempLastmessage.add(lastMessage.get(i));
                tempDates.add(dates.get(i));
                tempHeaderList.add(headerList.get(i));
                tempIsInitialized.add(isInitialized.get(i));
                tempLastSender.add(lastSender.get(i));
                tempOtherPersonPic.add(otherPersonPic.get(i));
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
                                tempOtherPersonPic.add(tempOtherPersonPic.get(i-1));
                            }
                            else if (k != 0 ){
                                tempChatIds.set(k, tempChatIds.get(Math.max(k - 1,0)));
                                tempNames.set(k, tempNames.get(Math.max(k - 1,0)));
                                tempLastmessage.set(k, tempLastmessage.get(Math.max(k - 1,0)));
                                tempDates.set(k, tempDates.get(Math.max(k - 1,0)));
                                tempHeaderList.set(k, tempHeaderList.get(Math.max(k - 1,0)));
                                tempIsInitialized.set(k, tempIsInitialized.get(Math.max(k - 1,0)));
                                tempLastSender.set(k, tempLastSender.get(Math.max(k - 1,0)));
                                tempOtherPersonPic.set(k,otherPersonPic.get(Math.max(k-1,0)));
                            }
                        }
                        tempChatIds.set(j,chatIds.get(i));
                        tempNames.set(j,names.get(i));
                        tempLastmessage.set(j,lastMessage.get(i));
                        tempDates.set(j,dates.get(i));
                        tempHeaderList.set(j,headerList.get(i));
                        tempIsInitialized.set(j,isInitialized.get(i));
                        tempLastSender.set(j,lastSender.get(i));
                        tempOtherPersonPic.set(j,otherPersonPic.get(i));
                        break;
                    }
                }
            }

        }
        userDTO.setChatId(tempChatIds);

        // Vi laver adapteren der laver vores listview over de chats man har
        if (mContext != null) {
            while(footerViews.size() != 0){
                tilmeldinger_listView.removeFooterView(footerViews.get(0));
                footerViews.remove(0);
            }
            System.out.println("FOOTER COUNT 1 " + tilmeldinger_listView.getFooterViewsCount());
            tilmeldinger_listView.setAdapter(null);
            System.out.println("FOOTER COUNT 2 " + tilmeldinger_listView.getFooterViewsCount());
            setListView_applicants(eventEventPic, eventHeaders, eventOwnerPic, eventFirstApplicants, eventApplicantPic, eventApplicantsSize);
            System.out.println("FOOTER COUNT 3 " + tilmeldinger_listView.getFooterViewsCount());
            LikeSide_Adapter adapter = new LikeSide_Adapter(mContext, tempNames, tempDates, tempLastmessage, tempHeaderList, tempLastSender, tempIsInitialized, otherPersonPic);
            System.out.println("FOOTER COUNT 4 " + tilmeldinger_listView.getFooterViewsCount());
            for (int i = 0; i < tempNames.size(); i++) {
                View v = adapter.getView(i,null,null);
                tilmeldinger_listView.addFooterView(v);
                footerViews.add(v);
            }
            System.out.println("FOOTER COUNT 5 " + tilmeldinger_listView.getFooterViewsCount());
        }
        this.dates = tempDates;
        this.names = tempNames;
        this.lastMessage = tempLastmessage;
        this.headerList = tempHeaderList;
        this.lastSender = tempLastSender;
        this.isInitialized = tempIsInitialized;
        this.chatIds = tempChatIds;
    }

    public void setChangeListeners_chats() {
        for (int j = 0; j < chatIds.size(); j++) {
            FirebaseFirestore.getInstance().collection("chats").document(chatIds.get(j)).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                private static final String TAG = "update from firestore";

                @Override
                public void onEvent(@Nullable DocumentSnapshot snapshot,
                                    @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e);
                        return;
                    }

                    if (snapshot != null && snapshot.exists()) {
                        ChatDTO temp = snapshot.toObject(ChatDTO.class);
                        if (temp != null) {
                            if (temp.getDates() != null) {
                                for (int k = 0; k < chatIds.size(); k++) {
                                    if (chatIds.get(k).equals(temp.getChatId())) {
                                        if (!temp.getDates().get(temp.getDates().size() - 1).equals(dates.get(k))) {
                                            dates.set(k, temp.getDates().get(temp.getDates().size() - 1));
                                            lastMessage.set(k, temp.getMessages().get(temp.getMessages().size() - 1));
                                            lastSender.set(k, temp.getSender().get(temp.getMessages().size() - 1));
                                            setListView_chats(chatIds, names, dates, lastMessage, headerList, lastSender, isInitialized, otherPersonPic);
                                        }
                                        break;
                                    }
                                }
                            }
                        }

                    } else {
                        Log.d(TAG, "Current data: null");
                    }
                }
            });
        }
    }


    public void setListView_applicants(@NonNull ArrayList<String> eventEventPic, @NonNull ArrayList<String> eventHeaders, @NonNull ArrayList<String> eventOwnerPic, @NonNull ArrayList<String> eventFirstApplicants, @NonNull ArrayList<String> eventApplicantPic, @NonNull ArrayList<Integer> eventApplicantsSize){
        if (mContext != null){
            LikeSide_Event_Adapter eventAdapter = new LikeSide_Event_Adapter(mContext, eventEventPic, eventHeaders, eventOwnerPic, eventFirstApplicants, eventApplicantPic, eventApplicantsSize);
            tilmeldinger_listView.setAdapter(eventAdapter);
        }
    }

    @Override
    public void onAttach(@NotNull Context context){
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onDetach(){
        //sendNoti();
        this.mContext = null;
        super.onDetach();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    // Skal ændres til noget baggrunds halløjsa senere hen
    private void sendNoti(){
        Intent i = new Intent(mContext, Activity_Likeside.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(mContext);
        stackBuilder.addNextIntentWithParentStack(i);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, "0")
                .setSmallIcon(R.drawable.chat)
                .setContentTitle("KOM TILBAGE")
                .setContentText("I miss you bro")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(mContext);
        builder.setContentIntent(resultPendingIntent);
        notificationManager.notify(0, builder.build());
    }
}
