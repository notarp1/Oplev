package DAL.Interfaces;

import DAL.Classes.UserDAO;
import DTO.EventDTO;
import DTO.UserDTO;

public interface IUserDAO {

    void getUser(UserDAO.MyCallback callback, String userId);
    void createUser(UserDTO user);
    void updateUser(UserDTO user);
    void deleteUser(String userId);


}
