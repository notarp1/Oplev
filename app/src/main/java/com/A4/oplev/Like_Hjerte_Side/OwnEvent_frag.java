package com.A4.oplev.Like_Hjerte_Side;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.A4.oplev.R;
import com.A4.oplev._Adapters.OwnEvents_Adapter2;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;

import Controller.UserController;
import DAL.Classes.EventDAO;
import DTO.UserDTO;

public class OwnEvent_frag extends Fragment {
    private RecyclerView recyclerView;
    private EventDAO eventDAO;
    private UserDTO userDTO;
    private UserController userController;
    private CardView sletKnap, redigerKnap;
    private String currentUser;
    private ArrayList<Date> dates = new ArrayList<>();
    private ArrayList<Integer> eventApplicantsSize = new ArrayList<>();
    private ArrayList<String> eventHeaders = new ArrayList<>(), eventOwnerPic = new ArrayList<>(),  eventEventPic = new ArrayList<>(),
                              eventApplicantPic = new ArrayList<>(), eventFirstApplicants = new ArrayList<>(), eventEventID = new ArrayList<>(),
                              eventParticipant = new ArrayList<>(), tempEventID = new ArrayList<>(), tempFirstApplicant = new ArrayList<>(),
                              names = new ArrayList<>();
    private Context mContext;
    View root2;
    boolean eventsReady = false;

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater i, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = i.inflate(R.layout.u_settings_events_frag, container, false);
        // sæt positionen til ownevent inde i activity likeside
        Activity_Likeside.setPosition(0);
        root2 = root;
        userController = UserController.getInstance();
        userDTO = userController.getCurrUser();
        eventDAO = new EventDAO();

        recyclerView = root.findViewById(R.id.ownEvents_Recycler);
        redigerKnap = root.findViewById(R.id.own_event_edit_picture);
        sletKnap = root.findViewById(R.id.own_event_delete_holder);




        // *Todo - Skal vise side for eget event (Antager at der skal laves en nyt xml dokument - Men hvad skal vises?)
        // *Todo - Organiser rækkefølge på events.
        // Todo - Lav visuel forskel når en applicant er accepteret, og eventet dermed er faslagt. (Vis tilmeldt osv)
        // Todo - Lav slet metode der fjerne det valgte element - og lav/ benyt slet metode fra backend.
        // todo - Lav rediger metode der virker på det valgte element - og lav/ benyt slet metode fra backend.



