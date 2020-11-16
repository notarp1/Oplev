package Controller;

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


}
