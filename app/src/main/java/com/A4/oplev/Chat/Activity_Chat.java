package com.A4.oplev.Chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.A4.oplev._Adapters.ChatList_Adapter;
import com.A4.oplev.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.Date;

import DAL.Classes.ChatDAO;
import DTO.ChatDTO;

public class Activity_Chat extends AppCompatActivity  implements View.OnClickListener {
    ImageView settings, tilbage;
    TextView navn;
    ListView beskeder;
    Button sendBesked;
    ArrayList<String> beskederStrings;
    EditText inputTekst;
    ChatDTO dto;
    ChatDAO dao = new ChatDAO();
    Context ctx;
    String person1, person2, chatDocumentPath;


    public void onCreate(Bundle saveInstanceState) {
        //dto = dao.getChat("huvc67lCMUyhHXcBksHg");
        chatDocumentPath = "60V6EddGhhZdY7pTGYRF";
        person1 = "person1";
        person2 = "person2";
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_chat_funktion);

        ctx = this;
        Intent intent = getIntent();

        settings = findViewById(R.id.chat_settings);
        tilbage = findViewById(R.id.chat_topbar_arrow);
        navn = findViewById(R.id.chat_topbar_text);
        navn.setText(intent.getStringExtra("navn"));
        sendBesked = findViewById(R.id.chat_indsendBesked);
        beskeder = findViewById(R.id.chat_beskedList);
        inputTekst = findViewById(R.id.chat_inputBesked2);

        inputTekst.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                System.out.println(event + "\t" + actionId);
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    if (!inputTekst.getText().toString().equals("")) {

                        updateChatDTO(person1,person2,inputTekst.getText().toString());

                        beskederStrings.add(inputTekst.getText().toString());

                        dao.updateChat(new ChatDAO.FirestoreCallback() {
                            @Override
                            public void onCallback(ChatDTO dto) {
                                if (dto.getChatId() != null){
                                    ChatList_Adapter adapter = new ChatList_Adapter(ctx, beskederStrings, dto, person1);
                                    beskeder.setAdapter(adapter);
                                    inputTekst.setText("");
                                }
                            }
                        }, dto);
                    }
                    handled = true;
                }
                return handled;
            }
        });

        beskederStrings = new ArrayList<>();
        dao.readChat(new ChatDAO.FirestoreCallback() {
            @Override
            public void onCallback(ChatDTO dto) {
                setChatDTO(dto);
                if (dto.getMessages() != null){
                    beskederStrings.clear();
                    beskederStrings.addAll(dto.getMessages());
                }

                ChatList_Adapter adapter = new ChatList_Adapter(ctx,beskederStrings, dto,person1);
                beskeder.setAdapter(adapter);
            }
        },chatDocumentPath);

        SystemClock.sleep(1000);

        sendBesked.setOnClickListener(this);
        tilbage.setOnClickListener(this);
        settings.setOnClickListener(this);

        // til at opdatere listen af beskeder
        FirebaseFirestore.getInstance().collection("chats").document(chatDocumentPath)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    private static final String TAG = "chat fra telefon";

                    @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    setChatDTO(snapshot.toObject(ChatDTO.class));
                    if (dto.getMessages() != null) {
                        beskederStrings.clear();
                        beskederStrings.addAll(dto.getMessages());
                    }
                    ChatList_Adapter adapter = new ChatList_Adapter(ctx,beskederStrings, dto,person1);
                    beskeder.setAdapter(adapter);
                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        if (v == tilbage){
            finish();
        }
        else if (v == sendBesked) {
            assert inputTekst != null;
            if (!inputTekst.getText().toString().equals("")) {

                updateChatDTO(person1,person2,inputTekst.getText().toString());

                beskederStrings.add(inputTekst.getText().toString());

                dao.updateChat(new ChatDAO.FirestoreCallback() {
                    @Override
                    public void onCallback(ChatDTO dto) {
                        if (dto.getChatId() != null){
                            ChatList_Adapter adapter = new ChatList_Adapter(ctx, beskederStrings, dto, person1);
                            beskeder.setAdapter(adapter);
                            inputTekst.setText("");
                        }
                    }
                }, dto);
            }
        }
        else if (v == settings){
            // gør noget her
        }
    }


    // bruges til at sætte chatDTO objektet gennem oncallback
    private void setChatDTO(ChatDTO dto){
        this.dto = dto;
    }

    // bruges til at opdatere ens chat objekt
    private void updateChatDTO(String newSender, String newReciever, String newMessage){
        ArrayList<String> tempSender = dto.getSender();
        if (tempSender == null) tempSender = new ArrayList<>();
        tempSender.add(newSender);
        dto.setSender(tempSender);

        ArrayList<String> tempReciever = dto.getReceiver();
        if (tempReciever == null) tempReciever = new ArrayList<>();
        tempReciever.add(newReciever);
        dto.setReceiver(tempReciever);

        ArrayList<String> tempMessage = dto.getMessages();
        if (tempMessage == null) tempMessage = new ArrayList<>();
        tempMessage.add(newMessage);
        dto.setMessages(tempMessage);

        ArrayList<Date> tempDate = dto.getDates();
        if (tempDate == null) tempDate = new ArrayList<>();
        tempDate.add(new Date());
        dto.setDates(tempDate);
    }
}
