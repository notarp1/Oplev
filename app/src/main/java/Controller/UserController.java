package Controller;

import com.A4.oplev.Login.Activity_CreateUser;
import com.A4.oplev.Activity_Profile;
import com.A4.oplev.UserSettings.U_Settings_Edit;
import com.A4.oplev.UserSettings.U_Settings_Main;

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
    String userPic = "https://firebasestorage.googleapis.com/v0/b/opleva4.appspot.com/o/question.png?alt=media&token=9dea34be-a183-4b37-bfb7-afd7a9db81f2";


    private UserController(){

        chatDAO = new ChatDAO();
        userDAO = new UserDAO();
        eventDAO = new EventDAO();

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


}
