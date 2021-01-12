package com.A4.oplev.Like_Hjerte_Side;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.fragment.app.Fragment;


import com.A4.oplev.Activity_Profile;
import com.A4.oplev.Chat.Activity_Chat;

import Controller.Listeners.OnSwipeTouchListener;
import Controller.UserController;
import DAL.Classes.ChatDAO;
import DAL.Classes.EventDAO;

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


public class LikesideList_frag extends Fragment{
    private ListView tilmeldinger_listView;
    private ChatDAO chatDAO;
    private EventDAO eventDAO;
    private UserDTO userDTO;
    private UserController userController;
    private String currentUser;
    private ArrayList<String> names = new ArrayList<>(), lastMessage = new ArrayList<>(), headerList = new ArrayList<>(), lastSender = new ArrayList<>(), isInitialized = new ArrayList<>(), chatIds = new ArrayList<>(), otherPersonPic = new ArrayList<>();
    private ArrayList<Date> dates = new ArrayList<>();
    private ArrayList<Integer> eventApplicantsSize = new ArrayList<>();
    private ArrayList<String> eventHeaders = new ArrayList<>(), eventEventPic = new ArrayList<>(), eventApplicantPic = new ArrayList<>(), eventOwnerPic = new ArrayList<>(), eventFirstApplicants = new ArrayList<>(), eventParticipant = new ArrayList<>(), eventEventID = new ArrayList<>(), tempEventID = new ArrayList<>(), tempFirstApplicant = new ArrayList<>();
    private Context mContext;
    private static ArrayList<View> footerViews = new ArrayList<>();
    private int listviewPosition = 0;
    boolean chatsReady = false, eventsReady = false;

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

        // Denne linje er til test
        //chatDAO.createChat(new ChatDTO(null,null,null,null,null,null,"Spasser","John dillermand","Aben"));

