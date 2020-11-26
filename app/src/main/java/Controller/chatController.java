package Controller;

import com.A4.oplev.Activity_Profile;
import com.A4.oplev.Login.Activity_CreateUser;
import com.A4.oplev.UserSettings.U_Settings_Edit;
import com.A4.oplev.UserSettings.U_Settings_Main;

import java.util.ArrayList;

import DAL.Classes.ChatDAO;
import DAL.Classes.EventDAO;
import DAL.Classes.UserDAO;
import DAL.Interfaces.CallbackUser;
import DTO.EventDTO;
import DTO.UserDTO;

public class chatController {
    private static chatController instance = null;


    private chatController(){


        this.instance = this;

    }

    public static chatController getInstance(){
        if (instance == null) instance = new chatController();
        return instance;
    }


}
