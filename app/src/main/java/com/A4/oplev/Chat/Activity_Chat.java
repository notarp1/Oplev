package com.A4.oplev.Chat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.A4.oplev.Activity_Event;
import com.A4.oplev.CreateEvent.Activity_Create_Event;
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
import DAL.Classes.EventDAO;
import DAL.Classes.UserDAO;
import DAL.Interfaces.CallbackEvent;
import DAL.Interfaces.CallbackUser;
import DTO.ChatDTO;
import DTO.EventDTO;
import DTO.UserDTO;


public class Activity_Chat extends AppCompatActivity  implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
    private ImageView settings, tilbage, sendBillede, sendMessage;
    private TextView navn;
    private ScrollView beskeder;
    private LinearLayout linearLayout;
    private ArrayList<String> beskederStrings = new ArrayList<>();
    private EditText inputTekst;
    private ChatDTO dto;
    private ChatDAO dao = new ChatDAO();
    private Context ctx;
    private String person1, person2, chatDocumentPath;
    private static final int REQUEST_CAMERARESULT=201;
    private PictureMaker pictureMaker;
    private Intent intent;


    public void onCreate(Bundle saveInstanceState) {
        pictureMaker = PictureMaker.getInstance();

        // Vi instantierer layoutet
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_chat_funktion);
        ctx = this;

        // her får vi intentet med navnet på den man skriver med
        intent = getIntent();
        chatDocumentPath = intent.getStringExtra("chatId");
        person1 = intent.getStringExtra("currentUser");
        person2 = intent.getStringExtra("otherUser");

        // finder elementerne i layoutet
        settings = findViewById(R.id.chat_settings);
        tilbage = findViewById(R.id.chat_topbar_arrow);
        navn = findViewById(R.id.chat_topbar_text);
        navn.setText(person2);
        sendBillede = findViewById(R.id.chat_upload_picture);
        beskeder = findViewById(R.id.chat_beskedList);
        inputTekst = findViewById(R.id.chat_inputBesked2);
        linearLayout = findViewById(R.id.chat_besked_linearlayout);
        sendMessage = findViewById(R.id.chat_send_message);


        // For at kunne sende besked med enter på IME tastaturet så bruger vi den her funktion
        inputTekst.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                // Checker hvis man har trykket GO (enter)
                if (actionId == EditorInfo.IME_ACTION_GO | actionId == EditorInfo.IME_ACTION_DONE | actionId == EditorInfo.IME_ACTION_SEND | actionId == EditorInfo.IME_ACTION_NEXT) {
                    // Vi gider kun sende teksten hvis den ikke er tom
                    if (!inputTekst.getText().toString().equals("")) {
                        if (person1 != null && person2 != null && inputTekst.getText().toString() != null) {
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
                                        View item = adapter.getView(beskederStrings.size() - 1, null, null);
                                        linearLayout.addView(item);
                                        // clear tekstboksen
                                        inputTekst.setText("");
                                        beskeder.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                beskeder.fullScroll(View.FOCUS_DOWN);
                                            }
                                        });
                                    }
                                }
                            }, dto);
                        }
                        handled = true;
                    } else Toast.makeText(ctx, "Der opstod en fejl ved afsendelse af besked. Prøv venligst igen", Toast.LENGTH_SHORT).show();
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
                beskeder.post(new Runnable() {
                    @Override
                    public void run() {
                        beskeder.fullScroll(View.FOCUS_DOWN);
                    }
                });
            }
        }, chatDocumentPath);


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
                            if (temp.getMessages() != null) {
                                if (temp.getMessages().size() != beskederStrings.size()) {
                                    if (temp.getChatId() != null) {
                                        setChatDTO(temp);
                                        if (dto.getMessages() != null) {
                                            beskederStrings.clear();
                                            beskederStrings.addAll(dto.getMessages());
                                        }
                                    }


                                    ChatList_Adapter adapter = new ChatList_Adapter(ctx, beskederStrings, dto, person1);
                                    if (dto.getSender() != null) {
                                        if (dto.getSender().get(dto.getSender().size() - 1).equals(person2)) {
                                            View item = adapter.getView(beskederStrings.size() - 1, null, null);
                                            linearLayout.addView(item);
                                        }
                                    }
                                    beskeder.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            beskeder.fullScroll(View.FOCUS_DOWN);
                                        }
                                    });                                }
                            }
                        } else {
                            Log.d(TAG, "Current data: null");
                        }
                    }
                });

        sendBillede.setOnClickListener(this);
        tilbage.setOnClickListener(this);
        settings.setOnClickListener(this);
        sendMessage.setOnClickListener(this);
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
            showPopup(settings);
        } else if (v == sendMessage){
            // Vi gider kun sende teksten hvis den ikke er tom
            if (!inputTekst.getText().toString().equals("")) {
                if (person1 != null && person2 != null && inputTekst.getText().toString() != null) {
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
                                View item = adapter.getView(beskederStrings.size() - 1, null, null);
                                linearLayout.addView(item);
                                // clear tekstboksen
                                inputTekst.setText("");
                                beskeder.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        beskeder.fullScroll(View.FOCUS_DOWN);
                                    }
                                });
                            }
                        }
                    }, dto);
                } else Toast.makeText(ctx, "Der opstod en fejl ved afsendelse af besked. Prøv venligst igen", Toast.LENGTH_SHORT).show();
            }
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
                        if (person1 != null && person2 != null) {
                            // Nu skal vi opdatere vores klasse objekt (lige nu skelner vi forskel med at et billede har messagen nedenunder men er meget ustabilt og skal ændres)
                            updateChatDTO(person1, person2, "pictureBlaBlaBla!:", url);

                            // Vi opdaterer vores chatobjekt i firestore
                            dao.updateChat(new ChatDAO.FirestoreCallback() {
                                @Override
                                public void onCallback(ChatDTO dto) {
                                    if (dto.getChatId() != null) {
                                        setChatDTO(dto);
                                        if (dto.getMessages() != null) {
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
                        } else Toast.makeText(ctx, "Der opstod en fejl ved afsendelse af besked. Prøv venligst igen", Toast.LENGTH_SHORT).show();
                    }
                },chatDocumentPath, person1);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(Activity_Chat.this, "Der skete en fejl ved indlæsning af billedet. Prøv venligst igen", Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(Activity_Chat.this, "Der blev ikke valgt noget billede",Toast.LENGTH_LONG).show();
        }
    }


    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        popup.setOnMenuItemClickListener(Activity_Chat.this);
        Menu m = popup.getMenu();
        inflater.inflate(R.menu.menu_chats_popup, m);
        MenuItem item = m.findItem(R.id.chat_forlad);
        SpannableString spanString = new SpannableString(item.getTitle().toString());
        spanString.setSpan(new ForegroundColorSpan(Color.RED), 0, spanString.length(), 0);
        item.setTitle(spanString);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        EventDAO eventDAO = new EventDAO();
        switch (item.getItemId()) {
            case R.id.chat_info:
                eventDAO.getEvent(new CallbackEvent() {
                    @Override
                    public void onCallback(EventDTO event) {
                        UserDAO userDAO = new UserDAO();
                        userDAO.getUser(new CallbackUser() {
                            @Override
                            public void onCallback(UserDTO user) {
                                Intent i = new Intent(ctx, Activity_Event.class);
                                i.putExtra("event", event);
                                i.putExtra("user", user);
                                i.putExtra("load", 1);
                                ctx.startActivity(i);
                            }
                        }, event.getOwnerId());
                    }
                }, intent.getStringExtra("eventID"));
                return true;
            case R.id.chat_report:
                Toast.makeText(ctx,"Denne funktion er ikke blevet implementeret endnu :(", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.chat_forlad:
                new AlertDialog.Builder(ctx)
                        .setTitle("Forlad chat")
                        .setMessage("Er du sikker på at du vil forlade denne chat med " + person2 + "?\nDette vil resultere i at eventet bliver aflyst og alle beskeder bliver fjernet")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(ctx, "Denne funktion er ikke blevet implementeret endnu", Toast.LENGTH_SHORT).show();
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                return true;
            default:
                return false;
        }
    }
}
