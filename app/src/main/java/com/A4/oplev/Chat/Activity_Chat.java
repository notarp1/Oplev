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
import android.widget.LinearLayout;
import android.widget.ScrollView;
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

import Controller.PictureMaker;
import DAL.Classes.ChatDAO;
import DTO.ChatDTO;


public class Activity_Chat extends AppCompatActivity  implements View.OnClickListener {
    private ImageView settings, tilbage;
    private TextView navn;
    private ScrollView beskeder;
    private LinearLayout linearLayout;
    private Button sendBillede;
    private ArrayList<String> beskederStrings = new ArrayList<>();
    private ArrayList<Bitmap> bitmaps = new ArrayList<>();
    private EditText inputTekst;
    private ChatDTO dto;
    private ChatDAO dao = new ChatDAO();
    private Context ctx;
    private String person1, person2, chatDocumentPath;
    private static final int REQUEST_CAMERARESULT=201;
    private PictureMaker pictureMaker;


    public void onCreate(Bundle saveInstanceState) {
        pictureMaker = PictureMaker.getInstance();

        // Vi instantierer layoutet
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_chat_funktion);
        ctx = this;

        // her får vi intentet med navnet på den man skriver med
        Intent intent = getIntent();
        chatDocumentPath = intent.getStringExtra("chatId");
        person1 = intent.getStringExtra("currentUser");
        person2 = intent.getStringExtra("otherUser");

        // finder elementerne i layoutet
        settings = findViewById(R.id.chat_settings);
        tilbage = findViewById(R.id.chat_topbar_arrow);
        navn = findViewById(R.id.chat_topbar_text);
        navn.setText(person2);
        sendBillede = findViewById(R.id.chat_indsendBesked);
        beskeder = findViewById(R.id.chat_beskedList);
        inputTekst = findViewById(R.id.chat_inputBesked2);
        linearLayout = findViewById(R.id.chat_besked_linearlayout);


