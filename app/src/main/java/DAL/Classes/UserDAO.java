package DAL.Classes;

import android.util.Log;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Controller.UserController;
import DAL.Interfaces.CallbackEvent;
import DAL.Interfaces.CallbackUser;
import DAL.Interfaces.IUserDAO;
import DTO.EventDTO;
import DTO.UserDTO;

public class UserDAO implements IUserDAO, CallbackUser {

    public FirebaseFirestore db;
    private static final String TAG = "userLog" ;


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
        userObject.put("joinedEvents", user.getJoinedEvents());
        userObject.put("events", new ArrayList<String>());
        userObject.put("pictures", user.getPictures());
        userObject.put("chatId", new ArrayList<>());
        userObject.put("userId", user.getUserId());
        userObject.put("userPicture", "https://firebasestorage.googleapis.com/v0/b/opleva4.appspot.com/o/question.png?alt=media&token=9dea34be-a183-4b37-bfb7-afd7a9db81f2");


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






    @Override
    public void deleteUser(UserDTO user) {

        ArrayList<String> events = user.getEvents();
        ArrayList<String> pictures = user.getPictures();
        EventDAO eventDAO = new EventDAO();

        for(int i = 0; i<6; i++){
            if(pictures.get(i) != null){
                String location = pictures.get(i);
                deleteFromStorage(location);
            }
            i++;
        }

        for(int i = 0; i < events.size(); i++){
            String location = events.get(i);

            eventDAO.getEvent(new CallbackEvent() {
                @Override
                public void onCallback(EventDTO event) {
                    String location = event.getEventPic();
                    deleteFromStorage(location);
                }
            }, location);

            i++;
        }






    }

    public void deleteFromStorage(String location){

        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(location);
        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // File deleted successfully
                Log.d(TAG, "onSuccess: deleted file");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Uh-oh, an error occurred!
                Log.d(TAG, "onFailure: did not delete file");
            }
        });

    }

    @Override
    public void onCallback(UserDTO user) {

    }
}
