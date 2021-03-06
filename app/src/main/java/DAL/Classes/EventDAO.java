package DAL.Classes;

import android.content.SharedPreferences;
import android.location.Location;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.A4.oplev.GpsTracker;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Controller.EventController;
import Controller.UserController;
import DAL.Interfaces.CallBackEventList;
import DAL.Interfaces.CallBackList;
import DAL.Interfaces.CallbackEvent;
import DAL.Interfaces.CallbackUser;
import DAL.Interfaces.IEventDAO;
import DTO.EventDTO;
import DTO.UserDTO;

public class EventDAO implements IEventDAO {
    FirebaseFirestore db;
    private final String TAG = "eventLog";
    private UserController userController;
    private String collectionPath = "events";
    private StorageReference mStorageRef;
    private StorageReference picRef;

    public EventDAO() {
        this.db = FirebaseFirestore.getInstance();
    }

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

    public void getEventIDs(CallBackList callBackList, SharedPreferences prefs) {
        CollectionReference docRef = db.collection(collectionPath);
        List<String> completeList = new ArrayList<>();
        List<String> types = new ArrayList<>();

        if (prefs.getBoolean("motionSwitch", true)) {
            types.add("Motion");
        }
        if (prefs.getBoolean("kulturSwitch", true)) {
            types.add("Kultur");
        }
        if (prefs.getBoolean("underholdningSwitch", true)) {
            types.add("Underholdning");
        }
        if (prefs.getBoolean("madDrikkeSwitch", true)) {
            types.add("Mad og Drikke");
        }
        if (prefs.getBoolean("musikNattelivSwitch", true)) {
            types.add("Musik og Natteliv");
        }
        if (prefs.getBoolean("gratisSwitch", true)) {
            types.add("Gratis");
        }
        if (prefs.getBoolean("blivKlogereSwitch", true)) {
            types.add("Bliv klogere");
        }


        if(!types.isEmpty()){
        docRef.whereIn("type", types).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    //setup location of phone/user
                    Location phoneLocation = new Location("LocationA");
                    phoneLocation.setLatitude(Double.parseDouble(prefs.getString("gpsLat", "0")));
                    phoneLocation.setLongitude(Double.parseDouble(prefs.getString("gpsLong", "0")));
                    Log.d(TAG, "onComplete: phonelocation: " + phoneLocation.toString());
                    int maxDistance = prefs.getInt("distance", 150);
                    Log.d(TAG, "onComplete: maxDistance (filter): " + maxDistance);
                    //setup age filter values
                    int filterMinAge = prefs.getInt("minAge", 18);
                    int filterMaxAge = prefs.getInt("maxAge", 99);
                    ArrayList<String> otherList = new ArrayList<>();
                    //for (QueryDocumentSnapshot document : task.getResult()) {
                    for(int i = 0; i < task.getResult().getDocuments().size(); i++){
                        DocumentSnapshot document = task.getResult().getDocuments().get(i);
                        //for each event
                        EventDTO dto = document.toObject(EventDTO.class);
                        if (UserController.getInstance().getCurrUser() != null) {
                            // if youre logged in
                            //setup needed current user values
                            String currentUserId = UserController.getInstance().getCurrUser().getUserId();
                            int currentUserAge = UserController.getInstance().getCurrUser().getAge();
                            if (!dto.getOwnerId().equals(currentUserId)
                                    && dto.getParticipant().equals("")
                                    && !dto.getApplicants().contains(currentUserId)
                                    && dto.getMinAge() <= currentUserAge
                                    && dto.getMaxAge() >= currentUserAge) {
                                //if youre not owner, no participant on event, you havent applied to event, you are within event age limits
                                //setup distance check
                                int distance = EventController.getInstance().calculateDistance(dto, prefs);
                                if (distance <= maxDistance) {
                                    //if within distancelimit
                                    //get owner of event and check his age is in between filters
                                    UserController.getInstance().getUser(new CallbackUser() {
                                        @Override
                                        public void onCallback(UserDTO user) {
                                            Log.d(TAG, "onCallback: (jbe) event filter, user recieved");
                                            if(filterMinAge <= user.getAge() && filterMaxAge >=user.getAge()){
                                                //if owner's age is in between age filters
                                                completeList.add(document.getId());
                                            }
                                            else{
                                                Log.d(TAG, "onCallback: (jbe) event filter: user age not good");
                                                otherList.add(document.getId());
                                            }
                                            Log.d(TAG, "onCallback:  (jbe) OLsize: " + otherList.size() +"cSize: " + completeList.size() + "taskSize " + task.getResult().getDocuments().size());
                                            if((otherList.size() + completeList.size()) == task.getResult().getDocuments().size()){
                                                Log.d(TAG, "onComplete: (jbe) eventlist complete");
                                                Collections.shuffle(completeList);
                                                callBackList.onCallback(completeList);
                                            }
                                        }
                                    }, dto.getOwnerId());
                                }
                                else{
                                    otherList.add(document.getId());
                                }
                            }
                            else{
                                otherList.add(document.getId());
                            }
                        }
                        else {
                            if (dto.getParticipant().equals("")) {
                                // not logged in
                                completeList.add(document.getId());
                            }
                            else{
                                otherList.add(document.getId());
                            }
                        }

                    }
                    if((otherList.size() + completeList.size()) == task.getResult().getDocuments().size()){
                        Log.d(TAG, "onComplete: (jbe) eventlist complete");
                        Collections.shuffle(completeList);
                        callBackList.onCallback(completeList);
                    }
                    else{
                        Log.d(TAG, "onComplete: (jbe) the count is off or waiting for callback");
                    }
                }
                else {
                    Log.d("switchFail", "Error getting motionswitch: ", task.getException());
                }

            }
        }); }
        else{ callBackList.onCallback(completeList); }
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
        eventObject.put("participant", "");
        eventObject.put("eventPic", event.getEventPic());
        eventObject.put("applicants", event.getApplicants());
        eventObject.put("type", event.getType());
        eventObject.put("coordinates", event.getCoordinates());


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
                        mStorageRef = FirebaseStorage.getInstance().getReference();

                        //if a picture to upload was chosen then upload pic else skip that part
                        if (picUri != null) {
                            //set picture reference (path where pic will be saved in db
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
                        } else {
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
        eventObject.put("coordinates", event.getCoordinates());
        eventObject.put("chatId", event.getChatId());
        Log.d(TAG, "updateEvent: (jbe) participant: " + event.getParticipant());

        db.collection("events").document(event.getEventId())
                .set(eventObject)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        UserController userController = UserController.getInstance();
                        userController.setSafe(true);
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        UserController userController = UserController.getInstance();
                        userController.setSafe(true);
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }

    @Override
    public void editEvent(EventDTO event, Uri newImageUri) {
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
        eventObject.put("coordinates", event.getCoordinates());

        if (newImageUri != null) {
            //if a new picture was chosen upon edit
            //set picture reference (path where pic will be saved in db
            mStorageRef = FirebaseStorage.getInstance().getReference();
            picRef = mStorageRef.child("events/" + event.getEventId() + "/1");
            //upload pic
            picRef.putFile(newImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get a URL to the uploaded content picture
                            picRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    //set URL of eventpic in eventObject map
                                    eventObject.put("eventPic", String.valueOf(uri));
                                    Log.d(TAG, "onSuccess: event url ");
                                    //eventObject is now updated with eventId and eventPic URL
                                    //update the event in db
                                    //overwrite database document with new eventId and link to pic
                                    db.collection("events").document(event.getEventId())
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
        else {
            //no new picture chosen
            db.collection("events").document(event.getEventId())
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
    }
    @Override
    public void deleteEvent(String eventId) {
        db.collection("events").document(eventId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });
    }
}