        // Det her er til ens egne events som andre prøver på at ansøge om at joine
        // vi prøver på at hente brugeren ind hvis den af en eller anden grund ikke er
        if (userDTO == null) userDTO = userController.getCurrUser();
        // vi bruger den her til ikke at instantiere listviewet hvis der bliver hentet noget fra databasen
        final boolean[] isGettingFromDB = {false};
        if (userDTO != null) {
            if (userDTO.getEvents() != null) {
                // hvis brugeren ikke har nogle events så skal vi sikre os at det ikke går i stå
                if (userDTO.getEvents().size() == 0) {
                    eventsReady = true;
                    setListView_events(eventEventPic, eventHeaders, eventOwnerPic, eventFirstApplicants, eventApplicantPic, eventApplicantsSize, eventEventID, dates);
                    // setChangeListener_tilmeldinger();

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
                                dates.add(event.getDate());
                                eventApplicantsSize.add(event.getApplicants().size());
                                eventHeaders.add(event.getTitle());
                                eventOwnerPic.add(event.getOwnerPic());
                                eventEventPic.add(event.getEventPic());
                                eventFirstApplicants.add(firstApplicant);
                                Log.d("eventSize test1",  eventApplicantsSize.toString());
                                eventEventID.add(event.getEventId());
                                eventParticipant.add(event.getParticipant());
                                // hvis det er den sidste event OG vi ikke henter noget fra databasen så kan vi køre den her kode
                                if (eventApplicantPic.size() == userDTO.getEvents().size() && !isGettingFromDB[0]) {
                                    // events er loadet færdigt
                                    eventsReady = true;
                                    // hvis chats er loadet færdigt så opsæt listviewet og listeners
                                    setListView_events(eventEventPic, eventHeaders, eventOwnerPic, eventFirstApplicants, eventApplicantPic, eventApplicantsSize, eventEventID, dates);
                                    // setChangeListener_tilmeldinger();
                                }
                            } else {
                                // hvis der er mindst 1 applicant så skal vi hente noget ind fra databasen
                                isGettingFromDB[0] = true;
                                // vi henter applicanten og indsætter personens værdier
                                userController.getUser(user -> {
                                    eventApplicantPic.add(user.getUserPicture());
                                    eventApplicantsSize.add(event.getApplicants().size());
                                    Log.d("eventSize test2",  eventApplicantsSize.toString());
                                    eventHeaders.add(event.getTitle());
                                    eventOwnerPic.add(event.getOwnerPic());
                                    eventEventPic.add(event.getEventPic());
                                    dates.add(event.getDate());
                                    eventFirstApplicants.add(firstApplicant);
                                    eventEventID.add(event.getEventId());
                                    eventParticipant.add(event.getParticipant());
                                    // nu har vi hentet færdigt
                                    isGettingFromDB[0] = false;
                                    // hvis det var sidste event så kør dette
                                    if (eventApplicantPic.size() == userDTO.getEvents().size()) {
                                        eventsReady = true;
                                        // hvis chats er færdige så opsæt listview og listeners
                                        setListView_events(eventEventPic, eventHeaders, eventOwnerPic, eventFirstApplicants, eventApplicantPic, eventApplicantsSize, eventEventID, dates);
                                        //setChangeListener_tilmeldinger();
                                    }
                                }, event.getApplicants().get(0));
                            }
                        }
                    }, userDTO.getEvents().get(j));
                }
            }
        };

        // Todo - OnSwipe virker ikke på recylerviewet - HELP
      /*  recyclerView.setOnTouchListener(new OnSwipeTouchListener(mContext){
            @Override
            public void onSwipeLeft() {
                // Sæt farven på billederne i toppen af skærmen
                getActivity().findViewById(R.id.besked_back).setVisibility(View.INVISIBLE);
                getActivity().findViewById(R.id.event_back).setVisibility(View.VISIBLE);
                getActivity().findViewById(R.id.hjerte_back).setVisibility(View.INVISIBLE);

                // Kreer fragmentet over til hjertesiden
                getFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right,
                        R.anim.exit_to_left).replace(R.id.likeside_frameLayout,new LikesideList_frag())
                        .commit();
            }
        });*/


        return root;
    }
