package com.A4.oplev;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ChatList_Adapter extends ArrayAdapter<String> {
    private Context mContext;
    private List<String> beskederList = new ArrayList<>();


    public ChatList_Adapter(@NonNull Context context, @NonNull ArrayList<String> list) {
        super(context, 0 , list);
        mContext = context;
        beskederList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.chat_besked_element,parent,false);

        String currentBesked = beskederList.get(position);

        TextView name = (TextView) listItem.findViewById(R.id.chat_besked_element_tekst);
        name.setText(currentBesked);

        if (position % 2 == 1){
            name.setGravity(Gravity.RIGHT);
        }

        return listItem;
    }

}
