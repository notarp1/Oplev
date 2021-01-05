package com.A4.oplev._Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.A4.oplev.PicassoFunc;
import com.A4.oplev.R;
import com.google.firebase.storage.internal.Sleeper;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import DAL.Classes.EventDAO;
import DAL.Classes.UserDAO;
import DAL.Interfaces.CallbackEvent;
import DAL.Interfaces.CallbackUser;
import DAL.Interfaces.IEventDAO;
import DAL.Interfaces.IUserDAO;
import DTO.EventDTO;
import DTO.UserDTO;

public class Event_Adapter extends RecyclerView.Adapter<Event_Adapter.ViewHolder>implements View.OnClickListener {

    List<Integer> eventListId;
    List<EventDTO> loadedEvent;
    int offset = 0;
    IEventDAO dataA;


    public Event_Adapter(List<EventDTO> scoreList) {
        this.loadedEvent = scoreList;
        this.dataA = new EventDAO();
        if(scoreList == null){
            testData();
        }else {
            loadedEvent = scoreList;
        }
    }

    public void testData(){
        // metode til oprettelse af test data, så der ikke skal bruges db adgang.
        List<EventDTO> test = new ArrayList<>();
        EventDTO data = new EventDTO();
        EventDTO data2 = new EventDTO();
        EventDTO data3 = new EventDTO();
        data.setTitle("Løbe tur i skoven").setOwnerId("1").setDescription("Løb en tur med mig");
        data2.setTitle("Spis en is").setOwnerId("2").setDescription("Is på Rungstedhavn");
        data3.setTitle("Tivoli").setOwnerId("3").setDescription("Juleudstilling i tivoli");
        loadedEvent.add(data);
        loadedEvent.add(data2);
        loadedEvent.add(data3);
    }


    public void loadData(boolean way){
        // Metode til at hente data ind i loaded listen, så der hele tiden kun er tre udfyldte EventDto'er i hukkomelsen.
        if(way){
            //Going to the right, first is dumped
            if(offset!= 0) {
                loadedEvent.remove(0);
            }
            if(offset < eventListId.size()) {
                offset++;
                add2list(offset + 1);
            }
        }else{
            if(offset != eventListId.size()-1){
                loadedEvent.remove(2);
            }
            if(offset!= 0){
                offset --;
                add2listStart(offset -1);
            }
        }
    }

    public void add2list(int pos){
        //Henter Data ind i loadEvent, i sluttningen.
       //loadedEvent.add(dataA.getEvent(eventListId.get(eventListId.get(pos))));
    }
    public void add2listStart(int pos){
        //Henter Data ind i loadEvent, i sluttningen.
        List<EventDTO> newList  = new ArrayList<>();
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
        EventDTO dto = loadedEvent.get(position);
        // Set item views based on your views and data model
        ImageView  profilePic = holder.profilePic;
        ImageView mainPic = holder.mainPic;
        TextView withWhoText = holder.withWhoText;
        TextView headlineText = holder.headlineText;




        // her skal dataen sættes in i holderen, der skal gøres brug af en billed controller til at håndtere billder.
        userDAO.getUser(new CallbackUser() {
            @Override
            public void onCallback(UserDTO user) {
                withWhoText.setText(user.getfName());
                headlineText.setText(dto.getTitle());
                headlineText.setText(dto.getDescription());
                

            }
        }, dto.getOwnerId());
    }

    public void dataCleanUp(int pos){

    }


    @Override
    public int getItemCount() {
        return loadedEvent.size();
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
