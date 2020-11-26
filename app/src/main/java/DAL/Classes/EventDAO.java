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
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import DAL.Interfaces.CallbackEvent;
import Controller.UserController;
import DAL.Interfaces.IEventDAO;
import DTO.EventDTO;
import DTO.UserDTO;

public class EventDAO implements IEventDAO {
    FirebaseFirestore db;
    private final String TAG = "eventLog";
    private UserController userController;

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
        userController = UserController.getInstance();
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



        db.collection("events")
                .add(eventObject)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                        userController.updateUserEvents(documentReference.getId());
                        eventObject.put("eventId", documentReference.getId());
                        //overwrite database document with new ownerId.
                        db.collection("events").document(documentReference.getId())
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
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    @Override
    public void updateEvent(EventDTO event) {

    }

    @Override
    public void deleteEvent(int eventId) {

    }
}
