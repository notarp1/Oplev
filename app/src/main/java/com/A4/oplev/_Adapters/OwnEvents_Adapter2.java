package com.A4.oplev._Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.A4.oplev.Activity_Event;
import com.A4.oplev.Activity_Profile;
import com.A4.oplev.CreateEvent.Activity_Create_Event;
import com.A4.oplev.R;
import com.google.firebase.firestore.auth.User;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import Controller.EventController;
import Controller.UserController;
import DAL.Classes.ChatDAO;
import DAL.Classes.EventDAO;
import DAL.Classes.UserDAO;
import DAL.Interfaces.CallbackEvent;
import DAL.Interfaces.CallbackUser;
import DAL.Interfaces.IEventDAO;
import DAL.Interfaces.IUserDAO;
import DTO.ChatDTO;
import DTO.EventDTO;
import DTO.UserDTO;

public class OwnEvents_Adapter2 extends RecyclerView.Adapter<OwnEvents_Adapter2.ViewHolder> implements View.OnClickListener {
    private IEventDAO eventDAO = new EventDAO();
    private IUserDAO userDAO = new UserDAO();
    private UserController userController = UserController.getInstance();
    private Context mContext, ctx;
    private ArrayList<Date> eventDate;
    private ArrayList<Integer> eventApplicantsSize;
    private ArrayList<String> eventHeaders, eventEventPic, eventApplicantPic, eventOwnerPic, eventFirstApplicants,eventID, eventParticipant, eventParticipantName;


    public OwnEvents_Adapter2(Context ctx, @NonNull Context context, @NonNull ArrayList<String> eventEventPic, @NonNull ArrayList<String> eventHeaders, @NonNull ArrayList<String> eventOwnerPic, @NonNull ArrayList<String> eventFirstApplicants, @NonNull ArrayList<String> eventApplicantPic, @NonNull ArrayList<Integer> eventApplicantsSize, ArrayList<String> eventID, ArrayList<Date> eventDate, ArrayList<String> eventParticipant, ArrayList<String> eventParticipantName ) {
        this.mContext = context;
        this.eventHeaders = eventHeaders;
        this.eventApplicantsSize = eventApplicantsSize;
        this.eventApplicantPic = eventApplicantPic;
        this.eventEventPic = eventEventPic;
        this.eventOwnerPic = eventOwnerPic;
        this.eventFirstApplicants = eventFirstApplicants;
        this.eventID = eventID;
        this.ctx = ctx;
        this.eventDate = eventDate;
        this.eventParticipant = eventParticipant;
        this.eventParticipantName = eventParticipantName;
    }


    @Override
    public void onClick(View v) {

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.u_settings_event_element, parent, false);
        // Return a new holder instance
        return new OwnEvents_Adapter2.ViewHolder(contactView);
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ImageView eventPic = holder.eventPic;
        ImageView profilePic1 = holder.profilePic1;
        ImageView box = holder.box_own_events;
        CardView profileHolder1 = holder.profileHolder1;
        TextView numberOfApplciants = holder.numberOfApplciants;
        ImageView profilePic2 = holder.profilePic2;
        CardView profilePic2Holder = holder.profilePic2Holder;
        TextView header = holder.header;
        TextView date  = holder.date;
        CardView edit = holder.edit;

        Picasso.get().load(eventEventPic.get(position))
                .resize(mContext.getDisplay().getWidth(), mContext.getDisplay().getHeight() / 2 + 200)
                .centerCrop()
                .placeholder(R.drawable.load2)
                .error(R.drawable.question)
                .into(eventPic);


