package DAL.Interfaces;

import android.content.Context;
import android.net.Uri;

import com.A4.oplev.UserSettings.U_Settings_Edit;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import Controller.UserController;
import DAL.Classes.UserDAO;
import DTO.UserDTO;

public interface IUserDAO {

    void getUser(CallbackUser callback, String userId);
    void createUser(UserDTO user);
    void updateUser(UserDTO user);
    void deleteUser(UserDTO user, Context ctx);
    void uploadFile(UserController userController, StorageReference picRefProfile, Uri filePic, int indexPlace, ArrayList<String> pictures, U_Settings_Edit ctx);


}
