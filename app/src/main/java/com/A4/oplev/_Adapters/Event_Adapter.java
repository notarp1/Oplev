package com.A4.oplev._Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.A4.oplev.Activity_Event;
import com.A4.oplev.Activity_Profile;
import com.A4.oplev.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import Controller.UserController;
import DAL.Classes.EventDAO;
import DAL.Classes.UserDAO;
import DAL.Interfaces.CallBackEventList;
import DAL.Interfaces.CallbackEvent;
import DAL.Interfaces.CallbackUser;
import DAL.Interfaces.IEventDAO;
import DAL.Interfaces.IUserDAO;
import DTO.EventDTO;
import DTO.UserDTO;

public class Event_Adapter extends RecyclerView.Adapter<Event_Adapter.ViewHolder>implements View.OnClickListener {

    static Event_Adapter instance = null;

    String TAG = "EventA";
    List<String> eventListId;
    List<EventDTO> loadedEvent;
    int offset = 0;
    IEventDAO dataA;
    int height;
    int width;
    Context ctx;
    // TODO Lav classen om så den kun henter Event en gang, og ikke 2 - 3 gange Alexander skal lave det når der er tid.
    //EventDTO eventDTO;

    public static Event_Adapter getInstance(List<EventDTO> scoreList, List<String> ids, Context frame, int height, int width) {
        if (instance == null) {
            instance = new Event_Adapter(scoreList, ids, frame, height, width);
        }
        return instance;
    }

    public static Event_Adapter getInstance() {
        return instance;
    }

    private Event_Adapter(List<EventDTO> scoreList, List<String> ids, Context frame, int height, int width) {
        this.ctx = frame;
        this.dataA = new EventDAO();
        this.height = height;
        this.width = width;
        this.eventListId = ids;
        this.loadedEvent = scoreList;

    }

    public void refreshData(List<String> ids) {
        this.eventListId = ids;
        ((EventDAO) dataA).getEvents(new CallBackEventList() {
            @Override
            public void onCallback(List<EventDTO> events) {
                loadedEvent = events;
            }
        }, ids);
    }


    public void loadData(boolean way) {
        // Metode til at hente data ind i loaded listen, så der hele tiden kun er tre udfyldte EventDto'er i hukkomelsen.
        if (way) {
            //Going to the right, first is dumped
            if (offset != 0) {
                loadedEvent.remove(0);
            }
            if (offset < eventListId.size()) {
                offset++;
                add2list(offset + 1);
            }
        } else {
            if (offset != eventListId.size() - 1) {
                loadedEvent.remove(2);
            }
            if (offset != 0) {
                offset--;
                add2listStart(offset - 1);
            }
        }
    }

    public void add2list(int pos) {
        //Henter Data ind i loadEvent, i sluttningen.
        //loadedEvent.add(dataA.getEvent(eventListId.get(eventListId.get(pos))));
    }

    public void add2listStart(int pos) {
        //Henter Data ind i loadEvent, i sluttningen.
        List<EventDTO> newList = new ArrayList<>();
        // newList.add(dataA.getEvent(eventListId.get(eventListId.get(pos))));
        newList.add(loadedEvent.get(0));
        newList.add(loadedEvent.get(1));
        loadedEvent = newList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.eventlist_item, parent, false);
        // Return a new holder instance
        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        IUserDAO userDAO = new UserDAO();
        // Get the data model based on position
        //eventDTO = loadedEvent.get(position);
        // Set item views based on your views and data model
        ImageView profilePic = holder.profilePic;
        ImageView mainPic = holder.mainPic;
        TextView withWhoText = holder.withWhoText;
        TextView headlineText = holder.headlineText;


        // her skal dataen sættes in i holderen, der skal gøres brug af en billed controller til at håndtere billder.

        dataA.getEvent(new CallbackEvent() {
            @Override
            public void onCallback(EventDTO eventDTO) {
                userDAO.getUser(new CallbackUser() {
                    @Override
                    public void onCallback(UserDTO user) {
                        System.out.println(eventDTO.getOwnerId() + "HAHA2");
                        withWhoText.setText(user.getfName());
                        headlineText.setText(eventDTO.getTitle());

                        Picasso.get().load(user.getUserPicture())
                                .resize(width / 8, height / 16)
                                .centerCrop()
                                .placeholder(R.drawable.load2)
                                .error(R.drawable.question)
                                .transform(new RoundedTransformation(90, 0))
                                .into(profilePic);

                        Picasso.get().load(eventDTO.getEventPic())
                                .resize(width, height)
                                .centerCrop()
                                .placeholder(R.drawable.load2)
                                .error(R.drawable.question)
                                .into(mainPic);

                                   }
                               }, eventDTO.getOwnerId());
                           }
                       }
                ,eventListId.get(position));

    }

    public void dataCleanUp(int pos) {

    }


    @Override
    public int getItemCount() {
        return eventListId.size();
    }

    @Override
    public void onClick(View view) {

    }

    public void removeTopItem() {

    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView mainPic, profilePic;
        public TextView headlineText, withWhoText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mainPic = (ImageView) itemView.findViewById(R.id.eventItem_Billede);
            profilePic = (ImageView) itemView.findViewById(R.id.eventItem_pp);
            withWhoText = (TextView) itemView.findViewById(R.id.evntItem_withWho);
            headlineText = (TextView) itemView.findViewById(R.id.eventitem_Headline);
            profilePic.setOnClickListener(this);
            mainPic.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int id = this.getLayoutPosition();
            UserController user = UserController.getInstance();

            if (view == profilePic) {

                dataA.getEvent(new CallbackEvent(){

                    @Override
                    public void onCallback(EventDTO event) {
                        user.getUser(new CallbackUser() {
                            @Override
                            public void onCallback(UserDTO user) {
                                Log.d(TAG, "onCallback: " + id + " username: " + user.getfName() + " EventID: " + event.getEventId());
                                Intent i = new Intent(ctx, Activity_Profile.class);
                                i.putExtra("user", user);
                                i.putExtra("load", 1);
                                ctx.startActivity(i);
                            }
                        }, event.getOwnerId());
                    }
                }, eventListId.get(id));


            }

            if (view == mainPic) {

                dataA.getEvent(new CallbackEvent() {
                    @Override
                    public void onCallback(EventDTO event) {
                        user.getUser(new CallbackUser() {
                            @Override
                            public void onCallback(UserDTO user) {
                                Intent i = new Intent(ctx, Activity_Event.class);
                                i.putExtra("user", user);
                                i.putExtra("event", event);
                                ctx.startActivity(i);
                            }
                        }, event.getOwnerId());
                    }
                }, eventListId.get(id));
            }
        }
    }
}
