package com.A4.oplev._Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.A4.oplev.PicassoFunc;
import com.A4.oplev.R;

import java.util.ArrayList;
import java.util.Date;

import DTO.UserDTO;

public class LikeSide_Event_Adapter extends ArrayAdapter<String> {
    private Context mContext;
    private ArrayList<String> headerList, numberOfAppliantsList;
    private ArrayList<Date> dateList;
    private ArrayList<UserDTO> thisUserList, otherUserList;
    private PicassoFunc picassoFunc;


    public LikeSide_Event_Adapter(@NonNull Context context, @NonNull ArrayList<String> headerList, @NonNull ArrayList<String> numberOfAppliantsList, @NonNull ArrayList<Date> dateList, @NonNull ArrayList<UserDTO> thisUserList, @NonNull ArrayList<UserDTO> otherUserList) {
        super(context, 0 , headerList);
        this.mContext = context;
        this.headerList = headerList;
        this.numberOfAppliantsList = numberOfAppliantsList;
        this.dateList = dateList;
        this.thisUserList = thisUserList;
        this.otherUserList = otherUserList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            // Vi inflater det relative layout som vi har lavet i xml filen
            listItem = LayoutInflater.from(mContext).inflate(R.layout.besked_liste_event_element,parent,false);

        ImageView eventPic = listItem.findViewById(R.id.beskeder_event_eventbillede);
        ImageView profilePic1 = listItem.findViewById(R.id.beskeder_event_lilleprofilbillede1);
        ImageView profilePic2 = listItem.findViewById(R.id.beskeder_event_lilleprofilbillede2);

        // Sætter overskriften for eventet
        TextView header = listItem.findViewById(R.id.beskeder_event_overskrift);
        header.setText(headerList.get(position));

        TextView numberOfAppliants = listItem.findViewById(R.id.beskeder_event_name);
        numberOfAppliants.setText(numberOfAppliantsList.get(position));

        TextView date = listItem.findViewById(R.id.beskeder_event_dato);
        date.setText(dateList.get(position).toString().substring(0,3));



        return listItem;
    }
}
