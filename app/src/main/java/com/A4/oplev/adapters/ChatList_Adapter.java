package com.A4.oplev.adapters;

import android.content.Context;
import android.view.Gravity;
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

import DTO.ChatDTO;


/**
 * inspiration from here
 * https://medium.com/mindorks/custom-array-adapters-made-easy-b6c4930560dd
 */

public class ChatList_Adapter extends ArrayAdapter<String> {
    private Context mContext;
    private List<String> beskederList = new ArrayList<>();
    private ChatDTO dto;
    private String thisUser;

    public ChatList_Adapter(@NonNull Context context, @NonNull ArrayList<String> list, ChatDTO dto, String thisUser) {
        super(context, 0 , list);
        mContext = context;
        beskederList = list;
        this.dto = dto;
        this.thisUser = thisUser;
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

        // instead of doing every other then check the sender of the message and place correct
        if (!dto.getSender().get(position).equals(thisUser)){
            name.setGravity(Gravity.RIGHT);
        }

        return listItem;
    }

}