        // For at kunne sende besked med enter på IME tastaturet så bruger vi den her funktion
        inputTekst.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                // Checker hvis man har trykket GO (enter)
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    // Vi gider kun sende teksten hvis den ikke er tom
                    if (!inputTekst.getText().toString().equals("")) {

                        // Selvlavet funktion til at opdatere attributterne i vores ChatDTO objekt
                        updateChatDTO(person1, person2, inputTekst.getText().toString(), null);
                        // vi adder beskeden til vores liste af beskeder
                        beskederStrings.add(inputTekst.getText().toString());

                        // Nu når vi har sendt en besked vil vi gerne opdatere den i firestore
                        dao.updateChat(new ChatDAO.FirestoreCallback() {
                            @Override
                            // Der skal ventes på et callback for at få det nye objekt fra databasen
                            public void onCallback(ChatDTO dto) {
                                if (dto.getChatId() != null) {
                                    // Sætter klassens objekt til at være det nye (kan åbenbart ikke gøres inde fra funktionen)
                                    setChatDTO(dto);
                                    // Selvlavet adapter der tager listen af beskeder, Chat-objektet og den nuværende brugers navn som input og laver listviewet over chatten
                                    ChatList_Adapter adapter = new ChatList_Adapter(ctx, beskederStrings, dto, person1);
                                    View item = adapter.getView(beskederStrings.size()-1, null, null);
                                    linearLayout.addView(item);
                                    // clear tekstboksen
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

        // Her indlæser vi en chat fra firestore af
        dao.readChat(new ChatDAO.FirestoreCallback() {
            @Override
            // Venter igen på callback fra metoden
            public void onCallback(ChatDTO dto) {
                // Sætter klassens ChatDTO objekt til det man har fået ind fra databasen
                setChatDTO(dto);
                // Hvis der er nogle beskeder i den liste man har så skal den cleares (nok redundant lige her men bruges også senere)
                if (dto.getMessages() != null) {
                    beskederStrings.clear();
                    beskederStrings.addAll(dto.getMessages());
                }

                // Opsætter listviewet med chatten der skal være der
                ChatList_Adapter adapter = new ChatList_Adapter(ctx, beskederStrings, dto, person1);
                linearLayout.removeAllViews();
                for (int i = 0; i < beskederStrings.size(); i++) {
                    View item = adapter.getView(i,null,null);
                    linearLayout.addView(item);
                }
            }
        }, chatDocumentPath);

        // Vi sleeper bare 1 sek for at det ikke ser mærkeligt ud når den bliver loadet (måske skal det fjernes eller ændres)
        SystemClock.sleep(1000);

        sendBillede.setOnClickListener(this);
        tilbage.setOnClickListener(this);
        settings.setOnClickListener(this);

        // Vi opstiller en eventlistener på den chat som vi er inde i lige nu, som vil opdatere listviewet hvis der kommer en opdatering fra databasen
        FirebaseFirestore.getInstance().collection("chats").document(chatDocumentPath)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    private static final String TAG = "update from firestore";

                    @Override
                    public void onEvent(@Nullable DocumentSnapshot snapshot,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        if (snapshot != null && snapshot.exists()) {
                            ChatDTO temp = snapshot.toObject(ChatDTO.class);
                            if (temp.getChatId() != null) {
                                setChatDTO(snapshot.toObject(ChatDTO.class));
                                if (dto.getMessages() != null) {
                                    beskederStrings.clear();
                                    beskederStrings.addAll(dto.getMessages());
                                }
                            }

                            ChatList_Adapter adapter = new ChatList_Adapter(ctx, beskederStrings, dto, person1);
                            for (int i = beskederStrings.size()-1; i < beskederStrings.size(); i++) {
                                if (dto.getSender() != null) {
                                    if (dto.getSender().get(i).equals(person2)) {
                                        View item = adapter.getView(beskederStrings.size()-1, null, null);
                                        linearLayout.addView(item);
                                    }
                                }
                            }
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
        } else if (v == sendBillede) {
            // For at kunne sende et billede skal der fås adgang fra brugeren
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (this.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    // Vi bruger en klasse som vil starte aktiviteten til at få et billede
                    pictureMaker.uploadPic(this);
                } else {
                    // Hvis vi ikke har fået adgang så lav en toast der siger man har brug for adgang til den funktion
                    if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                        Toast.makeText(this, "Your Permission is needed to get access the camera", Toast.LENGTH_LONG).show();
                    }
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, REQUEST_CAMERARESULT);
                }
            } else {
                // Hvis SDK'en er mindre end M (ikke lige sikker hvilken version) så behøves adgang ikke fra brugeren
                pictureMaker.uploadPic(this);
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
        // Vi tjekker alle parametrene om de er tomme og eller null og indsætter værdier derefter
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
    // Den her funktion bruges til at hente dataen fra når man uploader eller tager et billede hvor vi vil gemme billedet i chatten og firestore
    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            try {
                // Først konverterer vi URI'en om til et bitmap som vi kan sende til firestore
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                // Vi prøver på at uploade vores bitmap/fil
                dao.uploadFile(selectedImage, new ChatDAO.FirestoreCallbackPic() {
                    @Override
                    // Vi har brug for et callback til at vide om det lykkesdes
                    public void onCallBackPic(Uri url) {
                        // Nu skal vi opdatere vores klasse objekt (lige nu skelner vi forskel med at et billede har messagen nedenunder men er meget ustabilt og skal ændres)
                        updateChatDTO(person1, person2, "pictureBlaBlaBla!:", url);

                        // Vi opdaterer vores chatobjekt i firestore
                        dao.updateChat(new ChatDAO.FirestoreCallback() {
                            @Override
                            public void onCallback(ChatDTO dto) {
                                if (dto.getChatId() != null) {
                                    setChatDTO(dto);
                                    if (dto.getMessages() != null){
                                        beskederStrings.clear();
                                        beskederStrings.addAll(dto.getMessages());
                                    }
                                    ChatList_Adapter adapter = new ChatList_Adapter(ctx, beskederStrings, dto, person1);
                                    View item = adapter.getView(beskederStrings.size() - 1, null, null);
                                    linearLayout.addView(item);
                                    // clear tekstboksen
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
