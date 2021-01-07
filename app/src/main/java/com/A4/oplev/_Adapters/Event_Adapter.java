package com.A4.oplev._Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.A4.oplev.Activity_Profile;
import com.A4.oplev.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

import Controller.UserController;
import DAL.Classes.EventDAO;
import DAL.Classes.UserDAO;
import DAL.Interfaces.CallbackEvent;
import DAL.Interfaces.CallbackUser;
import DAL.Interfaces.IEventDAO;
import DAL.Interfaces.IUserDAO;
import DTO.EventDTO;
import DTO.UserDTO;

public class Event_Adapter extends RecyclerView.Adapter<Event_Adapter.ViewHolder>implements View.OnClickListener {

    List<String> eventListId;
    List<EventDTO> loadedEvent;
    int offset = 0;
    IEventDAO dataA;
    int lastPos = -1;
    Context ctx;
    EventDTO eventDTO;
    boolean way = true;
    boolean first = false;
    Bitmap rightimg = null;
    Bitmap currentImg = null;
    Bitmap leftimg = null;



    public Event_Adapter(Bitmap pic,List<EventDTO> scoreList, Context frame, List<String> ids) {
        this.loadedEvent = scoreList;
        this.ctx = frame;
        this.dataA = new EventDAO();
        this.eventListId = ids;
        first = true;
        this.currentImg = pic;

    }

    public void setCurrentImg(Bitmap bmp){
        this.currentImg = bmp;
    }
    public void setRightimg(Bitmap bmp){
        this.rightimg = bmp;
    }

    public void setWay(boolean way){
        this.way = way;
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
        eventDTO = loadedEvent.get(position);
        // Set item views based on your views and data model
        ImageView profilePic = holder.profilePic;
        ImageView mainPic = holder.mainPic;
        TextView withWhoText = holder.withWhoText;
        TextView headlineText = holder.headlineText;

        // her skal dataen sættes in i holderen, der skal gøres brug af en billed controller til at håndtere billder.
        userDAO.getUser(new CallbackUser() {
            @Override
            public void onCallback(UserDTO user) {

                int width = 200;
                int height = 10;

                withWhoText.setText(user.getfName());
                headlineText.setText(eventDTO.getTitle());

                if(first) {
                    mainPic.setImageBitmap(currentImg);
                    first = false;
                }else if(way && position < loadedEvent.size()-1) {
                    Picasso.get().load(loadedEvent.get(position + 1).getEventPic()).into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            // loaded bitmap is here (bitmap)
                            mainPic.setImageBitmap(rightimg);
                            Bitmap newleftimg = currentImg;
                            Bitmap newcurrentImg = rightimg;
                            Bitmap newrightimg = bitmap;
                            leftimg = newleftimg;
                            currentImg = newcurrentImg;
                            rightimg = newrightimg;

                        }

                        @Override
                        public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {
                        }
                    });
                } else if(position > 0) {
                    Picasso.get().load(loadedEvent.get(position - 1).getEventPic()).into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            // loaded bitmap is here (bitmap)
                            mainPic.setImageBitmap(leftimg);
                            Bitmap newrightimg = currentImg;
                            Bitmap newcurrentImg = leftimg;
                            Bitmap newleftimg = bitmap;

                            leftimg = newleftimg;
                            currentImg = newcurrentImg;
                            rightimg = newrightimg;
                        }

                        @Override
                        public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {
                        }
                    });
                }


                Picasso.get().load(eventDTO.getOwnerPic())
                        .resize(width, height/2 + 200)
                        .centerCrop()
                        .placeholder(R.drawable.load2)
                        .error(R.drawable.question)
                        .transform(new RoundedTransformation(90,0))
                        .into(profilePic);

            }
        }, eventDTO.getOwnerId());
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
            profilePic.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int id =this.getLayoutPosition();

            if(view == profilePic){
                UserController user = UserController.getInstance();
                user.getUser(new CallbackUser() {
                    @Override
                    public void onCallback(UserDTO user) {
                        Intent i = new Intent(ctx, Activity_Profile.class);
                        i.putExtra("user", user);
                        i.putExtra("load", 2);
                        ctx.startActivity(i);
                    }
                }, eventDTO.getOwnerId());
            }
        }
    }

}
