package DAL.Classes;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import Controller.UserController;
import DAL.Interfaces.CallBackEventList;
import DAL.Interfaces.CallBackList;
import DAL.Interfaces.CallbackEvent;
import DAL.Interfaces.IEventDAO;
import DTO.EventDTO;

public class EventDAO implements IEventDAO {
    FirebaseFirestore db;
    private final String TAG = "eventLog";
    private UserController userController;
    private String collectionPath = "events";
    private StorageReference mStorageRef;
    private StorageReference picRef;

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
    public void getEvents(CallBackEventList callbackEventList, List<String> Ids) {
        List<EventDTO> res = new ArrayList<>();
        for(String id : Ids){
            getEvent(new CallbackEvent() {
                @Override
                public void onCallback(EventDTO event) {
                    res.add(event);
                    if(res.size() == Ids.size()){
                        callbackEventList.onCallback(res);
                    }
                }
            } ,id);
        }

    }


    public void getEventIDs(CallBackList callBackList){
        CollectionReference docRef = db.collection(collectionPath);
        docRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List<String> list = new ArrayList<>();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {

                                list.add(document.getId());
                            }
                            callBackList.onCallback(list);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }


                    }
                });
    }

    @Override
    public void createEvent(EventDTO event, Uri picUri) {
        userController = UserController.getInstance();
        // send new event to db
        Map<String, Object> eventObject = new HashMap<>();

        eventObject.put("ownerId", event.getOwnerId());
        eventObject.put("ownerPic", event.getOwnerPic());
        eventObject.put("eventId", event.getEventId());
        eventObject.put("title", event.getTitle());
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


        //first add event (no pic, no eventid)
        db.collection("events")
                .add(eventObject)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                        //second add eventid to users' event list
                        userController.updateUserEvents(documentReference.getId());

                        //overwrite object map eventid
                        eventObject.put("eventId", documentReference.getId());

                        //get storage reference
                        mStorageRef  = FirebaseStorage.getInstance().getReference();

                        //if a picture to upload was chosen then upload pic else skip that part
                        if(picUri!=null) {
                            //set picture reference (path where pic will be saved
                            picRef = mStorageRef.child("events/" + documentReference.getId() + "/1");
                            picRef.putFile(picUri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            // Get a URL to the uploaded content picture
                                            picRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    //set URL of eventpic in eventObject map
                                                    eventObject.put("eventPic", String.valueOf(uri));
                                                    //eventObject is now updated with eventId and eventPic URL
                                                    //update the event in db
                                                    //overwrite database document with new eventId and link to pic
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
                                            });
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception exception) {
                                            // Handle unsuccessful uploads
                                            // ...
                                        }
                                    });
                        }
                        else{
                            //set picture reference  (path of pic, here hardcoded cuz default pic)
                            picRef = mStorageRef.child("events/default/2.jpg");
                            //get pic url from db
                            picRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    //set URL of eventpic in eventObject map
                                    eventObject.put("eventPic", String.valueOf(uri));
                                    //eventObject is now updated with eventId and eventPic URL
                                    //update the event in db
                                    //overwrite database document with new eventId and eventPic
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
                            });
                        }
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
