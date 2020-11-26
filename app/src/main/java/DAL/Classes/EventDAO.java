package DAL.Classes;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import DAL.Interfaces.IEventDAO;
import DTO.EventDTO;

public class EventDAO implements IEventDAO {
    @Override
    public EventDTO getEvent(int eventId) {
        return null;
    }

    @Override
    public void createEvent(EventDTO event) {
        // send new event to db
        Map<String, Object> eventObject = new HashMap<>();

        eventObject.put("ownerID", event.getOwnerId());
        eventObject.put("eventID", event.getEventId());
        eventObject.put("title", event.getEventId());
        eventObject.put("description", event.getDescription());
        eventObject.put("price", event.getPrice());
        eventObject.put("date", event.getDate());
        eventObject.put("city", event.getCity());
        eventObject.put("minAge", event.getMinAge());
        eventObject.put("maxAge", event.getMaxAge());
        eventObject.put("maleOn", event.isMaleOn());
        eventObject.put("femaleOn", event.isFemaleOn());
        eventObject.put("participant", event.getParticipant());
        eventObject.put("pictures", event.getEventPic());
        eventObject.put("applicants", event.getApplicants());
        //all set except "headline" unsure of what this supposed to be if not title again

       /* UNFINISHED connection to db
       * ( SHOULD ID'S BE STRINGS INSTEAD OF INT'S? HOW ARE THEY CREATED? )
       db.collection("events")
                .document(event.getEventId())
                .set(eventObject)
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference);

                })
                .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));*/
    }

    @Override
    public void updateEvent(EventDTO event) {

    }

    @Override
    public void deleteEvent(int eventId) {

    }
}
