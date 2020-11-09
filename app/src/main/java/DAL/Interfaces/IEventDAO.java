package DAL.Interfaces;

import DTO.EventDTO;

public interface IEventDAO {

    EventDTO getEvent(int eventId);
    void createEvent(EventDTO event);
    void updateEvent(EventDTO event);
    void deleteEvent(int eventId);
}
