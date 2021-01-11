package com.A4.oplev._Adapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.A4.oplev.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class OwnEvents_Adapter extends ArrayAdapter<String> {
    private Context mContext;
    private ArrayList<Integer> eventApplicantsSize;
    private ArrayList<String> eventHeaders, eventEventPic, eventApplicantPic, eventOwnerPic, eventFirstApplicants;

    public OwnEvents_Adapter(@NonNull Context context, @NonNull ArrayList<String> eventEventPic, @NonNull ArrayList<String> eventHeaders, @NonNull ArrayList<String> eventOwnerPic, @NonNull ArrayList<String> eventFirstApplicants, @NonNull ArrayList<String> eventApplicantPic, @NonNull ArrayList<Integer> eventApplicantsSize) {
        super(context, 0 , eventHeaders);
        this.mContext = context;
        this.eventHeaders = eventHeaders;
        this.eventApplicantsSize = eventApplicantsSize;
        this.eventApplicantPic = eventApplicantPic;
        this.eventEventPic = eventEventPic;
        this.eventOwnerPic = eventOwnerPic;
        this.eventFirstApplicants = eventFirstApplicants;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            // Vi inflater det relative layout som vi har lavet i xml filen
            listItem = LayoutInflater.from(mContext).inflate(R.layout.besked_liste_event_element,parent,false);

        ImageView eventPic = listItem.findViewById(R.id.event_picture);
        Picasso.get().load(eventEventPic.get(position))
                .resize(mContext.getDisplay().getWidth(), mContext.getDisplay().getHeight()/2 + 200)
                .centerCrop()
                .placeholder(R.drawable.load2)
                .error(R.drawable.question)
                .into(eventPic);


        ImageView profilePic1 = listItem.findViewById(R.id.beskeder_event_lilleprofilbillede1);
        if (eventOwnerPic.get(position).equals("")){
            profilePic1.setImageResource(R.drawable.question);
        }
        else {
            Picasso.get().load(eventOwnerPic.get(position))
                    .resize(mContext.getDisplay().getWidth(), mContext.getDisplay().getHeight() / 2 + 200)
                    .centerCrop()
                    .placeholder(R.drawable.load2)
                    .error(R.drawable.question)
                    .into(profilePic1);
        }

        ImageView profilePic2 = listItem.findViewById(R.id.beskeder_event_lilleprofilbillede2);
        if (eventApplicantPic.get(position).equals("")){
            profilePic2.setImageResource(R.drawable.question);
        }
        else {
            Picasso.get().load(eventApplicantPic.get(position))
                    .resize(mContext.getDisplay().getWidth(), mContext.getDisplay().getHeight() / 2 + 200)
                    .centerCrop()
                    .placeholder(R.drawable.load2)
                    .error(R.drawable.question)
                    .into(profilePic2);
        }

        // Sætter overskriften for eventet
        TextView header = listItem.findViewById(R.id.beskeder_event_overskrift);
        header.setText(eventHeaders.get(position));

        TextView numberOfAppliants = listItem.findViewById(R.id.own_event_antal_anmodning);
        numberOfAppliants.setText(eventApplicantsSize.get(position)+" anmoder(e)");

        TextView date = listItem.findViewById(R.id.own_event_beskeder_events_dato);
        date.setText("");



        return listItem;
    }

}
