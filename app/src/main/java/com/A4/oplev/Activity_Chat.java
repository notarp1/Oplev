package com.A4.oplev;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.A4.oplev.adapters.ChatList_Adapter;
import com.A4.oplev.listeners.OnSwipeTouchListener;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

import DAL.Classes.ChatDAO;
import DTO.ChatDTO;

public class Activity_Chat extends AppCompatActivity  implements View.OnClickListener {
    ImageView settings, tilbage;
    TextView navn;
    ListView beskeder;
    Button sendBesked;
    ArrayList<String> beskederStrings;
    TextInputLayout inputTekst;
    ChatDTO dto;
    ChatDAO dao = new ChatDAO();


    public void onCreate(Bundle saveInstanceState) {
        dto = dao.getChat("huvc67lCMUyhHXcBksHg");
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_chat_funktion);

        settings = findViewById(R.id.chat_settings);
        tilbage = findViewById(R.id.chat_topbar_arrow);


        Intent intent = getIntent();
        navn = findViewById(R.id.chat_topbar_text);
        navn.setText(intent.getStringExtra("navn"));

        sendBesked = findViewById(R.id.chat_indsendBesked);
        beskeder = findViewById(R.id.chat_beskedList);

        inputTekst = findViewById(R.id.chat_inputBesked);

        beskederStrings = new ArrayList<>();
        if (!(dto.getMessages() == null)) {
            beskederStrings = dto.getMessages();
        }
        else{
            beskederStrings = new ArrayList<>();
            beskederStrings.add("Default1");
            beskederStrings.add("Default2");
            beskederStrings.add("Default3");
        }



        if (!beskederStrings.isEmpty()) {
            ChatList_Adapter adapter = new ChatList_Adapter(this,beskederStrings);
            beskeder.setAdapter(adapter);
        }

        sendBesked.setOnClickListener(this);
        tilbage.setOnClickListener(this);
        settings.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v == tilbage){
            finish();
        }
        else if (v == sendBesked) {
            assert inputTekst != null;
            if (!inputTekst.getEditText().getText().toString().equals("")) {
                beskederStrings.add(inputTekst.getEditText().getText().toString());
                ChatList_Adapter adapter = new ChatList_Adapter(this, beskederStrings);
                beskeder.setAdapter(adapter);
                inputTekst.getEditText().setText("");
            }
        }
        else if (v == settings){
            // gør noget her
        }
    }
}
