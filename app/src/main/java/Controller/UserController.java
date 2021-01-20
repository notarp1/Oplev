package Controller;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.A4.oplev.Login.Activity_CreateUser;
import com.A4.oplev.Activity_Profile;
import com.A4.oplev.R;
import com.A4.oplev.UserSettings.U_Settings_Edit;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import DAL.Classes.ChatDAO;
import DAL.Classes.EventDAO;
import DAL.Classes.UserDAO;
import DAL.Interfaces.CallbackUser;
import DAL.Interfaces.IChatDAO;
import DAL.Interfaces.IEventDAO;
import DAL.Interfaces.IUserDAO;
import DTO.ChatDTO;
import DTO.UserDTO;
/**
 * @Author Christian Pone
 */

public class UserController {

    private static UserController instance = null;
    IChatDAO chatDAO;
    IUserDAO userDAO;
    IEventDAO eventDAO;
    private UserDTO user;
    private boolean isSafe;
    String userPic = "https://firebasestorage.googleapis.com/v0/b/opleva4.appspot.com/o/question.png?alt=media&token=9dea34be-a183-4b37-bfb7-afd7a9db81f2";

    private StorageReference mStorageRef, picRefProfile;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    public int indexPlace, picNumber, indexNumbers, pictureCount;

    private ArrayList<String> pictures;


    private UserController(IUserDAO userDAO, IChatDAO chatDAO, IEventDAO eventDAO){

        this.chatDAO = chatDAO;
        this.userDAO = userDAO;
        this.eventDAO = eventDAO;
        indexNumbers = 0;
        pictureCount = 0;
        setSafe(true);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        this.instance = this;

    }

    public static UserController getInstance(UserDAO userDAO, IChatDAO chatDAO, IEventDAO eventDAO){
        if (instance == null) {
            instance = new UserController(userDAO, chatDAO, eventDAO);
        }
        return instance;
    }
    public static UserController getInstance(){
        return instance;
    }

    public void setCurrUser(UserDTO user){
        this.user = user;
    }

    public UserDTO getCurrUser(){
        return this.user;
    }


    public void getUser(CallbackUser callbackUser, String userId){
        userDAO.getUser(callbackUser, userId);
    }

    public void createUser(String userId, Activity_CreateUser ctx){

        UserDTO user = new UserDTO();


        user.setfName(String.valueOf(ctx.fName.getText()));
        user.setlName(String.valueOf(ctx.lName.getText()));
        user.setCity(String.valueOf(ctx.city.getText()));
        user.setEmail(String.valueOf(ctx.email.getText()));
        user.setChatId(null);
        user.setAge(Integer.parseInt(String.valueOf(ctx.age.getText())));
        user.setUserId(userId);
        user.setEvents(null);
        user.setRequestedEvents(null);
        user.setUserPicture(userPic);

        userDAO.createUser(user);

    }


    public void updateUser(U_Settings_Edit ctx, ArrayList<String> pictures){
        int i = 0;

        while(i<6){

            if(pictures.get(i) != null){
                userPic = pictures.get(i);
                break;
            } else  userPic = "https://firebasestorage.googleapis.com/v0/b/opleva4.appspot.com/o/question.png?alt=media&token=9dea34be-a183-4b37-bfb7-afd7a9db81f2";
            i++;
        }

        user.setDescription(ctx.about.getText().toString());
        user.setCity(ctx.city.getText().toString());
        user.setJob(ctx.job.getText().toString());
        user.setEducation(ctx.education.getText().toString());
        user.setPictures(pictures);
        user.setUserPicture(userPic);
        userDAO.updateUser(user);

    }
    public void updateUserSimple(UserDTO user){

        userDAO.updateUser(user);

    }

    public String getUserAvatar(){
        return user.getUserPicture();
    }


    public void updateUserEvents(String event){
        ArrayList<String> eventList = user.getEvents();
        eventList.add(event);
        user.setEvents(eventList);
        userDAO.updateUser(user);
    }

    public ArrayList<String> getUserPictures(){
        return user.getPictures();
    }



