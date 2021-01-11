package com.A4.oplev._Adapters;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;

import com.A4.oplev.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class OwnEvents_Adapter extends ArrayAdapter<String> {
    private Context mContext;
    private ArrayList<Integer> eventApplicantsSize;
    private ArrayList<String> eventHeaders, eventEventPic, eventApplicantPic, eventOwnerPic, eventFirstApplicants;

    public OwnEvents_Adapter(@NonNull Context context, @NonNull ArrayList<String> eventEventPic, @NonNull ArrayList<String> eventHeaders, @NonNull ArrayList<String> eventOwnerPic, @NonNull ArrayList<String> eventFirstApplicants, @NonNull ArrayList<String> eventApplicantPic, @NonNull ArrayList<Integer> eventApplicantsSize) {
        super(context, 0, eventHeaders);
        this.mContext = context;
        this.eventHeaders = eventHeaders;
        this.eventApplicantsSize = eventApplicantsSize;
        this.eventApplicantPic = eventApplicantPic;
        this.eventEventPic = eventEventPic;
        this.eventOwnerPic = eventOwnerPic;
        this.eventFirstApplicants = eventFirstApplicants;
        Log.d("eventSize test6.1", "Goddag");
    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }

    public View getView(int position, @Nullable View convertView, ViewGroup parent) {

        Log.d("Counter", getViewTypeCount() + "" + eventApplicantsSize.size() + position);

        Log.d("eventSize test6.2", "Goddag");
        View listItem = convertView;
        if (listItem == null) {
            Log.d("eventSize test6.3", "Goddag");
            // Vi inflater det relative layout som vi har lavet i xml filen
            listItem = LayoutInflater.from(mContext).inflate(R.layout.u_settings_event_element, parent, false);
        }

        ImageView eventPic = listItem.findViewById(R.id.own_event_picture);
        Picasso.get().load(eventEventPic.get(position))
                .resize(mContext.getDisplay().getWidth(), mContext.getDisplay().getHeight() / 2 + 200)
                .centerCrop()
                .placeholder(R.drawable.load2)
                .error(R.drawable.question)
                .into(eventPic);


        //log.d("eventSize test4", eventApplicantsSize.toString());
        ImageView profilePic1 = listItem.findViewById(R.id.own_event_anmodning1_pic);
        CardView profileHolder1 =  listItem.findViewById(R.id.own_event_anmodning1_holder);
        TextView numberOfApplciants = listItem.findViewById(R.id.own_event_antal_anmodning);

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

        ImageView profilePic2 = listItem.findViewById(R.id.own_event_anmodning2_pic);
        CardView profilePic2Holder = (CardView) listItem.findViewById(R.id.own_event_anmodning2_holder);


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
            TextView header = listItem.findViewById(R.id.own_event_headline);
            header.setText(eventHeaders.get(position));

            TextView date = listItem.findViewById(R.id.own_event_beskeder_events_dato);
            date.setText("12/12-2021");

            return listItem;
        }

    }
