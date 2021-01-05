package DAL.Interfaces;

import android.net.Uri;

import DTO.EventDTO;

public interface IEventDAO {

    void getEvent(CallbackEvent callbackEvent, String eventId);
    void createEvent(EventDTO event, Uri picUri);
    void updateEvent(EventDTO event);
    void deleteEvent(int eventId);
}