    public void onClickAccept(Activity_Profile ctx, String header, String eventId, UserDTO otheruser){

        // vi laver et chatobjekt med de nødvendige informationer
        ChatDTO chatDTO = new ChatDTO(null,null,null,null,null,null,header, getCurrUser().getfName(),otheruser.getfName(), getCurrUser().getUserId(),otheruser.getUserId(),eventId);

        chatDAO.createChat(chatDTO, chatID -> {
            // vi skal opdatere eventet til at have en participant

            eventDAO.getEvent(event -> {
                if (event != null) {
                    //event.setApplicants(new ArrayList<>());
                    event.setParticipant(otheruser.getUserId());
                    eventDAO.updateEvent(event);
                }
            }, eventId);

            // vi skal indsætte chatid'et på begge brugeres lister
            ArrayList<String> otherUserChatID;
            if (otheruser.getChatId() == null){
                otherUserChatID = new ArrayList<>();
            } else otherUserChatID = otheruser.getChatId();
            otherUserChatID.add(chatID);


            ArrayList<String> thisUserChatID;
            if (getCurrUser().getChatId() == null){
                thisUserChatID = new ArrayList<>();
            } else thisUserChatID = getCurrUser().getChatId();
            thisUserChatID.add(chatID);

            // vi opdaterer begge brugere i databasen

            userDAO.updateUser(otheruser);
            userDAO.updateUser(getCurrUser());
            ctx.finish();
        });
    }


    public void onClickReject(Activity_Profile ctx, ArrayList<String> otherApplicants, String eventId, String header){

        // hvis der stadigvæk er nogle applicants tilbage i listen
        if (otherApplicants.size() != 0) {
            // vi henter eventet for at kunne opdatere det
            eventDAO.getEvent(event -> {
                if (event != null) {
                    // vi fjerner den applicant man har afvist og opdaterer det i databasen
                    ArrayList<String> newApplicants = event.getApplicants();
                    newApplicants.remove(0);
                    event.setApplicants(newApplicants);
                    eventDAO.updateEvent(event);
                }
            }, eventId);

            // vi fjerner den man har afvist i vores liste
            otherApplicants.remove(0);
            // hvis der er flere personer tilbage
            if (otherApplicants.size() != 0) {
                // vi indlæser den næste applicant
                getUser(user -> {
                    // start nyt intent med den næste applicant
                    Intent i12 = new Intent(ctx, Activity_Profile.class);
                    i12.putExtra("user", user);
                    i12.putExtra("load", 2);
                    i12.putExtra("numberOfApplicants", otherApplicants.size() - 1);
                    i12.putExtra("applicantList", otherApplicants);
                    i12.putExtra("header", header);
                    i12.putExtra("eventID", eventId);
                    ctx.startActivity(i12);
                }, otherApplicants.get(0));
            }
        }
        // afslut aktiviteten så man ikke kan gå tilbage
        ctx.finish();
    }
    public void iniProfile(Activity_Profile ctx){
        String aboutText = user.getfName() + ", " + user.getAge();
        String cityText =  user.getCity();
        String descText = user.getDescription();
        String aboutNameText = "Om "+ user.getfName();
        String eduText =  user.getEducation();
        String jobText =  user.getJob();

        ctx.about.setText(aboutText);
        ctx.city.setText(cityText);
        ctx.desc.setText(descText);
        ctx.aboutName.setText(aboutNameText);
        ctx.edu.setText(eduText);
        ctx.job.setText(jobText);

        if(user.getEducation() == null || user.getEducation().equals("")){
            eduText = "Ikke angivet";
            ctx.edu.setText(eduText);

        }
        if(user.getJob() == null || user.getJob().equals("")){
            jobText = "Ikke angivet";
            ctx.job.setText(jobText);
        }

    }

    public void iniPublicProfile(Activity_Profile ctx, UserDTO user){
        String aboutText = user.getfName() + ", " + user.getAge();
        String cityText =  user.getCity();
        String descText = user.getDescription();
        String aboutNameText = "Om "+ user.getfName();
        String eduText = user.getEducation();
        String jobText = user.getJob();

        ctx.about.setText(aboutText);
        ctx.city.setText(cityText);
        ctx.desc.setText(descText);
        ctx.aboutName.setText(aboutNameText);
        ctx.edu.setText(eduText);
        ctx.job.setText(jobText);

        if(user.getEducation() == null || user.getEducation().equals("")){
            eduText = "Ikke angivet";
            ctx.edu.setText(eduText);

        }
        if(user.getJob() == null || user.getJob().equals("")){
            jobText = "Ikke angivet";
            ctx.job.setText(jobText);
        }

    }

