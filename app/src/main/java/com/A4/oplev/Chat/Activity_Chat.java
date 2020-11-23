package com.A4.oplev.Chat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Base64;
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
import androidx.core.content.FileProvider;

import com.A4.oplev.BuildConfig;
import com.A4.oplev._Adapters.ChatList_Adapter;
import com.A4.oplev.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
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
    ArrayList<Bitmap> bitmaps = new ArrayList<>();
    EditText inputTekst;
    ChatDTO dto;
    ChatDAO dao = new ChatDAO();
    Context ctx;
    String person1, person2, chatDocumentPath;
    private static final int REQUEST_CAMERARESULT=201;
    private static int RESULT_LOAD_IMAGE = 1;


    public void onCreate(Bundle saveInstanceState) {
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
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    if (!inputTekst.getText().toString().equals("")) {

                        updateChatDTO(person1,person2,inputTekst.getText().toString());

                        beskederStrings.add(inputTekst.getText().toString());

                        dao.updateChat(new ChatDAO.FirestoreCallback() {
                            @Override
                            public void onCallback(ChatDTO dto) {
                                if (dto.getChatId() != null){
                                    System.out.println("Update");
                                    setChatDTO(dto);
                                    ChatList_Adapter adapter = new ChatList_Adapter(ctx, beskederStrings, dto, person1, new ArrayList<>());
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
                System.out.println("Read");
                setChatDTO(dto);
                if (dto.getMessages() != null){
                    beskederStrings.clear();
                    beskederStrings.addAll(dto.getMessages());
                }

                ChatList_Adapter adapter = new ChatList_Adapter(ctx,beskederStrings, dto,person1, new ArrayList<>());
                beskeder.setAdapter(adapter);
                System.out.println("Hejsa 11" + dto.toString());
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
                    System.out.println("EventListener");
                    if (dto.getMessages() != null) {
                        ChatDTO temp = snapshot.toObject(ChatDTO.class);
                        if (temp.getChatId() != null){
                        setChatDTO(snapshot.toObject(ChatDTO.class));
                        beskederStrings.clear();
                        beskederStrings.addAll(dto.getMessages());
                    }
                    }
                    ChatList_Adapter adapter = new ChatList_Adapter(ctx,beskederStrings, dto,person1, new ArrayList<>());
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
                    takePic();
                } else {
                    if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                        Toast.makeText(this, "Your Permission is needed to get access the camera", Toast.LENGTH_LONG).show();
                    }
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, REQUEST_CAMERARESULT);
                }
            } else {
                takePic();
            }
        } else if (v == settings) {
            // gør noget her
        }
    }


    // bruges til at sætte chatDTO objektet gennem oncallback
    private void setChatDTO(ChatDTO dto){
        this.dto = dto;
        System.out.println("Hejsa blabla" + this.dto);
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


    public void takePic(){
        //Intent imageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File imagesFolder = new File(Environment.getExternalStorageDirectory(), "MyImages");
        imagesFolder.mkdirs(); // <----
        File image = new File(imagesFolder, "image_001.jpg");
        Uri uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider",image);
        //imageIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        //imageIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        //startActivityForResult(imageIntent,0);

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, RESULT_LOAD_IMAGE);

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

                updateChatDTO(person1,person2,"pictureBlaBlaBla!:" + BitMapToString(selectedImage));

                dao.updateChat(new ChatDAO.FirestoreCallback() {
                    @Override
                    public void onCallback(ChatDTO dto) {
                        if (dto.getChatId() != null){
                            ChatList_Adapter adapter = new ChatList_Adapter(ctx, beskederStrings, dto, person1,bitmaps);
                            beskeder.setAdapter(adapter);
                            inputTekst.setText("");
                        }
                    }
                }, this.dto);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(Activity_Chat.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(Activity_Chat.this, "You haven't picked Image",Toast.LENGTH_LONG).show();
        }
    }

    public String BitMapToString(Bitmap bitmap){

        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

}
