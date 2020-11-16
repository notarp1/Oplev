package DAL.Interfaces;

import DTO.EventDTO;
import DTO.UserDTO;

public interface IUserDAO {

    UserDTO getUser(String userId);
    void createUser(UserDTO user);
    void updateUser(UserDTO user);
    void deleteUser(String userId);


}