// Todo - Listing virker ikke efter hensigten endnu.
        public void setListView_events(@NonNull ArrayList < String > eventEventPic, @NonNull ArrayList < String > eventHeaders, @NonNull ArrayList < String > eventOwnerPic, @NonNull ArrayList < String > eventFirstApplicants, @NonNull ArrayList < String > eventApplicantPic, @NonNull ArrayList < Integer > eventApplicantsSize, @NonNull ArrayList < String > eventEventID, @NonNull ArrayList < Date > dates )
        {
            if (mContext != null) {
                ArrayList<String> tempEventPic = new ArrayList<>(), tempEventHeaders = new ArrayList<>(), tempEventOwnerPic = new ArrayList<>(), tempEventFirstApplicants = new ArrayList<>(), tempEventApplicantPic = new ArrayList<>(), tempEventID = new ArrayList<>();
                ArrayList<Integer> tempEventApplicantsSize = new ArrayList<>();
                ArrayList<Date> tempDate = new ArrayList<>();

                for (int i = 0; i < eventHeaders.size(); i++) {
                    tempEventPic.add(eventEventPic.get(i));
                    tempEventHeaders.add(eventHeaders.get(i));
                    tempEventApplicantPic.add(eventApplicantPic.get(i));
                    tempEventApplicantsSize.add(eventApplicantsSize.get(i));
                    tempEventOwnerPic.add(eventOwnerPic.get(i));
                    tempEventFirstApplicants.add(eventFirstApplicants.get(i));
                    tempEventID.add(eventEventID.get(i));
                    tempDate.add(dates.get(i));
                    Log.d("list1", String.valueOf(eventApplicantsSize.size()));

                    /*
                    if(i==0) {
                        tempEventPic.add(eventEventPic.get(i));
                        tempEventHeaders.add(eventHeaders.get(i));
                        tempEventApplicantPic.add(eventApplicantPic.get(i));
                        tempEventApplicantsSize.add(eventApplicantsSize.get(i));
                        tempEventOwnerPic.add(eventOwnerPic.get(i));
                        tempEventFirstApplicants.add(eventFirstApplicants.get(i));
                        tempEventID.add(eventEventID.get(i));
                        tempDate.add(dates.get(i));
                    }
                    else if (tempDate.get(i-1).after(dates.get(i))){
                        tempEventPic.add(eventEventPic.get(i));
                        tempEventHeaders.add(eventHeaders.get(i));
                        tempEventApplicantPic.add(eventApplicantPic.get(i));
                        tempEventApplicantsSize.add(eventApplicantsSize.get(i));
                        tempEventOwnerPic.add(eventOwnerPic.get(i));
                        tempEventFirstApplicants.add(eventFirstApplicants.get(i));
                        tempEventID.add(eventEventID.get(i));
                        tempDate.add(dates.get(i));
                    }
                    else  {
                        // iterer over alle temp dates
                        for (int j = 0; j < i; j++) {
                            // hvis den man vil indsætte kommer efter den dato på plads j
                            if (dates.get(i).after(tempDate.get(j))) {
                                // iterer fra i'endes plads til j
                                for (int k = i; k >= j; k--) {
                                    // ved den første tilføjer vi fra den forriges plads (flyt den sidste dato til en ny plads)
                                    if (k == i) {
                                        tempEventPic.add(eventEventPic.get(i-1));
                                        tempEventHeaders.add(eventHeaders.get(i-1));
                                        tempEventApplicantPic.add(eventApplicantPic.get(i-1));
                                        tempEventApplicantsSize.add(eventApplicantsSize.get(i-1));
                                        tempEventOwnerPic.add(eventOwnerPic.get(i-1));
                                        tempEventFirstApplicants.add(eventFirstApplicants.get(i-1));
                                        tempEventID.add(eventEventID.get(i-1));
                                        tempDate.add(dates.get(i-1));
                                    }
                                    // fra alle pladser derefter så rykker vi pladsen 1 til højre
                                    else if (k != 0) {
                                        tempEventPic.add(eventEventPic.get(Math.max(i-1,0)));
                                        tempEventHeaders.add(eventHeaders.get(Math.max(i-1,0)));
                                        tempEventApplicantPic.add(eventApplicantPic.get(Math.max(i-1,0)));
                                        tempEventApplicantsSize.add(eventApplicantsSize.get(Math.max(i-1,0)));
                                        tempEventOwnerPic.add(eventOwnerPic.get(Math.max(i-1,0)));
                                        tempEventFirstApplicants.add(eventFirstApplicants.get(Math.max(i-1,0)));
                                        tempEventID.add(eventEventID.get(Math.max(i-1,0)));
                                        tempDate.add(dates.get(Math.max(i-1,0)));
                                    }
                                }
                                // når vi har rykket alle gamle datoer så indsætter vi den nye dato vi vil indsætte
                                tempEventPic.set(j,eventEventPic.get(i));
                                tempEventHeaders.set(j,eventHeaders.get(i));
                                tempEventApplicantPic.set(j, eventApplicantPic.get(i));
                                tempEventApplicantsSize.set(j, eventApplicantsSize.get(i));
                                tempEventOwnerPic.set(j, eventOwnerPic.get(i));
                                tempEventFirstApplicants.set(j, eventFirstApplicants.get(i));
                                tempEventID.set (j, eventEventID.get(i));
                                tempDate.set(j, dates.get(i));
                                break;
                            }
                        }

                    }*/
                }
                this.tempEventID = tempEventID;
                this.tempFirstApplicant = tempEventFirstApplicants;

                Log.d("eventSize test3",  tempEventApplicantsSize.toString());

                LinearLayoutManager layoutManager = new LinearLayoutManager(root2.getContext(), LinearLayoutManager.VERTICAL, false);
                OwnEvents_Adapter2 eventAdapter = new OwnEvents_Adapter2(root2.getContext(), tempEventPic, tempEventHeaders, tempEventOwnerPic, tempEventFirstApplicants, tempEventApplicantPic, tempEventApplicantsSize, tempEventID, tempDate);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(eventAdapter);
            }
        }




    @Override
    public void onAttach(@NotNull Context context){
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onDetach(){
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

