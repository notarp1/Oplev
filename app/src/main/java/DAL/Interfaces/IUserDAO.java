package DAL.Interfaces;

import DTO.EventDTO;
import DTO.UserDTO;

public interface IUserDAO {

    UserDTO getUser(int userId);
    void createUser(UserDTO user);
    void updateUser(UserDTO user);
    void deleteUser(int userId);


}
