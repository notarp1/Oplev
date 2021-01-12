package com.A4.oplev._Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.A4.oplev.Activity_Event;
import com.A4.oplev.CreateEvent.Activity_Create_Event;
import com.A4.oplev.Like_Hjerte_Side.Activity_Likeside;
import com.A4.oplev.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import Controller.UserController;
import DAL.Classes.EventDAO;

public class OwnEvents_Adapter2 extends RecyclerView.Adapter<OwnEvents_Adapter2.ViewHolder> implements View.OnClickListener {
    private EventDAO eventDAO = new EventDAO();
    private UserController userController = UserController.getInstance();
    private Context mContext;
    private ArrayList<Integer> eventApplicantsSize;
    private ArrayList<String> eventHeaders, eventEventPic, eventApplicantPic, eventOwnerPic, eventFirstApplicants,eventID ;


    public OwnEvents_Adapter2(@NonNull Context context, @NonNull ArrayList<String> eventEventPic, @NonNull ArrayList<String> eventHeaders, @NonNull ArrayList<String> eventOwnerPic, @NonNull ArrayList<String> eventFirstApplicants, @NonNull ArrayList<String> eventApplicantPic, @NonNull ArrayList<Integer> eventApplicantsSize, ArrayList<String> eventID) {
        this.mContext = context;
        this.eventHeaders = eventHeaders;
        this.eventApplicantsSize = eventApplicantsSize;
        this.eventApplicantPic = eventApplicantPic;
        this.eventEventPic = eventEventPic;
        this.eventOwnerPic = eventOwnerPic;
        this.eventFirstApplicants = eventFirstApplicants;
        this.eventID = eventID;
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
        CardView profileHolder1 = holder.profileHolder1;
        TextView numberOfApplciants = holder.numberOfApplciants;
        ImageView profilePic2 = holder.profilePic2;
        CardView profilePic2Holder = holder.profilePic2Holder;
        TextView header = holder.header;
        TextView date  = holder.date;

        Picasso.get().load(eventEventPic.get(position))
                .resize(mContext.getDisplay().getWidth(), mContext.getDisplay().getHeight() / 2 + 200)
                .centerCrop()
                .placeholder(R.drawable.load2)
                .error(R.drawable.question)
                .into(eventPic);

        if (eventFirstApplicants.get(position).equals("")){
            numberOfApplciants.setText("");
            profilePic1.setVisibility(View.GONE);
            profileHolder1.setVisibility(View.GONE);
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
        }

        if (eventApplicantsSize.get(position)==1){
            numberOfApplciants.setText(" 1 anmodning");
            profilePic2.setVisibility(View.GONE);
            profilePic2Holder.setVisibility(View.GONE);
        }

        if (eventApplicantsSize.get(position)<1){
            profilePic2.setVisibility(View.GONE);
            profilePic2Holder.setVisibility(View.GONE);
        }


        else if (eventApplicantsSize.get(position)>= 2) {
            numberOfApplciants.setText(" " + eventApplicantsSize.get(position) + " anmodninger");
            profilePic2.setVisibility(View.VISIBLE);
            profilePic2Holder.setVisibility(View.VISIBLE);

        }

        // Sætter overskriften for eventet
        header.setText(eventHeaders.get(position));
        // Todo - Dato for event skal findes.
        date.setText("12/12-2021");

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView eventPic,profilePic1, profilePic2, box_own_events;
        public CardView profileHolder1, profilePic2Holder;
        public TextView numberOfApplciants, header, date;
        public View edit, delete;

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
            edit = itemView.findViewById(R.id.own_event_edit_picture);
            delete = itemView.findViewById(R.id.own_event_delete_picture);

            edit.setOnClickListener(this);
            delete.setOnClickListener(this);
            box_own_events.setOnClickListener(this);
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


            if(v == edit){
                //Edit post
                Intent i = new Intent(v.getContext(), Activity_Create_Event.class);
                Bundle bundle = new Bundle();

            }else if(v == delete){
                //Delete

            }
        }
    }

    @Override
    public int getItemCount() {
        return this.eventOwnerPic.size();
    }
}
