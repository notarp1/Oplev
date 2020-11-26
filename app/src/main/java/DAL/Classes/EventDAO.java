package DAL.Classes;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import DAL.Interfaces.IEventDAO;
import DTO.EventDTO;

public class EventDAO implements IEventDAO {
    FirebaseFirestore db;
    private final String TAG = "eventLog";

    public EventDAO(){
        this.db = FirebaseFirestore.getInstance();
    }

    @Override
    public EventDTO getEvent(int eventId) {
        return null;
    }

    @Override
    public void createEvent(EventDTO event) {
        // send new event to db
        Map<String, Object> eventObject = new HashMap<>();

        eventObject.put("ownerId", event.getOwnerId());
        eventObject.put("ownerPic", event.getOwnerPic());
        eventObject.put("eventId", event.getEventId());
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
        eventObject.put("eventPic", event.getEventPic());
        eventObject.put("applicants", event.getApplicants());
        eventObject.put("type", event.getType());

        //UNFINISHED connection to db
        //create the new event document
       db.collection("events")
                .add(eventObject)
                .addOnSuccessListener(documentReference -> {
                    //when created
                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference);
                    //overwrite hashmap ownerId
                    eventObject.put("eventId", String.valueOf(documentReference));
                    //overwrite database document with new ownerId.
                    db.collection("events").document(String.valueOf(documentReference))
                            .set(eventObject)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully written!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error writing document", e);
                                }
                            });
                })
                .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));
    }

    @Override
    public void updateEvent(EventDTO event) {

    }

    @Override
    public void deleteEvent(int eventId) {

    }
}
