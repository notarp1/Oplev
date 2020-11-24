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

public class Hjerteside_Adapter extends ArrayAdapter<String> {
    private Context mContext;
    private List<String> nameList = new ArrayList<>(), ageList = new ArrayList<>(), profilePictures = new ArrayList<>(), headerList = new ArrayList<>(), placementList = new ArrayList<>(), timeList = new ArrayList<>(), eventPictureList = new ArrayList<>();
    private List<Integer> priceList = new ArrayList<>();

    public Hjerteside_Adapter(@NonNull Context context, @NonNull ArrayList<String> headerList,
                              @NonNull ArrayList<String> names, @NonNull ArrayList<String> ageList,
                              @NonNull ArrayList<String> profilePictures, @NonNull ArrayList<String> placementList,
                              @NonNull ArrayList<String> timeList, @NonNull ArrayList<Integer> priceList, @NonNull ArrayList<String> eventPictureList) {
        super(context, 0 , headerList);
        this.mContext = context;
        this.headerList = headerList;
        this.nameList = names;
        this.ageList= ageList;
        this.profilePictures = profilePictures;
        this.eventPictureList = eventPictureList;
        this.placementList = placementList;
        this.timeList = timeList;
        this.priceList = priceList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.hjerteside_liste_element,parent,false);

        String currentHeader = headerList.get(position);
        String currentName = nameList.get(position);
        String currentAge = ageList.get(position);
        String profilePic = profilePictures.get(position);
        String currentEventPic = eventPictureList.get(position);
        String currentPlacement = placementList.get(position);
        String currentTime = timeList.get(position);
        int currentPrice = priceList.get(position);


        TextView header = (TextView) listItem.findViewById(R.id.hjertside_overskrift);
        header.setText(currentHeader);

        TextView name = (TextView) listItem.findViewById(R.id.hjerteside_medhvem);
        name.setText("Med " + currentName + ", " + currentAge);


        return listItem;
    }
}
