package com.A4.oplev._Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.util.Base64;
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
    private List<Bitmap> pictures = new ArrayList<>();
    private int pictureCount = 0;

    public ChatList_Adapter(@NonNull Context context, @NonNull ArrayList<String> list, ChatDTO dto, String thisUser, ArrayList<Bitmap> pictures) {
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
        boolean isPic = false;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.chat_besked_element,parent,false);

        String[] currentBesked = beskederList.get(position).split("!:");
        TextView besked = (TextView) listItem.findViewById(R.id.chat_besked_element_tekst);

        if (currentBesked[0].equals("pictureBlaBlaBla")) {
            SpannableStringBuilder ssb = new SpannableStringBuilder();
            ssb.append(" ");
            int start = ssb.length()-1;
            if (start < 0) {
                start = 0;
            }
            Drawable drawable = new BitmapDrawable(mContext.getResources(), StringToBitMap(currentBesked[1]));
            drawable.setBounds(0, 0, mContext.getResources().getDisplayMetrics().widthPixels/2, mContext.getResources().getDisplayMetrics().heightPixels/2);
            ImageSpan span = new ImageSpan(drawable);
            ssb.setSpan(span, start, ssb.length(), 0);
            besked.setText(ssb);
            besked.setBackgroundColor(mContext.getResources().getColor(R.color.backgroundColor));
            isPic = true;
        } else {
            currentBesked[0] = beskederList.get(position);
            besked.setText(currentBesked[0]);
        }



        if (!dto.getSender().get(position).equals(thisUser)){
            int width = mContext.getResources().getDisplayMetrics().widthPixels;
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(width/4, 0, width/50, 0);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                besked.setLayoutParams(new RelativeLayout.LayoutParams(lp));
                if (!isPic) {
                    besked.setBackgroundColor(mContext.getResources().getColor(R.color.chatColorGrey));
                    besked.setTextColor(mContext.getResources().getColor(R.color.black));
                }
            }
        }
        else{
            int width = mContext.getResources().getDisplayMetrics().widthPixels;
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(width/50, 0, width/4, 0);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                besked.setLayoutParams(new RelativeLayout.LayoutParams(lp));
                if (!isPic) {
                    besked.setBackgroundColor(mContext.getResources().getColor(R.color.chatColorBlue));
                    besked.setTextColor(mContext.getResources().getColor(R.color.white));
                }
            }
        }

        return listItem;
    }

    public Bitmap StringToBitMap(String encodedString){
        try{
            byte [] encodeByte = Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }
        catch(Exception e){
            e.getMessage();
            return null;
        }
    }

}
