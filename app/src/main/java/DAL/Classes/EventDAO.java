package DAL.Classes;

import android.content.Intent;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.NonNull;

import com.A4.oplev.__Main.Activity_Main;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import DAL.Interfaces.CallbackEvent;
import DAL.Interfaces.IEventDAO;
import DTO.EventDTO;
import DTO.UserDTO;

public class EventDAO implements IEventDAO {

    public FirebaseFirestore db;
    private static final String TAG = "eventLog" ;
    private String collectionPath = "events";

    public EventDAO(){this.db = FirebaseFirestore.getInstance();}

    @Override
    public void getEvent(CallbackEvent callbackEvent, String eventId) {

        DocumentReference docRef = db.collection(collectionPath).document(eventId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                DocumentSnapshot documentSnapshot = task.getResult();
                if(documentSnapshot != null){
                    System.out.println(documentSnapshot.getData());
                    EventDTO event = documentSnapshot.toObject(EventDTO.class);
                    callbackEvent.onCallback(event);
                }
            }
        });

    }


    @Override
    public void createEvent(EventDTO event) {
        // send new event to db
        Map<String, Object> eventObject = new HashMap<>();

        eventObject.put("ownerID", event.getOwner());
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
        eventObject.put("pictures", event.getPictures());
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
