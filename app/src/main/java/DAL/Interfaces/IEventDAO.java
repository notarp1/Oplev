package DAL.Interfaces;

import DTO.EventDTO;

public interface IEventDAO {

    void getEvent(CallbackEvent callbackEvent, String eventId);
    void createEvent(EventDTO event);
    void updateEvent(EventDTO event);
    void deleteEvent(int eventId);
}