        // Hvis vi af en eller anden grund ikke har en user så prøv på at få den igen
        if (userDTO == null) userDTO = userController.getCurrUser();
        if (userDTO != null) {
            if (userDTO.getChatId() != null) {
                // Hvis man ikke har nogle chats så skal vi sikre os at det ikke går i stå
                if (userDTO.getChatId().size() == 0) {
                    chatsReady = true;
                    if (eventsReady) {
                        setListView_chats(chatIds, names, dates, lastMessage, headerList, lastSender, isInitialized, otherPersonPic);
                        setListView_applicants(eventEventPic, eventHeaders, eventOwnerPic, eventFirstApplicants, eventApplicantPic, eventApplicantsSize);
                        setChangeListener_tilmeldinger();
                        setChangeListeners_chats();
                        setUserChangeListener();
                        eventsReady = false;
                        chatsReady = false;
                    }
                }
                // Vi looper over alle chatId's og tilføjer værdier til vores arrays
                for (int j = 0; j < userDTO.getChatId().size(); j++) {
                    chatDAO.readChat(dto -> {
                        // ternary operator til at få den anden brugers userid som vi skal hente fra databasen
                        String otherUserID = dto.getUser1ID().equals(userDTO.getUserId()) ? dto.getUser2ID() : dto.getUser1ID();
                        userController.getUser(user -> {
                            if (user != null){
                                // Vi tilføjer billedet af den anden person
                                otherPersonPic.add(user.getUserPicture());
                                // Hvis dette er den sidste chat som vi har loadet ind så skal vi enten vente på eventsne eller også så laver vi listviewet
                                if (userDTO.getChatId().size() == otherPersonPic.size()) {
                                    chatsReady = true;
                                    // hvis eventsne er klar så køres dette
                                    if (eventsReady) {
                                        // vi opsætter listviewet samt changelistenerne for chats og events
                                        setListView_chats(chatIds, names, dates, lastMessage, headerList, lastSender, isInitialized, otherPersonPic);
                                        setChangeListeners_chats();
                                        setChangeListener_tilmeldinger();
                                        setUserChangeListener();
                                        chatsReady = false;
                                        eventsReady = false;
                                    }
                                }
                            }
                        }, otherUserID);
                        // dette er nok redundant nu da vi sender en auto genereret besked, men hvis chatten ikke er instantieret så får den nogle default værdier
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

        // vi laver en clicklistener
        tilmeldinger_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Hvis den position vi clicker på er et af brugerens events
                if (position < tempEventID.size()) {
                    // Vi henter eventet ind fra databasen
                    eventDAO.getEvent(event -> {
                        // dernæst henter vi den første bruger ind fra databasen
                        userController.getUser(user -> {
                            // vi sender informationer videre til activity_profile som står for accepter/afslå bruger
                            Intent i12 = new Intent(mContext, Activity_Profile.class);
                            i12.putExtra("user", user);
                            i12.putExtra("load", 2);
                            i12.putExtra("numberOfApplicants", event.getApplicants().size()-1);
                            i12.putExtra("applicantList", event.getApplicants());
                            i12.putExtra("header", event.getTitle());
                            i12.putExtra("eventID", event.getEventId());
                            mContext.startActivity(i12);
                        }, tempFirstApplicant.get(position));
                    }, tempEventID.get(position));
                }
                else {
                    // hvis den position man clicker på er en chat så sender vi disse værdier
                    Intent i1 = new Intent(getActivity(), Activity_Chat.class);
                    i1.putExtra("currentUser",currentUser);
                    i1.putExtra("otherUser",names.get(position-tempEventID.size()));
                    i1.putExtra("chatId", userDTO.getChatId().get(position-tempEventID.size()));
                    startActivity(i1);
                }
            }
        });

        // Vi laver en swipe listener (måske skal ændres, måske ikke)
        tilmeldinger_listView.setOnTouchListener(new OnSwipeTouchListener(mContext){
            @SuppressLint("ResourceAsColor")
            @Override
            public void onSwipeLeft() {
                // Sæt farven på billederne i toppen af skærmen
                getActivity().findViewById(R.id.besked_back).setVisibility(View.INVISIBLE);
                getActivity().findViewById(R.id.event_back).setVisibility(View.INVISIBLE);
                getActivity().findViewById(R.id.hjerte_back).setVisibility(View.VISIBLE);

                // Kreer fragmentet over til hjertesiden
                getFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right,
                        R.anim.exit_to_left).replace(R.id.likeside_frameLayout,new HjerteSide_frag())
                        .commit();
            }

            @Override
            public void onSwipeRight() {
                // Sæt farven på billederne i toppen af skærmen
                getActivity().findViewById(R.id.besked_back).setVisibility(View.INVISIBLE);
                getActivity().findViewById(R.id.hjerte_back).setVisibility(View.INVISIBLE);
                getActivity().findViewById(R.id.event_back).setVisibility(View.VISIBLE);
            }
        });


        // Det her er til ens egne events som andre prøver på at ansøge om at joine
        // vi prøver på at hente brugeren ind hvis den af en eller anden grund ikke er
        if (userDTO == null) userDTO = userController.getCurrUser();
        // vi bruger den her til ikke at instantiere listviewet hvis der bliver hentet noget fra databasen
        final boolean[] isGettingFromDB = {false};
        if (userDTO != null){
            if (userDTO.getEvents() != null){
                // hvis brugeren ikke har nogle events så skal vi sikre os at det ikke går i stå
                if (userDTO.getEvents().size() == 0) {
                    eventsReady = true;
                    if (chatsReady) {
                        setListView_chats(chatIds, names, dates, lastMessage, headerList, lastSender, isInitialized, otherPersonPic);
                        setListView_applicants(eventEventPic, eventHeaders, eventOwnerPic, eventFirstApplicants, eventApplicantPic, eventApplicantsSize);
                        setChangeListener_tilmeldinger();
                        setChangeListeners_chats();
                        setUserChangeListener();
                        eventsReady = false;
                        chatsReady = false;
                    }
                }

                // vi itererer over alle events
                for (int j = 0; j < userDTO.getEvents().size(); j++) {
                    eventDAO.getEvent(event -> {
                        if (event != null) {
                            // ternary operator for at få den første applicant
                            String firstApplicant = event.getApplicants().size() == 0 ? "" : event.getApplicants().get(0);
                            // Hvis der ikke er nogle applicants så indsætter vi disse værdier
                            if (firstApplicant.equals("")) {
                                eventApplicantPic.add("");
                                eventApplicantsSize.add(event.getApplicants().size());
                                eventHeaders.add(event.getTitle());
                                eventOwnerPic.add(event.getOwnerPic());
                                eventEventPic.add(event.getEventPic());
                                eventFirstApplicants.add(firstApplicant);
                                eventEventID.add(event.getEventId());
                                eventParticipant.add(event.getParticipant());
                                // hvis det er den sidste event OG vi ikke henter noget fra databasen så kan vi køre den her kode
                                if (eventApplicantPic.size() == userDTO.getEvents().size() && !isGettingFromDB[0]) {
                                    // events er loadet færdigt
                                    eventsReady = true;
                                    // hvis chats er loadet færdigt så opsæt listviewet og listeners
                                    if (chatsReady) {
                                        setListView_chats(chatIds, names, dates, lastMessage, headerList, lastSender, isInitialized, otherPersonPic);
                                        setListView_applicants(eventEventPic, eventHeaders, eventOwnerPic, eventFirstApplicants, eventApplicantPic, eventApplicantsSize);
                                        setChangeListener_tilmeldinger();
                                        setChangeListeners_chats();
                                        setUserChangeListener();
                                        eventsReady = false;
                                        chatsReady = false;
                                    }
                                }
                            } else {
                                // hvis der er mindst 1 applicant så skal vi hente noget ind fra databasen
                                isGettingFromDB[0] = true;
                                // vi henter applicanten og indsætter personens værdier
                                userController.getUser(user -> {
                                    eventApplicantPic.add(user.getUserPicture());
                                    eventApplicantsSize.add(event.getApplicants().size());
                                    eventHeaders.add(event.getTitle());
                                    eventOwnerPic.add(event.getOwnerPic());
                                    eventEventPic.add(event.getEventPic());
                                    eventFirstApplicants.add(firstApplicant);
                                    eventEventID.add(event.getEventId());
                                    eventParticipant.add(event.getParticipant());
                                    // nu har vi hentet færdigt
                                    isGettingFromDB[0] = false;
                                    // hvis det var sidste event så kør dette
                                    if (eventApplicantPic.size() == userDTO.getEvents().size()) {
                                        eventsReady = true;
                                        // hvis chats er færdige så opsæt listview og listeners
                                        if (chatsReady) {
                                            setListView_chats(chatIds, names, dates, lastMessage, headerList, lastSender, isInitialized, otherPersonPic);
                                            setListView_applicants(eventEventPic, eventHeaders, eventOwnerPic, eventFirstApplicants, eventApplicantPic, eventApplicantsSize);
                                            setChangeListener_tilmeldinger();
                                            setChangeListeners_chats();
                                            setUserChangeListener();
                                            eventsReady = false;
                                            chatsReady = false;
                                        }
                                    }
                                }, event.getApplicants().get(0));
                            }
                        }
                    }, userDTO.getEvents().get(j));
                }
            }
        }

        // vi overskriver scrolllisteneren til at give os listviewets position til hver en tid, som vi bruger når vi laver listviewet igen
        tilmeldinger_listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                listviewPosition = firstVisibleItem;
            }
        });

        return root;
    }

    // får at lave viewsne til chats
    public void setListView_chats(ArrayList<String> chatIds, ArrayList<String> names, ArrayList<Date> dates, ArrayList<String> lastMessage, ArrayList<String> headerList, ArrayList<String> lastSender, ArrayList<String> isInitialized, ArrayList<String> otherPersonPic){
        ArrayList<String> tempNames = new ArrayList<>(), tempLastmessage = new ArrayList<>(), tempHeaderList = new ArrayList<>(), tempLastSender = new ArrayList<>(), tempIsInitialized = new ArrayList<>(), tempChatIds = new ArrayList<>(), tempOtherPersonPic = new ArrayList<>();
        ArrayList<Date> tempDates = new ArrayList<>();

        if (chatIds.size() >= userDTO.getChatId().size()) {
            // iterer over hele listen
            for (int i = 0; i < otherPersonPic.size(); i++) {
                // hvis det er det første element så tilføjer vi det bare
                if (i == 0) {
                    tempChatIds.add(chatIds.get(i));
                    tempNames.add(names.get(i));
                    tempLastmessage.add(lastMessage.get(i));
                    tempDates.add(dates.get(i));
                    tempHeaderList.add(headerList.get(i));
                    tempIsInitialized.add(isInitialized.get(i));
                    tempLastSender.add(lastSender.get(i));
                    tempOtherPersonPic.add(otherPersonPic.get(i));
                }
                // hvis den dato man vil ligge ind kommer før den forrige så indsætter vi det bare
                else if (tempDates.get(i - 1).after(dates.get(i))) {
                    tempChatIds.add(chatIds.get(i));
                    tempNames.add(names.get(i));
                    tempLastmessage.add(lastMessage.get(i));
                    tempDates.add(dates.get(i));
                    tempHeaderList.add(headerList.get(i));
                    tempIsInitialized.add(isInitialized.get(i));
                    tempLastSender.add(lastSender.get(i));
                    tempOtherPersonPic.add(otherPersonPic.get(i));
                }
                // hvis den man vil indsætte har en dato der kommer før nogle andre datoer
                else {
                    // iterer over alle temp dates
                    for (int j = 0; j < i; j++) {
                        // hvis den man vil indsætte kommer efter den dato på plads j
                        if (dates.get(i).after(tempDates.get(j))) {
                            // iterer fra i'endes plads til j
                            for (int k = i; k >= j; k--) {
                                // ved den første tilføjer vi fra den forriges plads (flyt den sidste dato til en ny plads)
                                if (k == i) {
                                    tempChatIds.add(tempChatIds.get(i - 1));
                                    tempNames.add(tempNames.get(i - 1));
                                    tempLastmessage.add(tempLastmessage.get(i - 1));
                                    tempDates.add(tempDates.get(i - 1));
                                    tempHeaderList.add(tempHeaderList.get(i - 1));
                                    tempIsInitialized.add(tempIsInitialized.get(i - 1));
                                    tempLastSender.add(tempLastSender.get(i - 1));
                                    tempOtherPersonPic.add(tempOtherPersonPic.get(i - 1));
                                }
                                // fra alle pladser derefter så rykker vi pladsen 1 til højre
                                else if (k != 0) {
                                    tempChatIds.set(k, tempChatIds.get(Math.max(k - 1, 0)));
                                    tempNames.set(k, tempNames.get(Math.max(k - 1, 0)));
                                    tempLastmessage.set(k, tempLastmessage.get(Math.max(k - 1, 0)));
                                    tempDates.set(k, tempDates.get(Math.max(k - 1, 0)));
                                    tempHeaderList.set(k, tempHeaderList.get(Math.max(k - 1, 0)));
                                    tempIsInitialized.set(k, tempIsInitialized.get(Math.max(k - 1, 0)));
                                    tempLastSender.set(k, tempLastSender.get(Math.max(k - 1, 0)));
                                    tempOtherPersonPic.set(k, otherPersonPic.get(Math.max(k - 1, 0)));
                                }
                            }
                            // når vi har rykket alle gamle datoer så indsætter vi den nye dato vi vil indsætte
                            tempChatIds.set(j, chatIds.get(i));
                            tempNames.set(j, names.get(i));
                            tempLastmessage.set(j, lastMessage.get(i));
                            tempDates.set(j, dates.get(i));
                            tempHeaderList.set(j, headerList.get(i));
                            tempIsInitialized.set(j, isInitialized.get(i));
                            tempLastSender.set(j, lastSender.get(i));
                            tempOtherPersonPic.set(j, otherPersonPic.get(i));
                            break;
                        }
                    }
                }
            }
            // vi overskriver brugerens chatid liste med den nye
            userDTO.setChatId(tempChatIds);

            // Så vi ikke crasher noget
            if (mContext != null) {
                // Fjern alle gamle footviews hvis der var nogle
                while (footerViews.size() != 0) {
                    tilmeldinger_listView.removeFooterView(footerViews.get(0));
                    footerViews.remove(0);
                }
                // Vi laver listviewet for eventsne
                setListView_applicants(eventEventPic, eventHeaders, eventOwnerPic, eventFirstApplicants, eventApplicantPic, eventApplicantsSize);
                LikeSide_Adapter adapter = new LikeSide_Adapter(mContext, tempNames, tempDates, tempLastmessage, tempHeaderList, tempLastSender, tempIsInitialized, otherPersonPic);
                // vi indsætter footviews for alle chats og tilføjer dem til vores liste
                for (int i = 0; i < tempNames.size(); i++) {
                    View v = adapter.getView(i, null, null);
                    tilmeldinger_listView.addFooterView(v);
                    footerViews.add(v);
                }
                // Vi sætter selectionen til det vi har fået fra vores overrided scrolllistener
                tilmeldinger_listView.setSelection(listviewPosition);
            }
            // vi overskriver de lister vi har med den rigtige rækkefølge
            this.dates = tempDates;
            this.names = tempNames;
            this.lastMessage = tempLastmessage;
            this.headerList = tempHeaderList;
            this.lastSender = tempLastSender;
            this.isInitialized = tempIsInitialized;
            this.chatIds = tempChatIds;
        }
    }

    // vi opsætter en lytter til alle brugerens chats
    public void setChangeListeners_chats() {
        for (int j = 0; j < chatIds.size(); j++) {
            // vi opsætter en documentlistener
            FirebaseFirestore.getInstance().collection("chats").document(chatIds.get(j)).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                private static final String TAG = "update from firestore";

                @Override
                public void onEvent(@Nullable DocumentSnapshot snapshot,
                                    @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e);
                        return;
                    }

                    // nogle checks for at være sikker på den eksisterer
                    if (snapshot != null && snapshot.exists()) {
                        // vi konverterer snapshottet til et chat objekt
                        ChatDTO temp = snapshot.toObject(ChatDTO.class);
                        if (temp != null) {
                            if (temp.getDates() != null) {
                                for (int k = 0; k < chatIds.size(); k++) {
                                    // vi finder den rigtige plads med id'et og overskriver dens værdier til de nuværende
                                    if (chatIds.get(k).equals(temp.getChatId())) {
                                        if (!temp.getDates().get(temp.getDates().size() - 1).equals(dates.get(k))) {
                                            dates.set(k, temp.getDates().get(temp.getDates().size() - 1));
                                            lastMessage.set(k, temp.getMessages().get(temp.getMessages().size() - 1));
                                            lastSender.set(k, temp.getSender().get(temp.getMessages().size() - 1));
                                            // genlaver listviewet
                                            setListView_chats(userDTO.getChatId(), names, dates, lastMessage, headerList, lastSender, isInitialized, otherPersonPic);
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

    // changelistener for egne events
    public void setChangeListener_tilmeldinger(){
        // Bruger denne til at tjekke om begge informationer er loadet ind fra databasen (ownerpic og applicantpic)
        boolean[] readies = new boolean[2];
        readies[0] = false;
        readies[1] = false;
        for (int i = 0; i < userController.getCurrUser().getEvents().size(); i++) {
            // sæt changelistener op på alle egne events
            FirebaseFirestore.getInstance().collection("events").document(userController.getCurrUser().getEvents().get(i)).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                private static final String TAG = "update from firestore";

                @Override
                public void onEvent(@Nullable DocumentSnapshot snapshot,
                                    @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e);
                        return;
                    }

                    if (snapshot != null && snapshot.exists()) {
                        // konverter til et event objekt
                        EventDTO temp = snapshot.toObject(EventDTO.class);
                        if (temp != null) {
                            for (int j = 0; j < eventEventID.size(); j++) {
                                // vi finder det sted med det rigtige eventid og erstatter gamle værdier
                                if (eventEventID.get(j).equals(temp.getEventId())){
                                    eventApplicantsSize.set(j,temp.getApplicants().size());
                                    // ternary operator til at få applicanten
                                    String applicant = temp.getApplicants().size() == 0 ? "" : temp.getApplicants().get(0);
                                    eventFirstApplicants.set(j,applicant);
                                    eventParticipant.set(j,temp.getParticipant());
                                    int finalJ1 = j;
                                    // vi bliver altid nødt til at indlæse ejeren billede hvis det nu er blevet ændret
                                    userController.getUser(new CallbackUser() {
                                        @Override
                                        public void onCallback(UserDTO user) {
                                            if (user != null) {
                                                eventOwnerPic.set(finalJ1, user.getUserPicture());
                                                // vi har indlæst owner pic og derfor er den klar til at lave listviewet
                                                readies[0] = true;
                                                // hvis vi har indlæst applicantpic eller hvis der ikke er nogle applicants så laver vi listviewet
                                                if (readies[1] || !(temp.getApplicants().size() > 0)) {
                                                    setListView_chats(userDTO.getChatId(), names, dates, lastMessage, headerList, lastSender, isInitialized, otherPersonPic);
                                                }
                                            }
                                        }
                                    },temp.getOwnerId());

                                    // hvis der er mindst 1 applicant så skal vi hente personens billede ind
                                    if (temp.getApplicants().size() > 0) {
                                        int finalJ = j;
                                        userController.getUser(new CallbackUser() {
                                            @Override
                                            public void onCallback(UserDTO user) {
                                                if (user != null) {
                                                    eventApplicantPic.set(finalJ,user.getUserPicture());
                                                    // vi har indlæst applicantpic og er klar til at lave listviewet
                                                    readies[1] = true;
                                                    // hvis vi har indlæst ownerpic så lav listviewet
                                                    if (readies[0]){
                                                        setListView_chats(userDTO.getChatId(),names,dates,lastMessage,headerList,lastSender,isInitialized,otherPersonPic);
                                                    }
                                                }
                                            }
                                        },temp.getApplicants().get(0));
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

    public void setUserChangeListener(){
        FirebaseFirestore.getInstance().collection("users").document(userController.getCurrUser().getUserId()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            private static final String TAG = "update from firestore";

            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    // konverter til et event objekt
                    UserDTO temp = snapshot.toObject(UserDTO.class);
                    if (temp != null) {
                        if (temp.getChatId() != null){
                            if (temp.getChatId().size() > userDTO.getChatId().size()) {
                                boolean isFound = false;
                                for (int i = 0; i < temp.getChatId().size(); i++) {
                                    for (int j = 0; j < userDTO.getChatId().size(); j++) {
                                        if (temp.getChatId().get(i).equals(userDTO.getChatId().get(j))) {
                                            isFound = true;
                                        }
                                    }
                                    if (!isFound) {
                                        ArrayList<String> tempchatID = userDTO.getChatId();
                                        tempchatID.add(temp.getChatId().get(i));
                                        userDTO.setChatId(tempchatID);
                                        chatDAO.readChat(new ChatDAO.FirestoreCallback() {
                                            @Override
                                            public void onCallback(ChatDTO dto) {
                                                String otherUserID = dto.getUser1ID().equals(userController.getCurrUser().getUserId()) ? dto.getUser2ID() : dto.getUser1ID();
                                                userController.getUser(new CallbackUser() {
                                                    @Override
                                                    public void onCallback(UserDTO user) {
                                                        otherPersonPic.add(user.getUserPicture());
                                                        String nameToAdd = dto.getUser1().equals(userController.getCurrUser().getfName()) ? dto.getUser2() : dto.getUser1();
                                                        names.add(nameToAdd);
                                                        dates.add(dto.getDates().get(dto.getDates().size() - 1));
                                                        lastMessage.add(dto.getMessages().get(dto.getMessages().size() - 1));
                                                        headerList.add(dto.getHeader());
                                                        lastSender.add(dto.getSender().get(dto.getSender().size() - 1));
                                                        isInitialized.add("true");
                                                        setListView_chats(temp.getChatId(), names, dates, lastMessage, headerList, lastSender, isInitialized, otherPersonPic);
                                                    }
                                                }, otherUserID);
                                            }
                                        }, temp.getChatId().get(i));
                                        break;
                                    }
                                    isFound = false;
                                }
                            } else if (tilmeldinger_listView.getFooterViewsCount() < temp.getChatId().size()){
                                chatDAO.readChat(new ChatDAO.FirestoreCallback() {
                                    @Override
                                    public void onCallback(ChatDTO dto) {
                                        String otherUserID = dto.getUser1ID().equals(userController.getCurrUser().getUserId()) ? dto.getUser2ID() : dto.getUser1ID();
                                        userController.getUser(new CallbackUser() {
                                            @Override
                                            public void onCallback(UserDTO user) {
                                                otherPersonPic.add(user.getUserPicture());
                                                chatIds = temp.getChatId();
                                                userDTO.setChatId(temp.getChatId());
                                                String nameToAdd = dto.getUser1().equals(userController.getCurrUser().getfName()) ? dto.getUser2() : dto.getUser1();
                                                names.add(nameToAdd);
                                                dates.add(dto.getDates().get(dto.getDates().size() - 1));
                                                lastMessage.add(dto.getMessages().get(dto.getMessages().size() - 1));
                                                headerList.add(dto.getHeader());
                                                lastSender.add(dto.getSender().get(dto.getSender().size() - 1));
                                                isInitialized.add("true");
                                                setListView_chats(temp.getChatId(), names, dates, lastMessage, headerList, lastSender, isInitialized, otherPersonPic);
                                            }
                                        }, otherUserID);
                                    }
                                }, temp.getChatId().get(temp.getChatId().size()-1));
                            }
                        }
                    }
                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });
    }


    // laver listviewet for egne events
    public void setListView_applicants(@NonNull ArrayList<String> eventEventPic, @NonNull ArrayList<String> eventHeaders, @NonNull ArrayList<String> eventOwnerPic, @NonNull ArrayList<String> eventFirstApplicants, @NonNull ArrayList<String> eventApplicantPic, @NonNull ArrayList<Integer> eventApplicantsSize){
        if (mContext != null) {
            ArrayList<String> tempEventPic = new ArrayList<>(), tempEventHeaders = new ArrayList<>(), tempEventOwnerPic = new ArrayList<>(), tempEventFirstApplicants = new ArrayList<>(), tempEventApplicantPic = new ArrayList<>(), tempEventID = new ArrayList<>();
            ArrayList<Integer> tempEventApplicantsSize = new ArrayList<>();

            for (int i = 0; i < eventHeaders.size(); i++) {
                // vi sorterer alle egne events fra hvis der ikke findes applicants eller hvis der findes en participant
                if (eventApplicantsSize.get(i) != 0 && eventParticipant.get(i).equals("")) {
                    tempEventPic.add(eventEventPic.get(i));
                    tempEventHeaders.add(eventHeaders.get(i));
                    tempEventApplicantPic.add(eventApplicantPic.get(i));
                    tempEventApplicantsSize.add(eventApplicantsSize.get(i));
                    tempEventOwnerPic.add(eventOwnerPic.get(i));
                    tempEventFirstApplicants.add(eventFirstApplicants.get(i));
                    tempEventID.add(eventEventID.get(i));
                }
            }
            this.tempEventID = tempEventID;
            this.tempFirstApplicant = tempEventFirstApplicants;

            // lav adapteren til listviewet og opdater viewet
            LikeSide_Event_Adapter eventAdapter = new LikeSide_Event_Adapter(mContext, tempEventPic, tempEventHeaders, tempEventOwnerPic, tempEventFirstApplicants, tempEventApplicantPic, tempEventApplicantsSize);
            tilmeldinger_listView.setAdapter(eventAdapter);
        }
    }

    // For at få konteksten ligeså snart det er muligt
    @Override
    public void onAttach(@NotNull Context context){
        super.onAttach(context);
        this.mContext = context;
    }

    // For at beholde konteksten indtil sidste mulige tidspunkt
    @Override
    public void onDetach(){
        this.mContext = null;
        super.onDetach();
    }

    // Den her skal køre vores notifikations service senere hen (i think)
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
