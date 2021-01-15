package DAL.Classes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.NonNull;

import com.A4.oplev.Activity_Ini;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Controller.UserController;
import DAL.Interfaces.CallbackEvent;
import DAL.Interfaces.CallbackUser;
import DAL.Interfaces.CallbackUserDelete;
import DAL.Interfaces.IUserDAO;
import DTO.ChatDTO;
import DTO.EventDTO;
import DTO.UserDTO;

public class UserDAO implements IUserDAO, CallbackUser, CallbackUserDelete {

    public FirebaseFirestore db;
    private static final String TAG = "userLog" ;
    private boolean wait = false;
    private  ArrayList<String> requested = new ArrayList<>();
    String userIdremove;


    public UserDAO(){
        this.db = FirebaseFirestore.getInstance();
    }


    @Override
    public void getUser(CallbackUser callbackUser, String userId) {
        System.out.println("userId = " + userId);
         DocumentReference docRef = db.collection("users").document(userId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                DocumentSnapshot documentSnapshot = task.getResult();
                if(documentSnapshot != null){
                    System.out.println(documentSnapshot.getData());
                    UserDTO user = documentSnapshot.toObject(UserDTO.class);
                    callbackUser.onCallback(user);
                }
            }
        });
    }


    @Override
    public void createUser(UserDTO user) {

        Map<String, Object> userObject = new HashMap<>();

        userObject.put("fName", user.getfName());
        userObject.put("lName", user.getlName());
        userObject.put("job", user.getJob());
        userObject.put("education", user.getEducation());
        userObject.put("age", user.getAge());
        userObject.put("city", user.getCity());
        userObject.put("description", user.getDescription());
        userObject.put("email", user.getEmail());
        userObject.put("requestedEvents", user.getRequestedEvents());
        userObject.put("events", new ArrayList<String>());
        userObject.put("pictures", user.getPictures());
        userObject.put("chatId", new ArrayList<>());
        userObject.put("userId", user.getUserId());
        userObject.put("userPicture", "https://firebasestorage.googleapis.com/v0/b/opleva4.appspot.com/o/question.png?alt=media&token=9dea34be-a183-4b37-bfb7-afd7a9db81f2");
        userObject.put("likedeEvents",new ArrayList<>());


        db.collection("users")
                .document(user.getUserId())
                .set(userObject)
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference);

                })
                .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));
    }

    @Override
    public void updateUser(UserDTO user) {

        ObjectMapper oMapper = new ObjectMapper();
        Map<String, Object> userObject = oMapper.convertValue(user, Map.class);
        //System.out.println(user.getChatId().get(0));

        db.collection("users").document(user.getUserId())
                .set(userObject)
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


    public void setRequested(ArrayList<String> requested) {
        this.requested = requested;
    }

    public ArrayList<String> getRequested() {
        return requested;
    }

    @Override
    public void deleteUser(UserDTO user, Context ctx) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        ArrayList<String> events = user.getEvents();

        ArrayList<String> chats = user.getChatId();

        ChatDAO chatDAO = new ChatDAO();
        UserController userController = UserController.getInstance();
        UserDTO userDTO = userController.getCurrUser();
        ArrayList<String> userRequests = userDTO.getRequestedEvents();
        setRequested(userRequests);
        userIdremove = userDTO.getUserId();

        deleteRefs(new CallbackUserDelete() {
            @Override
                public void onCallbackDelete() {
                deleteUserOnline(new CallbackUserDelete() {
                        @Override
                        public void onCallbackDelete() {
                            deleterUserLocal(new CallbackUserDelete() {
                                @Override
                                public void onCallbackDelete() {

                                    System.out.println("hej med dig");
                                    loopThing(new CallbackUserDelete() {
                                        @Override
                                        public void onCallbackDelete() {

                                            removeLastApps(new CallbackUserDelete() {
                                                @Override
                                                public void onCallbackDelete() {
                                                   /* Intent i = new Intent(ctx, Activity_Ini.class);
                                                    ctx.startActivity(i);*/
                                                }
                                            });




                                        }
                                    }, chats, chatDAO, user);
                                }
                            }, user, db);
                        }
                    }, ctx);
                }
            }, events, chats);

    }

    private void removeLastApps(CallbackUserDelete delete){
        EventDAO eventDAO = new EventDAO();
        for(int m = 0; m<requested.size(); m++){
            eventDAO.getEvent(new CallbackEvent() {
                @Override
                public void onCallback(EventDTO event) {


                    ArrayList<String> apps = event.getApplicants();

                    for(int i = 0; i<apps.size(); i++){
                        if(apps.get(i).equals(userIdremove)){
                            apps.remove(i);

                        }
                    }

                    event.setApplicants(apps);
                    eventDAO.updateEvent(event);
                }
            }, requested.get(m));
        }
    }
     private void loopThing(CallbackUserDelete delete,  ArrayList<String> chats, ChatDAO chatDAO, UserDTO user){

        ArrayList<String> newChat = new ArrayList<>();
        ArrayList<String> userVisited = new ArrayList<>();
        UserDTO userTest = new UserDTO();
        for(int i = 0; i<chats.size(); i++){
            newChat.add(chats.get(i));
        }

        for(int i = 0; i<newChat.size(); i++){
            System.out.println("HAHA: " + newChat.get(i));;
        }
        for (int i = 0; i < chats.size(); i++) {


            int finalI = i;
            chatDAO.readChat(new ChatDAO.FirestoreCallback() {
                @Override
                public void onCallback(ChatDTO dto) {
                    String userId;
                    if(dto.getUser1ID().equals(user.getUserId())){
                        userId = dto.getUser2ID();
                    } else userId = dto.getUser1ID();

                    String eventId = dto.getEventID();

                    deleteParticipants(new CallbackUserDelete() {
                        @Override
                        public void onCallbackDelete() {

                            if(userVisited.contains(userId)){
                                System.out.println("suckadicxk");
                            } else {

                                userVisited.add(userId);

                                getUser(new CallbackUser() {
                                    @Override
                                    public void onCallback(UserDTO user1) {

                                        //Henter chatIds
                                        ArrayList<String> uChats = user1.getChatId();

                                        //Finder chatID
                                        for(int i = 0; i<newChat.size(); i++){

                                            //chat 1 -> new chat
                                            for(int j = 0; j < uChats.size(); j++){

                                                if(uChats.get(j).equals(newChat.get(i))){
                                                    //hvis chat1 ->
                                                    //fjerneer corresponding ID

                                                    db.collection("chats").document(uChats.get(j))
                                                            .delete()
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    System.out.println("CHAT DELETED");

                                                                }
                                                            }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            System.out.println(e + "HAHA");
                                                        }
                                                    });
                                                    uChats.remove(j);

                                                    user1.setChatId(uChats);



                                                }
                                            }
                                        }
                                        updateOtherUser(new CallbackUserDelete() {
                                            @Override
                                            public void onCallbackDelete() {

                                            }
                                        }, user1);

                                    }
                                }, userId);
                            }

                        }
                    }, eventId, user.getUserId());

                }
            }, chats.get(i));
        }
        delete.onCallbackDelete();
    }

    private void updateOtherUser(CallbackUserDelete delete, UserDTO user){
        updateUser(user);
        delete.onCallbackDelete();
    }

    private void deleteParticipants(CallbackUserDelete delete, String eventId, String userId){

        EventDAO eventDAO = new EventDAO();


        ArrayList<String> userRequests = getRequested();

        eventDAO.getEvent(new CallbackEvent() {
            @Override
            public void onCallback(EventDTO event) {

                String participant = event.getParticipant();
                ArrayList<String> applicants = event.getApplicants();
                System.out.println(participant + "HAHA");
                event.setParticipant("");

                for(int i = 0; i<userRequests.size(); i++){

                    if(applicants.contains(userRequests.get(i))){

                        userRequests.remove(i);
                    }

                }
                for(int i = 0; i<applicants.size(); i++){
                    if(applicants.get(i).equals(userId)){
                        applicants.remove(i);

                    }
                }


                setRequested(userRequests);
                eventDAO.updateEvent(event);
                delete.onCallbackDelete();
            }
        }, eventId);


    }
    private void deleterUserLocal(CallbackUserDelete delete, UserDTO user, FirebaseFirestore db){


        db.collection("users").document(user.getUserId())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        System.out.println("slettet");
                        delete.onCallbackDelete();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("IkkeSlettetLokal");

                    }
                });




    }

    private void deleteUserOnline(CallbackUserDelete delete, Context ctx){

        /*SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        FirebaseUser currUser  = FirebaseAuth.getInstance().getCurrentUser() ;
        String username = prefs.getString("username", "username");
        String password = prefs.getString("password", "password");

        System.out.println(username);
        System.out.println(password);
        AuthCredential credential = EmailAuthProvider
                .getCredential(username, password);

        currUser.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        currUser.delete()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            System.out.println("UserSucces");
                                            delete.onCallbackDelete();
                                        }
                                    }
                                })   .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                System.out.println("IkkeSlettetOnline");
                                System.out.println(e);
                            }
                        });
                    }
                }); */
        delete.onCallbackDelete();

    }

    private void deleteRefs(CallbackUserDelete delete, ArrayList<String> events, ArrayList<String> chats){

      FirebaseFirestore db = FirebaseFirestore.getInstance();

        WriteBatch writeBatch = db.batch();
        System.out.println("NONNA");

        if(events.size() != 0) {
            for (int i = 0; i < events.size(); i++) {
                DocumentReference documentReference = db.collection("events").document(events.get(i));
                writeBatch.delete(documentReference);
                System.out.println("EVENTS");
            }
        }

        /*
        if(chats.size() != 0) {
            for (int i = 0; i < chats.size(); i++) {

                DocumentReference documentReference = db.collection("chats").document(chats.get(i));
                writeBatch.delete(documentReference);
                System.out.println("CHATS");
            }
        } */

        writeBatch.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                System.out.println("VIRKER");
                delete.onCallbackDelete();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("WHHAW");
            }
        });

    }



    public void setWait(boolean wait) {
        this.wait = wait;
    }

    public boolean isWait() {
        return wait;
    }

    @Override
    public void onCallback(UserDTO user) {

    }


    @Override
    public void onCallbackDelete() {

    }
}

