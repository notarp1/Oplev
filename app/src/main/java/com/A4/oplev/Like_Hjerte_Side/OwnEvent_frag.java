package com.A4.oplev.Like_Hjerte_Side;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.fragment.app.Fragment;

import com.A4.oplev.Activity_Event;
import com.A4.oplev.Activity_Profile;
import com.A4.oplev.Chat.Activity_Chat;
import com.A4.oplev.R;
import com.A4.oplev._Adapters.OwnEvents_Adapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;

import Controller.Listeners.OnSwipeTouchListener;
import Controller.UserController;
import DAL.Classes.EventDAO;
import DTO.UserDTO;

public class OwnEvent_frag extends Fragment {
    private ListView ownEvents_listView;
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

    boolean eventsReady = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater i, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = i.inflate(R.layout.u_settings_events_frag, container, false);

        userController = UserController.getInstance();
        userDTO = userController.getCurrUser();
        eventDAO = new EventDAO();

        ownEvents_listView = root.findViewById(R.id.own_events_list);
        redigerKnap = root.findViewById(R.id.own_event_edit_holder);
        sletKnap = root.findViewById(R.id.own_event_delete_holder);

        ownEvents_listView.setOnTouchListener(new OnSwipeTouchListener(mContext){
            @SuppressLint("ResourceAsColor")
            @Override
            public void onSwipeLeft() {
                // Sæt farven på billederne i toppen af skærmen
                getActivity().findViewById(R.id.besked_back).setVisibility(View.VISIBLE);
                getActivity().findViewById(R.id.event_back).setVisibility(View.INVISIBLE);
                getActivity().findViewById(R.id.hjerte_back).setVisibility(View.INVISIBLE);

                // Kreer fragmentet over til hjertesiden
                getFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_from_right,
                        R.anim.exit_to_left).replace(R.id.likeside_frameLayout,new LikesideList_frag())
                        .commit();
            }
        });

        // *Todo - Skal vise side for eget event (Antager at der skal laves en nyt xml dokument - Men hvad skal vises?)
        // * Todo - Lav visuel forskel når en applicant er accepteret, og eventet dermed er faslagt. (Vis tilmeldt osv)
        ownEvents_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position < eventHeaders.size()) {
                    ArrayList<String> applicants = new ArrayList<>();
                    //sendNoti();
                    //Toast.makeText(mContext, "Notifikation sendt", Toast.LENGTH_SHORT).show();

                    eventDAO.getEvent(event -> {

                        userController.getUser(user -> {
                            Intent intent = new Intent(mContext, Activity_Event.class);
                            intent.putExtra("user", user);
                            intent.putExtra("load", 1);
                            intent.putExtra("event", event);
                            mContext.startActivity(intent);
                        }, event.getOwnerId());

                    },"1v3EKAc0iNWJvebUDW6u");
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
                    setListView_applicants(eventEventPic, eventHeaders, eventOwnerPic, eventFirstApplicants, eventApplicantPic, eventApplicantsSize);
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
                                    setListView_applicants(eventEventPic, eventHeaders, eventOwnerPic, eventFirstApplicants, eventApplicantPic, eventApplicantsSize);
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
                                    eventFirstApplicants.add(firstApplicant);
                                    eventEventID.add(event.getEventId());
                                    eventParticipant.add(event.getParticipant());
                                    // nu har vi hentet færdigt
                                    isGettingFromDB[0] = false;
                                    // hvis det var sidste event så kør dette
                                    if (eventApplicantPic.size() == userDTO.getEvents().size()) {
                                        eventsReady = true;
                                        // hvis chats er færdige så opsæt listview og listeners
                                        setListView_applicants(eventEventPic, eventHeaders, eventOwnerPic, eventFirstApplicants, eventApplicantPic, eventApplicantsSize);
                                        //setChangeListener_tilmeldinger();
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

        public void setListView_applicants(@NonNull ArrayList < String > eventEventPic, @NonNull ArrayList < String > eventHeaders, @NonNull ArrayList < String > eventOwnerPic, @NonNull ArrayList < String > eventFirstApplicants, @NonNull ArrayList < String > eventApplicantPic, @NonNull ArrayList < Integer > eventApplicantsSize)
        {
            if (mContext != null) {
                ArrayList<String> tempEventPic = new ArrayList<>(), tempEventHeaders = new ArrayList<>(), tempEventOwnerPic = new ArrayList<>(), tempEventFirstApplicants = new ArrayList<>(), tempEventApplicantPic = new ArrayList<>(), tempEventID = new ArrayList<>();
                ArrayList<Integer> tempEventApplicantsSize = new ArrayList<>();

                for (int i = 0; i < eventHeaders.size(); i++) {
                        tempEventPic.add(eventEventPic.get(i));
                        tempEventHeaders.add(eventHeaders.get(i));
                        tempEventApplicantPic.add(eventApplicantPic.get(i));
                        tempEventApplicantsSize.add(eventApplicantsSize.get(i));
                        tempEventOwnerPic.add(eventOwnerPic.get(i));
                        tempEventFirstApplicants.add(eventFirstApplicants.get(i));
                        tempEventID.add(eventEventID.get(i));
                }
                this.tempEventID = tempEventID;
                this.tempFirstApplicant = tempEventFirstApplicants;

                Log.d("eventSize test3",  tempEventApplicantsSize.toString());

                OwnEvents_Adapter eventAdapter = new OwnEvents_Adapter(mContext, tempEventPic, tempEventHeaders, tempEventOwnerPic, tempEventFirstApplicants, tempEventApplicantPic, tempEventApplicantsSize);
                ownEvents_listView.setAdapter(eventAdapter);
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

