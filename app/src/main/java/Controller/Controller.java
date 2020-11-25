package Controller;

import com.A4.oplev.Login.Activity_CreateUser;
import com.A4.oplev.Activity_Profile;
import com.A4.oplev.UserSettings.U_Settings_Edit;
import com.A4.oplev.UserSettings.U_Settings_Main;

import java.util.ArrayList;
import java.util.Date;

import DAL.Classes.ChatDAO;
import DAL.Classes.EventDAO;
import DAL.Classes.UserDAO;
import DAL.Interfaces.CallbackUser;
import DTO.UserDTO;

import DTO.EventDTO;

public class Controller {
    private static Controller instance = null;
    static ChatDAO chatDAO;
    static UserDAO userDAO;
    static EventDAO eventDAO;
    private UserDTO user;


    private Controller(){

        chatDAO = new ChatDAO();
        userDAO = new UserDAO();
        eventDAO = new EventDAO();

        this.instance = this;

    }

    public static Controller getInstance(){
        if (instance == null) instance = new Controller();
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

        userDAO.createUser(user);

    }


    public void updateUser(U_Settings_Edit ctx, ArrayList<String> pictures){


        user.setDescription(ctx.about.getText().toString());
        user.setCity(ctx.city.getText().toString());
        user.setJob(ctx.job.getText().toString());
        user.setEducation(ctx.education.getText().toString());
        user.setPictures(pictures);
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

    public void createEvent(String name, String desc, String price, String date, String time, String city,
                            String minAge, String maxAge, boolean maleOn, boolean femaleOn){
        //create event dto
        EventDTO event = new EventDTO();

        /*
        todo: set owner of the event. is it string?
        event.setOwner(getCurrUser().getUserId());*/

        //set some values of DTO
        event.setName(name);
        event.setDescription(desc);
        event.setPrice(Integer.parseInt(price));

        //getting date and time from string method input parameters
        //date string input is DD/MM/YYYY
        String[] dateSplit = date.split("/");
        int day = Integer.parseInt(dateSplit[0]);
        int month = Integer.parseInt(dateSplit[1]) - 1; //decrement to null index months of Date()
        int year = Integer.parseInt(dateSplit[2]) - 1900; //subtract cuz wtf with this date obj
        //getting time the same way. time input format HH:MM
        String[] timeSplit = time.split(":");
        int hour = Integer.parseInt(timeSplit[0]);
        int minute = Integer.parseInt(timeSplit[1]);
        //set the date now that values are available
        event.setDate(new Date(year, month, day, hour, minute));

        //set rest of values
        event.setCity(city);
        event.setMinAge(Integer.parseInt(minAge));
        event.setMaxAge(Integer.parseInt(maxAge));
        event.setMaleOn(maleOn);
        event.setFemaleOn(femaleOn);

        /*//testing
        System.out.println("maleOn:" + maleOn);
        System.out.println(day + "/" + month + "/" + year + "\n"
                +"date:" + event.getDate());*/

        EventDAO eventDAO = new EventDAO();
        eventDAO.createEvent(event);
    }
}
