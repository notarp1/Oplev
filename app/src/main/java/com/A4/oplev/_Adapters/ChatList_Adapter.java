package com.A4.oplev._Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.A4.oplev.R;

import java.io.ByteArrayOutputStream;
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
    private List<Intent> pictures = new ArrayList<>();
    private int pictureCount = 0;

    public ChatList_Adapter(@NonNull Context context, @NonNull ArrayList<String> list, ChatDTO dto, String thisUser, ArrayList<Intent> pictures) {
        super(context, 0 , list);
        this.mContext = context;
        this.beskederList = list;
        this.dto = dto;
        this.thisUser = thisUser;
        this.pictures = pictures;
    }

    @SuppressLint("ResourceAsColor")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.chat_besked_element,parent,false);

        String currentBesked;
        TextView besked = (TextView) listItem.findViewById(R.id.chat_besked_element_tekst);

        if (beskederList.get(position).equals("pictureBlaBlaBla")) {
            SpannableStringBuilder ssb = new SpannableStringBuilder();
            int start = ssb.length()-1;
            if (start < 0) {
                start = 0;
            }
            ssb.setSpan(new ImageSpan(mContext, R.drawable._profiltest), start, ssb.length(), 0);
            besked.setText(ssb);
        } else {
            currentBesked = beskederList.get(position);
            besked.setText(currentBesked);
        }



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
