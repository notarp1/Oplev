package com.A4.oplev._Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.SystemClock;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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

    public ChatList_Adapter(@NonNull Context context, @NonNull ArrayList<String> list, ChatDTO dto, String thisUser) {
        super(context, 0 , list);
        this.mContext = context;
        this.beskederList = list;
        this.dto = dto;
        this.thisUser = thisUser;
    }

    // Den her funktion vil lave vores listview for chatsne
    @SuppressLint("ResourceAsColor")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        System.out.println(position);
        System.out.println("Picturecount: " + getPictureCount(position)+1);
        View listItem = convertView;
        boolean isPic = false;
        if(listItem == null)
            // Vi inflater det relative layout som vi har lavet i xml
            listItem = LayoutInflater.from(mContext).inflate(R.layout.chat_besked_element,parent,false);

        // Vi finder vores textview og finder ud af hvad chatbeskeden er
        String currentBesked = beskederList.get(position);
        TextView besked = (TextView) listItem.findViewById(R.id.chat_besked_element_tekst);

        // hvis chatbeskeden er et billede (igen meget ustabilt lavet og skal ændres)
        if (currentBesked.equals("pictureBlaBlaBla!:")) {
            // For at kunne indsætte et billede i et textview så bruger vi spannable string
            SpannableStringBuilder ssb = new SpannableStringBuilder();
            // Billedet kunne ikke indsættet uden den her tomme streng af en eller anden grund
            ssb.append(" ");

            // Det her er en metode som skal hente et billede fra firestore storage og siden den nye måde at hente et billede på med URI så skal vi køre det asynkront med et callback
            uriToBitMap(dto.getPictures().get(getPictureCount(position)), new BitMapCallback() {
                @Override
                public void onCallBack(Bitmap bitmap) {
                    // Vi gør os sikre på at billedet er blevet læst ind ellers vil programmet crashe
                    if (bitmap != null) {
                        // Vi kreerer et drawable fra det bitmap vi lige har fået fra firestore
                        Drawable drawable = new BitmapDrawable(mContext.getResources(), bitmap);
                        // Vi sætter dens dimensioner med denne funktion som nok skal ændres lidt
                        drawable.setBounds(0, 0, mContext.getResources().getDisplayMetrics().widthPixels / 2, mContext.getResources().getDisplayMetrics().heightPixels / 2);
                        // Vi laver et imagespan ud af det som kan placeres i vores spannable string
                        ImageSpan span = new ImageSpan(drawable);
                        ssb.setSpan(span, ssb.length() - 1, ssb.length(), 0);
                        // Nu sætter vi vores billede ind i tekstfeltet
                        besked.setText(ssb);
                        // Sætter baggrunden til den default vi har inde i aktiviteten
                        besked.setBackgroundColor(mContext.getResources().getColor(R.color.backgroundColor));
                    } else {
                        // Hvis bitmappet er tomt så bare send en fejlbesked
                        Toast.makeText(mContext,"Error whilst loading picture", Toast.LENGTH_LONG).show();
                    }
                }
            });
            isPic = true;
        } else {
            // Hvis beskeden ikke er et billede så sætter vi bare teksten til det beskeden er
            besked.setText(currentBesked);
        }


        // Vi tjekker om brugeren der har sendt beskeden er den anden person
        if (!dto.getSender().get(position).equals(thisUser)){
            // Vi finder skærmstørrelsen af telefonen så vi kan sætte layoutet ens for alle telefoner
            int width = mContext.getResources().getDisplayMetrics().widthPixels;
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            // Sætter nogle marginer alt efter bredden af skærmen
            lp.setMargins(width/4, 0, width/50, width/50);
            // Kun SDK 19 eller efter kan gøre dette her derfor checker vi om det er sandt
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                besked.setLayoutParams(new RelativeLayout.LayoutParams(lp));
                // Hvis beskeden ikke var et billede skal vi sætte baggrundens farve og tekstens farve til noget nyt
                if (!isPic) {
                    besked.setBackgroundColor(mContext.getResources().getColor(R.color.chatColorGrey));
                    besked.setTextColor(mContext.getResources().getColor(R.color.black));
                }
                else besked.setGravity(Gravity.RIGHT);
            }
        }
        else{
            // Helt det samme gøres for hvis beskeden er sendt fra en selv bare med andre farver og en anden margine på textviewet
            int width = mContext.getResources().getDisplayMetrics().widthPixels;
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(width/50, 0, width/4, width/50);
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


    // Den her funktion tager en URI som den skal hente et billede fra og returnerer bitmappen af billedet
    public void uriToBitMap(Uri url, BitMapCallback bitMapCallback){
        final Bitmap[] bitmap = {null};
        Runnable r = () -> {
            try {
                bitmap[0] = BitmapFactory.decodeStream(new URL(url.toString()).openConnection().getInputStream());
                bitMapCallback.onCallBack(bitmap[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
        new Thread(r).start();
        while (bitmap[0] == null){
            SystemClock.sleep(1);
        }
    }


    // Interface til callback når man skal hente billeder
    private interface BitMapCallback{
        void onCallBack(Bitmap bitmap);
    }

    // Finder antallet af billeder der har været før den position man er på nu
    private int getPictureCount(int position){
        int pics = 0;
        for (int i = 0; i <= position; i++) {
            if (beskederList.get(i).equals("pictureBlaBlaBla!:")){
                pics++;
            }
        }
        return Math.max(pics-1,0);
    }
}
