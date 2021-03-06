package com.A4.oplev.Like_Hjerte_Side;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import Controller.Listeners.OnSwipeTouchListener;
import Controller.UserController;
import DAL.Classes.ChatDAO;
import DAL.Classes.EventDAO;
import DAL.Classes.UserDAO;
import DAL.Interfaces.CallbackEvent;
import DAL.Interfaces.CallbackUser;
import DTO.ChatDTO;
import DTO.EventDTO;
import DTO.UserDTO;

import com.A4.oplev.R;
import com.A4.oplev._Adapters.Hjerteside_Adapter;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.Date;

public class HjerteSide_frag extends Fragment{
    private Context mContext;
    private ListView listView;
    private UserDTO currentUser;
    private UserDAO dao = new UserDAO();
    private EventDAO eventDAO = new EventDAO();
    private ArrayList<String> headerList = new ArrayList<>(), names = new ArrayList<>(), profilePictures = new ArrayList<>(), placementList = new ArrayList<>(), eventPictureList = new ArrayList<>(), ageList = new ArrayList<>(), eventIDList = new ArrayList<>();
    private ArrayList<Date> timeList = new ArrayList<>();
    private ArrayList<Integer> priceList = new ArrayList<>();
    private TextView nomessages;

    @SuppressLint("ClickableViewAccessibility")
    public View onCreateView(LayoutInflater i, ViewGroup container, Bundle savedInstanceState) {
        View root = i.inflate(R.layout.hjerteside_frag,container,false);

        // sæt positionen til hjeteside inde i activity likeside
        Activity_Likeside.setPosition(2);

        listView = root.findViewById(R.id.hjerteside_listview);
        nomessages = root.findViewById(R.id.nomessages_hjerte);
        currentUser = UserController.getInstance().getCurrUser();

        if(currentUser.getLikedeEvents() != null) {
            if (currentUser.getLikedeEvents().size() == 0) {
                nomessages.setVisibility(View.VISIBLE);
            } else nomessages.setVisibility(View.GONE);


            // find alle likede events
            for (int j = 0; j < currentUser.getLikedeEvents().size(); j++) {
                // hent eventet
                eventDAO.getEvent(event ->
                        // hent ejerens bruger
                        dao.getUser(user ->
                        {
                            // tilføj nødvendige informationer som skal bruges i adapteren
                            headerList.add(event.getTitle());
                            names.add(user.getfName());
                            profilePictures.add(user.getUserPicture());
                            placementList.add(event.getCity());
                            eventPictureList.add(event.getEventPic());
                            timeList.add(event.getDate());
                            priceList.add(event.getPrice());
                            eventIDList.add(event.getEventId());
                            ageList.add(user.getAge() + "");
                            // hvis det er den sidste tilføjede event så opsæt views og sæt change listener på brugeren
                            if (headerList.size() == currentUser.getLikedeEvents().size()) {
                                Hjerteside_Adapter adapter = new Hjerteside_Adapter(mContext, headerList, names, profilePictures, placementList, timeList, priceList, eventPictureList, ageList, eventIDList);
                                listView.setAdapter(adapter);
                                setUserChangeListener();
                            }
                        }, event.getOwnerId()), currentUser.getLikedeEvents().get(j));
            }
        }



        // Sætter en swipelistener op for at kunne swipe til siden og kunne gå tilbage til likesiden (med chatsne)
        listView.setOnTouchListener(new OnSwipeTouchListener(getContext()){
            @Override
            public void onSwipeRight() {
                getActivity().findViewById(R.id.besked_back).setVisibility(View.VISIBLE);
                getActivity().findViewById(R.id.hjerte_back).setVisibility(View.INVISIBLE);
                getActivity().findViewById(R.id.event_back).setVisibility(View.INVISIBLE);

                getFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.slide_in_left,
                        android.R.anim.slide_out_right).replace(R.id.likeside_frameLayout,new LikesideList_frag())
                        .commit();
            }
        });


        return root;
    }

    public void setUserChangeListener() {
        FirebaseFirestore.getInstance().collection("users").document(currentUser.getUserId()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            private static final String TAG = "update from firestore";

            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                // vi skal ikke ændre noget hvis aktiviteten er stoppet
                if (mContext != null) {
                    if (snapshot != null && snapshot.exists()) {
                        // konverter til et event objekt
                        UserDTO temp = snapshot.toObject(UserDTO.class);
                        if (temp != null) {
                            if (temp.getLikedeEvents() != null) {
                                // Tjek om et liket event er blevet fjernet (man kan ikke like nogle events mens man er på denne side så at tjekke om det er under er ligegyldigt)
                                if (temp.getLikedeEvents().size() < eventIDList.size()) {
                                    // boolean til at tjekke om vi har fundet det event der er blevet fjernet
                                    boolean isFound = false;
                                    // iterer alle events
                                    for (int i = 0; i < eventIDList.size(); i++) {
                                        // nulstil boolean til næste iteration
                                        isFound = false;
                                        for (int j = 0; j < temp.getLikedeEvents().size(); j++) {
                                            // hvis et event id er det samme så sæt til true for at være fundet
                                            if (eventIDList.get(i).equals(temp.getLikedeEvents().get(j))) {
                                                isFound = true;
                                            }
                                        }
                                        // hvis eventet ikke findes i temp listen så skal det fjernes fra vores lister
                                        if (!isFound) {
                                            eventIDList.remove(i);
                                            headerList.remove(i);
                                            names.remove(i);
                                            profilePictures.remove(i);
                                            placementList.remove(i);
                                            eventPictureList.remove(i);
                                            timeList.remove(i);
                                            priceList.remove(i);
                                            ageList.remove(i);
                                            // genlav viewsne
                                            Hjerteside_Adapter adapter = new Hjerteside_Adapter(mContext, headerList, names, profilePictures, placementList, timeList, priceList, eventPictureList, ageList, eventIDList);
                                            listView.setAdapter(adapter);
                                            UserController.getInstance().setCurrUser(temp);
                                            currentUser = temp;
                                            if (currentUser.getLikedeEvents().size() == 0){
                                                nomessages.setVisibility(View.VISIBLE);
                                            } else nomessages.setVisibility(View.GONE);
                                            // stop loopsne
                                            break;
                                        }
                                    }
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


    @Override
    public void onAttach(Context ctx){
        this.mContext = ctx;
        super.onAttach(ctx);
    }

    @Override
    public void onDetach(){
        this.mContext = null;
        super.onDetach();
    }
}
