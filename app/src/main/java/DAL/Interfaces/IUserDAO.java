package DAL.Interfaces;

import android.content.Context;

import DAL.Classes.UserDAO;
import DTO.UserDTO;

public interface IUserDAO {

    void getUser(CallbackUser callback, String userId);
    void createUser(UserDTO user);
    void updateUser(UserDTO user);
    void deleteUser(UserDTO user, Context ctx);


}