        if (eventParticipant.get(position)!="") {
            box.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.own_events_occupied_background));
            profilePic2Holder.setVisibility(View.GONE);

            Picasso.get().load(eventApplicantPic.get(position))
                    .resize(mContext.getDisplay().getWidth(), mContext.getDisplay().getHeight() / 2 + 200)
                    .centerCrop()
                    .placeholder(R.drawable.load2)
                    .error(R.drawable.question)
                    .into(profilePic1);

            edit.setVisibility(View.GONE);
            numberOfApplciants.setText(" Med "+eventParticipantName.get(position));

        }

         else if (eventFirstApplicants.get(position).equals("")){
            numberOfApplciants.setText("");
            profileHolder1.setVisibility(View.GONE);
            profilePic2Holder.setVisibility(View.GONE);

        }

        else {
            profilePic1.setVisibility(View.VISIBLE);
            profileHolder1.setVisibility(View.VISIBLE);
            Picasso.get().load(eventApplicantPic.get(position))
                    .resize(mContext.getDisplay().getWidth(), mContext.getDisplay().getHeight() / 2 + 200)
                    .centerCrop()
                    .placeholder(R.drawable.load2)
                    .error(R.drawable.question)
                    .into(profilePic1);

            if (eventApplicantsSize.get(position) == 1) {
                numberOfApplciants.setText(" 1 anmodning");
                profilePic2.setVisibility(View.GONE);
                profilePic2Holder.setVisibility(View.GONE);
            }

            else if (eventApplicantsSize.get(position) >= 2) {
                numberOfApplciants.setText(" " + eventApplicantsSize.get(position) + " anmodninger");
                profilePic2.setVisibility(View.VISIBLE);
                profilePic2Holder.setVisibility(View.VISIBLE);

            }
        }

        // Sætter overskriften for eventet
        header.setText(eventHeaders.get(position));

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/YYYY");
        String strDate = formatter.format(eventDate.get(position));
        date.setText(strDate);

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView eventPic,profilePic1, profilePic2, box_own_events;
        public CardView profileHolder1, profilePic2Holder;
        public TextView numberOfApplciants, header, date;
        public CardView edit, delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            box_own_events = (ImageView)itemView.findViewById(R.id.box_own_events);
            eventPic = (ImageView)itemView.findViewById(R.id.own_event_picture);
            profilePic1 = (ImageView)itemView.findViewById(R.id.own_event_anmodning1_pic);
            profileHolder1 =  (CardView)itemView.findViewById(R.id.own_event_anmodning1_holder);
            profilePic2 = (ImageView)itemView.findViewById(R.id.own_event_anmodning2_pic);
            profilePic2Holder = (CardView) itemView.findViewById(R.id.own_event_anmodning2_holder);
            numberOfApplciants = (TextView)itemView.findViewById(R.id.own_event_antal_anmodning);
            header = (TextView)itemView.findViewById(R.id.own_event_headline);
            date = (TextView)itemView.findViewById(R.id.own_event_beskeder_events_dato);
            edit = (CardView)itemView.findViewById(R.id.own_event_edit_holder);
            delete = (CardView)itemView.findViewById(R.id.own_event_delete_holder);

            edit.setOnClickListener(this);
            delete.setOnClickListener(this);
            box_own_events.setOnClickListener(this);
            profileHolder1.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (v == box_own_events){

                eventDAO.getEvent(event -> {
                    userController.getUser(user -> {
                        Intent intent = new Intent(mContext, Activity_Event.class);
                        intent.putExtra("user", user);
                        intent.putExtra("load", 1);
                        intent.putExtra("event", event);
                        mContext.startActivity(intent);
                    }, event.getOwnerId());

                },eventID.get(getPosition()));
            }
            if (v == profileHolder1){

                eventDAO.getEvent(event -> {
                    userController.getUser(user -> {
                        Intent intent = new Intent(mContext, Activity_Profile.class);
                        intent.putExtra("user", user);
                        intent.putExtra("load", 1);
                        mContext.startActivity(intent);
                    }, event.getApplicants().get(0));
                },eventID.get(getPosition()));
            }


            if(v == edit){
                //Edit post
                eventDAO.getEvent(new CallbackEvent() {
                    @Override
                    public void onCallback(EventDTO event) {
                        if((event.getParticipant() == null || event.getParticipant().equals("")) && event.getApplicants().isEmpty()) {
                            Intent i = new Intent(v.getContext(), Activity_Create_Event.class);
                            i.putExtra("event", event);
                            i.putExtra("edit", true);
                            mContext.startActivity(i);
                        }else{
                            Toast toast = Toast.makeText(mContext,"Denne event har ansøgere eller en deltager, slet for dem for at redigere", Toast.LENGTH_SHORT);
                            toast.show();
                        }

                    }
                }, eventID.get(getPosition()));

            }else if(v == delete){
                //Delete

                AlertDialog.Builder builder = new AlertDialog.Builder(ctx);

                builder.setTitle("Slet Event");
                builder.setMessage("Er du sikker på du vil slette dit event? ");

                builder.setPositiveButton("JA, SLET EVENT.", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                     /*   eventDAO.getEvent(new CallbackEvent() {
                            @Override
                            public void onCallback(EventDTO event) {
                                EventController eventController = EventController.getInstance();

                                if(event.getParticipant().equals(""))eventController.deleteEvent(event.getEventId(), 0, event.getChatId(), event.getOwnerId());
                                else eventController.deleteEvent(event.getEventId(), 1, event.getChatId(), event.getParticipant());


                            }
                        }, eventID.get(getPosition()));*/
                        Toast.makeText(mContext.getApplicationContext(), "Slet event er ikke færdig-implementeret.", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("NEJ, BEHOLD EVENT.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();

            }
        }
    }

    @Override
    public int getItemCount() {
        return this.eventOwnerPic.size();
    }
}
