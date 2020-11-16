package DAL.Interfaces;

import DTO.UserDTO;

public interface CallbackUser {
    void onCallback(UserDTO user);
}
