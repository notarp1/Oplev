package com.A4.oplev._Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.A4.oplev.R;

import java.util.List;

import DTO.EventDTO;

public class Event_Adapter extends RecyclerView.Adapter<Event_Adapter.ViewHolder>implements View.OnClickListener {

    List<EventDTO> eventList;

    public Event_Adapter(List<EventDTO> scoreList) {
        this.eventList = scoreList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.eventlist_item, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the data model based on position
        EventDTO dto = eventList.get(position);
        // Set item views based on your views and data model
        ImageView  profilePic = holder.profilePic;
        ImageView mainPic = holder.mainPic;
        TextView withWhoText = holder.withWhoText;
        TextView headlineText = holder.headlineText;

        withWhoText.setText(String.valueOf(dto.getOwner()));

        headlineText.setText(dto.getName());
    }



    @Override
    public int getItemCount() {
        return eventList.size();
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


        }


        @Override
        public void onClick(View view) {
            int id =this.getLayoutPosition();

        }
    }

}
