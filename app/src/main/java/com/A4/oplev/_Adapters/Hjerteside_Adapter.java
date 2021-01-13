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

import com.A4.oplev.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Hjerteside_Adapter extends ArrayAdapter<String> {
    private Context mContext;
    private List<String> nameList = new ArrayList<>(), ageList = new ArrayList<>(), profilePictures = new ArrayList<>(), headerList = new ArrayList<>(), placementList = new ArrayList<>(), eventPictureList = new ArrayList<>();
    private List<Integer> priceList = new ArrayList<>();
    private List<Date> timeList = new ArrayList<>();

    public Hjerteside_Adapter(@NonNull Context context, @NonNull ArrayList<String> headerList,
                              @NonNull ArrayList<String> names,
                              @NonNull ArrayList<String> profilePictures, @NonNull ArrayList<String> placementList,
                              @NonNull ArrayList<Date> timeList, @NonNull ArrayList<Integer> priceList, @NonNull ArrayList<String> eventPictureList, @NonNull ArrayList<String> ageList) {
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
            // Vi inflater layoutet som vi har lavet i xml
            listItem = LayoutInflater.from(mContext).inflate(R.layout.hjerteside_liste_element,parent,false);

        // Vi sætter nogle værdier ind i layoutet (dette er ikke færddigjort og skal ændres senere hen, så siden ser lidt tom ud lige nu)
        String currentHeader = headerList.get(position);
        String currentName = nameList.get(position);
        String currentAge = ageList.get(position);
        //String currentPlacement = placementList.get(position);
        //Date currentTime = timeList.get(position);
        //int currentPrice = priceList.get(position);


        TextView header = (TextView) listItem.findViewById(R.id.hjertside_overskrift);
        header.setText(currentHeader);

        TextView name = (TextView) listItem.findViewById(R.id.hjerteside_medhvem);
        name.setText("Med " + currentName + ", " + currentAge);

        ImageView profilBillede = (ImageView) listItem.findViewById(R.id.hjerteside_profilbillede);
        Picasso.get().load(profilePictures.get(position))
                .resize(mContext.getDisplay().getWidth(), mContext.getDisplay().getHeight() / 2 + 200)
                .centerCrop()
                .placeholder(R.drawable.load2)
                .error(R.drawable.question)
                .into(profilBillede);

        ImageView eventBillede = (ImageView) listItem.findViewById(R.id.hjerteside_billede);
        Picasso.get().load(eventPictureList.get(position))
                //.resize(mContext.getDisplay().getWidth(), mContext.getDisplay().getHeight() / 2 + 200)
                //.centerCrop()
                .placeholder(R.drawable.load2)
                .error(R.drawable.question)
                .into(eventBillede);

        return listItem;
    }
}
