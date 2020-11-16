package Controller;

import com.google.firebase.firestore.auth.User;

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
    private UserDTO curUser;


    private Controller(){

        chatDAO = new ChatDAO();
        userDAO = new UserDAO();
        eventDAO = new EventDAO();

        this.instance = this;

    }

    public UserDTO getCurrUser(){
        return curUser;
    }

    public void setCurrUser(UserDTO user){
        this.curUser = user;
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

    public void updateUser(UserDTO userDTO){
        userDAO.updateUser(userDTO);
    }

    public void deleteUser(String userId){
        userDAO.deleteUser(userId);
    }


}
