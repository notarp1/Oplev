package DAL.Interfaces;

import DTO.EventDTO;
import DTO.UserDTO;

public interface CallbackEvent {
    void onCallback(EventDTO event);
}
