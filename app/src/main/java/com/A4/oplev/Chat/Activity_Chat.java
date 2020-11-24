package com.A4.oplev.Chat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
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
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.A4.oplev._Adapters.ChatList_Adapter;
import com.A4.oplev.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;

import Controller.PictureBitmapConverter;
import DAL.Classes.ChatDAO;
import DTO.ChatDTO;


public class Activity_Chat extends AppCompatActivity  implements View.OnClickListener {
    ImageView settings, tilbage;
    TextView navn;
    ListView beskeder;
    Button sendBesked;
    ArrayList<String> beskederStrings;
    ArrayList<Bitmap> bitmaps = new ArrayList<>();
    EditText inputTekst;
    ChatDTO dto;
    ChatDAO dao = new ChatDAO();
    Context ctx;
    String person1, person2, chatDocumentPath;
    private static final int REQUEST_CAMERARESULT=201;
    private PictureBitmapConverter pictureBitmapConverter;


    public void onCreate(Bundle saveInstanceState) {
        chatDocumentPath = "60V6EddGhhZdY7pTGYRF";
        person1 = "person1";
        person2 = "person2";
        pictureBitmapConverter = PictureBitmapConverter.getInstance();
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
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    if (!inputTekst.getText().toString().equals("")) {

                        updateChatDTO(person1,person2,inputTekst.getText().toString(), null);

                        beskederStrings.add(inputTekst.getText().toString());

                        dao.updateChat(new ChatDAO.FirestoreCallback() {
                            @Override
                            public void onCallback(ChatDTO dto) {
                                if (dto.getChatId() != null){
                                    setChatDTO(dto);
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
                    if (dto.getMessages() != null) {
                        ChatDTO temp = snapshot.toObject(ChatDTO.class);
                        if (temp.getChatId() != null){
                        setChatDTO(snapshot.toObject(ChatDTO.class));
                        beskederStrings.clear();
                        beskederStrings.addAll(dto.getMessages());
                    }
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
        if (v == tilbage) {
            finish();
        } else if (v == sendBesked) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (this.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    ///method to get Images
                    pictureBitmapConverter.uploadPic(this);
                } else {
                    if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                        Toast.makeText(this, "Your Permission is needed to get access the camera", Toast.LENGTH_LONG).show();
                    }
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, REQUEST_CAMERARESULT);
                }
            } else {
                pictureBitmapConverter.uploadPic(this);
            }
        } else if (v == settings) {
            // gør noget her
        }
    }


    // bruges til at sætte chatDTO objektet gennem oncallback
    private void setChatDTO(ChatDTO dto){
        this.dto = dto;
    }

    // bruges til at opdatere ens chat objekt
    private void updateChatDTO(String newSender, String newReciever, String newMessage, Uri newPic){
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

        if (newPic != null) {
            ArrayList<Uri> tempPics = dto.getPictures();
            if (tempPics == null) tempPics = new ArrayList<>();
            tempPics.add(newPic);
            dto.setPics(tempPics);
        }
    }


    // https://stackoverflow.com/questions/38352148/get-image-from-the-gallery-and-show-in-imageview
    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                dao.uploadFile(selectedImage, new ChatDAO.FirestoreCallbackPic() {
                    @Override
                    public void onCallBackPic(Uri url) {
                        System.out.println(url);
                        updateChatDTO(person1,person2,"pictureBlaBlaBla!:",url);

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
                },chatDocumentPath);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(Activity_Chat.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(Activity_Chat.this, "You haven't picked Image",Toast.LENGTH_LONG).show();
        }
    }
}
