package Controller;

import com.A4.oplev.Activity_Profile;
import com.A4.oplev.UserSettings.U_Settings_Edit;

import DAL.Classes.ChatDAO;
import DAL.Classes.EventDAO;
import DAL.Classes.UserDAO;
import DAL.Interfaces.CallbackUser;
import DTO.UserDTO;

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

    public UserDTO getCurrUser(){
        return user;
    }

    public void setCurrUser(UserDTO user){
        this.user = user;
    }


    public static Controller getInstance(){
        if (instance == null) instance = new Controller();
        return instance;
    }


    public void getUser(CallbackUser callbackUser, String userId){
        userDAO.getUser(callbackUser, userId);

    }

    public void createUser(UserDTO userDTO){
        userDAO.createUser(userDTO);
    }


    public void updateUser(U_Settings_Edit ctx){

        user.setDescription(ctx.about.getText().toString());
        user.setCity(ctx.city.getText().toString());
        user.setJob(ctx.job.getText().toString());
        user.setEducation(ctx.education.getText().toString());

        userDAO.updateUser(user);

    }

    public void deleteUser(String userId){
        userDAO.deleteUser(userId);
    }

    public void iniEditProfil(U_Settings_Edit ctx){
        ctx.about.setText(user.getDescription());
        ctx.city.setText(user.getCity());
        ctx.job.setText(user.getJob());
        ctx.education.setText(user.getEducation());
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

        } else if(user.getJob() == null || user.getJob().equals("")){
            jobText = "\uD83D\uDCBC " + "Ikke angivet";
            ctx.job.setText(jobText);
        }

    }


}
