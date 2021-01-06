package Controller;

import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.A4.oplev.Login.Activity_CreateUser;
import com.A4.oplev.Activity_Profile;
import com.A4.oplev.UserSettings.U_Settings_Edit;
import com.A4.oplev.UserSettings.U_Settings_Main;
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
import DTO.UserDTO;

public class UserController {
    private static UserController instance = null;
    static ChatDAO chatDAO;
    static UserDAO userDAO;
    static EventDAO eventDAO;
    private UserDTO user;
    private boolean isSafe;
    String userPic = "https://firebasestorage.googleapis.com/v0/b/opleva4.appspot.com/o/question.png?alt=media&token=9dea34be-a183-4b37-bfb7-afd7a9db81f2";

    private StorageReference mStorageRef, picRefProfile;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private int indexPlace, picNumber, indexNumbers, pictureCount;

    private ArrayList<String> pictures;


    private UserController(){

        chatDAO = new ChatDAO();
        userDAO = new UserDAO();
        eventDAO = new EventDAO();
        indexNumbers = 0;
        pictureCount = 0;
        setSafe(true);
        //ny
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        this.instance = this;

    }

    public static UserController getInstance(){
        if (instance == null) instance = new UserController();
        return instance;
    }

    public void setCurrUser(UserDTO user){
        this.user = user;
    }

    public UserDTO getCurrUser(){
        return user;
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
        user.setJoinedEvents(null);
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

    public void deleteUser(String userId){
        userDAO.deleteUser(userId);
    }


    public void iniProfile(Activity_Profile ctx){
        String aboutText = user.getfName() + ", " + user.getAge();
        String cityText = "\uD83D\uDCCD " + user.getCity();
        String descText = user.getDescription();
        String aboutNameText = "Om "+ user.getfName();
        String eduText = "\uD83C\uDF93 " + user.getEducation();
        String jobText = "\uD83D\uDCBC " + user.getJob();

        ctx.about.setText(aboutText);
        ctx.city.setText(cityText);
        ctx.desc.setText(descText);
        ctx.aboutName.setText(aboutNameText);
        ctx.edu.setText(eduText);
        ctx.job.setText(jobText);

        if(user.getEducation() == null || user.getEducation().equals("")){
            eduText = "\uD83C\uDF93 " + "Ikke angivet";
            ctx.edu.setText(eduText);

        }
        if(user.getJob() == null || user.getJob().equals("")){
            jobText = "\uD83D\uDCBC " + "Ikke angivet";
            ctx.job.setText(jobText);
        }

    }

    public void iniPublicProfile(Activity_Profile ctx, UserDTO user){
        String aboutText = user.getfName() + ", " + user.getAge();
        String cityText = "\uD83D\uDCCD " + user.getCity();
        String descText = user.getDescription();
        String aboutNameText = "Om "+ user.getfName();
        String eduText = "\uD83C\uDF93 " + user.getEducation();
        String jobText = "\uD83D\uDCBC " + user.getJob();

        ctx.about.setText(aboutText);
        ctx.city.setText(cityText);
        ctx.desc.setText(descText);
        ctx.aboutName.setText(aboutNameText);
        ctx.edu.setText(eduText);
        ctx.job.setText(jobText);

        if(user.getEducation() == null || user.getEducation().equals("")){
            eduText = "\uD83C\uDF93 " + "Ikke angivet";
            ctx.edu.setText(eduText);

        }
        if(user.getJob() == null || user.getJob().equals("")){
            jobText = "\uD83D\uDCBC " + "Ikke angivet";
            ctx.job.setText(jobText);
        }

    }



    public void iniEditProfile(U_Settings_Edit ctx){
        ctx.about.setText(user.getDescription());
        ctx.city.setText(user.getCity());
        ctx.job.setText(user.getJob());
        ctx.education.setText(user.getEducation());
    }

    public void iniUserMainSettings(U_Settings_Main ctx){
        String aboutText = user.getfName() + ", " + user.getAge();
        ctx.about.setText(aboutText);

    }


    /*KODE HÅNDTERE USER EDIT */
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
                    picRefProfile = mStorageRef.child("users/" + currentUser.getUid() + "/" + i);

                    //Dette skal være callback

                    picRefProfile.putFile(filePic)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    // Get a URL to the uploaded content

                                    picRefProfile.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            Uri downloadUrl = uri;

                                            indexNumbers += 1;
                                            setPictures(indexPlace, downloadUrl, pictures);
                                            setSafe(false);
                                            updateUserAndGUI(ctx);

                                        }

                                    });


                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle unsuccessful uploads
                                    // ...
                                }
                            });

                }


            } else updateUserAndGUI(ctx);

    }

    public void deletePicture(int number, ArrayList<String> pictures, U_Settings_Edit ctx, Bitmap stockphotoBit) {

       if(pictures.get(number) != null){
            pictures.set(number, null);
            setSafe(false);
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

    private void setPictures(int index, Uri uri, ArrayList<String> pictures){

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
