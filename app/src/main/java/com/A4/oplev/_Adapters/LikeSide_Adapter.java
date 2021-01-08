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
import androidx.cardview.widget.CardView;

import com.A4.oplev.R;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


public class  LikeSide_Adapter extends ArrayAdapter<String> {
    private Context mContext;
    private List<String> nameList = new ArrayList<>(), lastMessage = new ArrayList<>(), headerList = new ArrayList<>(), lastMessageSender = new ArrayList<>(), isInitialized = new ArrayList<>(), otherPersonPic = new ArrayList<>();
    private List<Date> dateList = new ArrayList<>();

    public LikeSide_Adapter(@NonNull Context context, @NonNull ArrayList<String> names, @NonNull ArrayList<Date> dates, @NonNull ArrayList<String> lastMessage, @NonNull ArrayList<String> headerList, @NonNull ArrayList<String> lastMessageSender, ArrayList<String> isInitialized, ArrayList<String> otherPersonPic) {
        super(context, 0 , names);
        this.mContext = context;
        this.nameList = names;
        this.dateList= dates;
        this.lastMessage = lastMessage;
        this.headerList = headerList;
        this.lastMessageSender = lastMessageSender;
        this.isInitialized = isInitialized;
        this.otherPersonPic = otherPersonPic;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            // Vi inflater det relative layout som vi har lavet i xml filen
            listItem = LayoutInflater.from(mContext).inflate(R.layout.besked_liste_element,parent,false);

        // Vi får nogle værdier som vi skal bruge til at sætte ind i layoutet
        String currentName = nameList.get(position);
        String currentDate;
        // Vi tjekker om chatten indeholder nogle beskeder hvis ikke så indsæt tomme strenge
        if (isInitialized.get(position).equals("false")) currentDate = "";
        else {
            Date now = new Date();
            long days = Math.max(now.getDay(),dateList.get(position).getDay()) - Math.min(now.getDay(),dateList.get(position).getDay());
            long months = Math.max(now.getMonth(),dateList.get(position).getMonth()) - Math.min(now.getMonth(),dateList.get(position).getMonth());
            long years = Math.max(now.getYear(),dateList.get(position).getYear()) - Math.min(now.getYear(),dateList.get(position).getYear());
            if (years > 0 || months > 0 || days > 7){
                DateFormat format1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                format1.setTimeZone(TimeZone.getTimeZone("GMT+1"));
                currentDate = format1.format(dateList.get(position));
            } else if (years == 0 && months == 0 && days == 0) currentDate = "I dag";
            else currentDate = dateList.get(position).toString().substring(0,3);
        }


        String currentLastMessage;
        if (isInitialized.get(position).equals("true")) {
            // Vi checker om den besked der sidst blev sendt er fra den man chatter med
            if (lastMessageSender.get(position).equals(currentName)) {
                if (lastMessage.get(position).equals("pictureBlaBlaBla!:"))
                    currentLastMessage = currentName + ": " + "[Picture]";
                    // Sæt stringen til dette hvis sidste besked er fra anden person
                else {
                    if (lastMessage.get(position).length() > 30){
                        currentLastMessage = currentName + ": " + lastMessage.get(position).substring(0,30-currentName.length()) + "...";
                    } else {
                        currentLastMessage = currentName + ": " + lastMessage.get(position);
                    }
                    currentLastMessage = currentLastMessage.replaceAll("\n","");
                }
            } else if (lastMessageSender.get(position).equals("Oplev")){
                if (lastMessage.get(position).length() > 30){
                    currentLastMessage = "Oplev: " + lastMessage.get(position).substring(0,30-5) + "...";
                } else {
                    currentLastMessage = "Oplev: " + lastMessage.get(position);
                }
                currentLastMessage = currentLastMessage.replaceAll("\n","");
            } else {
                if (lastMessage.get(position).equals("pictureBlaBlaBla!:"))
                    currentLastMessage = "Dig: " + "[Picture]";
                    // Sæt stringen til dette hvis sidste besked er fra en selv
                else {
                    if (lastMessage.get(position).length() > 30){
                        currentLastMessage = "Dig: " + lastMessage.get(position).substring(0,30) + "...";
                    } else {
                        currentLastMessage = "Dig: " + lastMessage.get(position);
                    }
                    currentLastMessage = currentLastMessage.replaceAll("\n","");
                }
            }
        }
        else currentLastMessage = "";

        // Indsæt nogle værdier ind layoutet (skal ændres senere hen til at hente billeder osv)
        String currentHeader = headerList.get(position);

        TextView lastMessage = (TextView) listItem.findViewById(R.id.besked_liste_lastmessage);
        lastMessage.setText(currentLastMessage);

        TextView header = (TextView) listItem.findViewById(R.id.beskeder_overskrift);
        header.setText(currentHeader);

        TextView date = (TextView) listItem.findViewById(R.id.beskeder_dato);
        date.setText(currentDate);

        TextView name = (TextView) listItem.findViewById(R.id.beskeder_name);
        name.setText("Med " + currentName);

        ImageView profilBillede = listItem.findViewById(R.id.likeside_chats_profilbillede);
        Picasso.get().load(otherPersonPic.get(position))
                .resize(mContext.getDisplay().getWidth(), mContext.getDisplay().getHeight() / 2 + 200)
                .centerCrop()
                .placeholder(R.drawable.load2)
                .error(R.drawable.question)
                .into(profilBillede);

        return listItem;
    }
}
