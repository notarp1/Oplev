package com.A4.oplev._Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
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

import com.A4.oplev.R;

import java.io.IOException;
import java.net.URL;
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
    private int pictureCount;

    public ChatList_Adapter(@NonNull Context context, @NonNull ArrayList<String> list, ChatDTO dto, String thisUser) {
        super(context, 0 , list);
        this.mContext = context;
        this.beskederList = list;
        this.dto = dto;
        this.thisUser = thisUser;
        this.pictureCount = 0;
    }

    @SuppressLint("ResourceAsColor")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        boolean isPic = false;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.chat_besked_element,parent,false);

        String currentBesked = beskederList.get(position);
        TextView besked = (TextView) listItem.findViewById(R.id.chat_besked_element_tekst);

        if (currentBesked.equals("pictureBlaBlaBla!:")) {
            SpannableStringBuilder ssb = new SpannableStringBuilder();
            ssb.append(" ");


            final Bitmap[] pictureBitMap = {null};
            uriToBitMap(dto.getPictures().get(pictureCount), new BitMapCallback() {
                @Override
                public void onCallBack(Bitmap bitmap) {
                    pictureBitMap[0] = bitmap;
                }
            });

            if (pictureBitMap[0] != null) {
                Drawable drawable = new BitmapDrawable(mContext.getResources(), pictureBitMap[0]);
                drawable.setBounds(0, 0, mContext.getResources().getDisplayMetrics().widthPixels / 2, mContext.getResources().getDisplayMetrics().heightPixels / 2);
                ImageSpan span = new ImageSpan(drawable);
                ssb.setSpan(span, ssb.length() - 1, ssb.length(), 0);
                besked.setText(ssb);
                besked.setBackgroundColor(mContext.getResources().getColor(R.color.backgroundColor));
                isPic = true;
            }
        } else {
            currentBesked = beskederList.get(position);
            besked.setText(currentBesked);
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


    public void uriToBitMap(Uri url, BitMapCallback bitMapCallback){
        final Bitmap[] bitmap = {null};
        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    bitmap[0] = BitmapFactory.decodeStream(new URL(url.toString()).openConnection().getInputStream());
                    bitMapCallback.onCallBack(bitmap[0]);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(r).start();
    }


    private interface BitMapCallback{
        void onCallBack(Bitmap bitmap);
    }
}
