package com.A4.oplev;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class Activity_Chat extends AppCompatActivity  implements View.OnClickListener {
    ImageView settings, tilbage;
    TextView navn;
    ListView beskeder;
    Button sendBesked;
    ArrayList<String> beskederStrings;
    TextInputLayout inputTekst;


    public void onCreate(Bundle saveInstanceState) {
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
        beskederStrings.add("Hejsa");
        beskederStrings.add("hej med dig");



        if (!beskederStrings.isEmpty()) {
            ChatList_Adapter adapter = new ChatList_Adapter(this,beskederStrings);
            //ArrayAdapter adapter = new ArrayAdapter(this, R.layout.chat_besked_element, R.id.chat_besked_element_tekst, beskederStrings);
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
        else if (v == sendBesked){
            assert inputTekst.getEditText() != null;
            beskederStrings.add(inputTekst.getEditText().getText().toString());
            ChatList_Adapter adapter = new ChatList_Adapter(this,beskederStrings);
            //ArrayAdapter adapter = new ArrayAdapter(this, R.layout.chat_besked_element, R.id.chat_besked_element_tekst, beskederStrings);
            beskeder.setAdapter(adapter);
            inputTekst.getEditText().setText("");
        }
        else if (v == settings){
            // gør noget her
        }
    }
}