    public void iniEditProfile(U_Settings_Edit ctx){
        ctx.about.setText(user.getDescription());
        ctx.city.setText(user.getCity());
        ctx.job.setText(user.getJob());
        ctx.education.setText(user.getEducation());
        ctx.textview.setText("Rediger Profil");
        ctx.pictures = getUserPictures();
        ctx.stockphotoBit = BitmapFactory.decodeResource(ctx.getContext().getResources(), R.drawable.uploadpic);
    }




    /*KODE HÅNDTERE U_Settings_Options */
    public void deleteUser(UserDTO user, Context ctx){
        userDAO.deleteUser(user, ctx);
    }

    /*KODE HÅNDTERE U_Settings_Edit */
    public void iniEdit(U_Settings_Edit ctx){
       ImageView avatar;
       pictures = getUserPictures();
           for(int i = 0; i<6; i++){
                if(pictures.get(i) != null){
                    avatar = getPictureNumber(i, ctx);
                    Picasso.get().load(pictures.get(i)).into(avatar);
                }
            }
    }

    public void uploadPicture(U_Settings_Edit ctx, Uri[] uris){

        pictures = getUserPictures();
        for (Uri a : uris) {
            if (a != null) {
                pictureCount += 1;

            }
        }
        if(pictureCount > 0 )
            for (int i = 0; i < 6; i++) {

                System.out.println(uris[i]);
                System.out.println(i);

                if (uris[i] != null) {
                    indexPlace = i;

                    Uri filePic = uris[i];
                    picRefProfile = mStorageRef.child("users/" + user.getUserId() + "/" + i);

                    userDAO.uploadFile(getInstance(), picRefProfile, filePic, indexPlace, pictures, ctx);
                }


            } else updateUserAndGUI(ctx);

    }

    public void deletePicture(int number, ArrayList<String> pictures, U_Settings_Edit ctx, Bitmap stockphotoBit) {
       if(pictures.get(number) != null && pictures.get(number).substring(0,23).equals("https://firebasestorage")){

           //put i DAL
           StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(pictures.get(number));
           storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
               @Override
               public void onSuccess(Void aVoid) {
                   // File deleted successfully

               }
           }).addOnFailureListener(new OnFailureListener() {
               @Override
               public void onFailure(@NonNull Exception exception) {
                   // Uh-oh, an error occurred!

               }
           });

           pictures.set(number, null);
           setSafe(false);
           updateUserAndGUI(ctx);
            ImageView pic = getPictureNumber(number, ctx);
            pic.setImageBitmap(stockphotoBit);
        } else if (pictures.get(number) != null){
           UserDTO dto = getCurrUser();
           dto.getPictures().set(number,null);
           dto.setPictures(dto.getPictures());
           setSafe(false);
           updateUserSimple(dto);
           updateUserAndGUI(ctx);
           ImageView pic = getPictureNumber(number, ctx);
           pic.setImageBitmap(stockphotoBit);
       }
    }

    private ImageView getPictureNumber(int picNumber, U_Settings_Edit ctx){
        ImageView returnPic = ctx.p0;
        switch (picNumber){
            case 0:
                returnPic = ctx.p0;
                break;
            case 1:
                returnPic = ctx.p1;
                break;
            case 2:
                returnPic = ctx.p2;
                break;
            case 3:
                returnPic = ctx.p3;
                break;
            case 4:
                returnPic = ctx.p4;
                break;
            case 5:
                returnPic = ctx.p5;
                break;
        }
        return  returnPic;
    }

    public void setPictures(int index, Uri uri, ArrayList<String> pictures){

        String address = String.valueOf(uri);

        pictures.set(index, address);

    }

    public void updateUserAndGUI(U_Settings_Edit ctx) {
        pictures = getUserPictures();
        updateUser(ctx, pictures);

        Toast.makeText(ctx.getContext(), "Profil opdateret!", Toast.LENGTH_SHORT).show();
    }

    public void setSafe(boolean safe) {
        this.isSafe = safe;
    }

    public boolean isSafe() {
        return isSafe;
    }
}
