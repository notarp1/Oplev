package com.A4.oplev._Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
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

    @SuppressLint("ResourceAsColor")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.chat_besked_element,parent,false);

        String currentBesked = beskederList.get(position);

        TextView besked = (TextView) listItem.findViewById(R.id.chat_besked_element_tekst);
        besked.setText(currentBesked);

        if (!dto.getSender().get(position).equals(thisUser)){
            int width = getContext().getResources().getDisplayMetrics().widthPixels;
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(width/4, 0, width/50, 0);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                besked.setLayoutParams(new RelativeLayout.LayoutParams(lp));
                besked.setBackgroundColor(getContext().getResources().getColor(R.color.chatColorGrey));
                besked.setTextColor(getContext().getResources().getColor(R.color.black));
            }
        }
        else{
            int width = getContext().getResources().getDisplayMetrics().widthPixels;
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(width/50, 0, width/4, 0);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                besked.setLayoutParams(new RelativeLayout.LayoutParams(lp));
                besked.setBackgroundColor(getContext().getResources().getColor(R.color.chatColorBlue));
                besked.setTextColor(getContext().getResources().getColor(R.color.white));

            }
        }

        return listItem;
    }

}
