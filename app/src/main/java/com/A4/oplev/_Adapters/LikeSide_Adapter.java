package com.A4.oplev._Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.A4.oplev.R;
import java.util.ArrayList;
import java.util.List;


public class LikeSide_Adapter extends ArrayAdapter<String> {
    private Context mContext;
    private List<String> nameList = new ArrayList<>(), dateList = new ArrayList<>(), lastMessage = new ArrayList<>(), headerList = new ArrayList<>(), lastMessageSender = new ArrayList<>();

    public LikeSide_Adapter(@NonNull Context context, @NonNull ArrayList<String> names, @NonNull ArrayList<String> dates, @NonNull ArrayList<String> lastMessage, @NonNull ArrayList<String> headerList, @NonNull ArrayList<String> lastMessageSender) {
        super(context, 0 , names);
        this.mContext = context;
        this.nameList = names;
        this.dateList= dates;
        this.lastMessage = lastMessage;
        this.headerList = headerList;
        this.lastMessageSender = lastMessageSender;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.besked_liste_element,parent,false);

        String currentName = nameList.get(position);
        String currentDate = dateList.get(position);
        String currentLastMessage;
        if (lastMessageSender.get(position).equals(currentName)) {
            currentLastMessage = currentName + ": " + lastMessage.get(position);
        } else {
            currentLastMessage = "Dig: " + lastMessage.get(position);
        }
        String currentHeader = headerList.get(position);

        TextView lastMessage = (TextView) listItem.findViewById(R.id.besked_liste_lastmessage);
        lastMessage.setText(currentLastMessage);

        TextView header = (TextView) listItem.findViewById(R.id.beskeder_overskrift);
        header.setText(currentHeader);

        TextView date = (TextView) listItem.findViewById(R.id.beskeder_dato);
        date.setText(currentDate);

        TextView name = (TextView) listItem.findViewById(R.id.beskeder_name);
        name.setText("Med " + currentName);

        return listItem;
    }
}
