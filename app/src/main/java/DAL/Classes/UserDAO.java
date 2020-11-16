package DAL.Classes;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import DAL.Interfaces.CallbackUser;
import DAL.Interfaces.IUserDAO;
import DTO.UserDTO;

public class UserDAO implements IUserDAO, CallbackUser {

    public FirebaseFirestore db;
    private static final String TAG = "userLog" ;


    public UserDAO(){
        this.db = FirebaseFirestore.getInstance();
    }



    @Override
    public void getUser(CallbackUser callbackUser, String userId) {
        DocumentReference docRef = db.collection("users").document(userId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                DocumentSnapshot documentSnapshot = task.getResult();
                if(documentSnapshot != null){
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
        userObject.put("phone", user.getPhone());
        userObject.put("joinedEvents", user.getJoinedEvents());
        userObject.put("events", user.getEvents());
        userObject.put("pictures", user.getPictures());


        db.collection("users")
                .add(userObject)
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    db.collection("users").document(documentReference.getId()).update("userId", documentReference.getId());
                })
                .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));
    }

    @Override
    public void updateUser(UserDTO user) {

    }

    @Override
    public void deleteUser(String userId) {

    }


    @Override
    public void onCallback(UserDTO user) {

    }
}
