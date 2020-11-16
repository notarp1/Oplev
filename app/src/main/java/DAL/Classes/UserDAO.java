package DAL.Classes;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;
import com.google.firebase.firestore.auth.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import DAL.DBAccess;
import DAL.Interfaces.IUserDAO;
import DTO.UserDTO;

public class UserDAO implements IUserDAO {

    public FirebaseFirestore db;
    private static final String TAG = "userLog" ;
    String userId;
    UserDTO test;
    static boolean pass = false;


    public UserDAO(){
        this.db = FirebaseFirestore.getInstance();
    }


    @Override
    public UserDTO getUser(String userId) {

        this.userId = userId;


         readData(new MyCallback() {
            @Override
            public void onCallback(UserDTO user) {
                System.out.println("UserName = " + user.getfName());
                Log.d(TAG, user.toString());
            }
        });

        //I need to be able to return the user object here, so I can use it in my Controller!
        return null;
    }

    public void readData(MyCallback myCallback) {
        DocumentReference docRef = db.collection("users").document(userId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                DocumentSnapshot documentSnapshot = task.getResult();
                if(documentSnapshot != null){

                    UserDTO user = documentSnapshot.toObject(UserDTO.class);
                    System.out.println("hej " + user.getfName());
                    myCallback.onCallback(user);
                }


            }
        });
    }

    public interface MyCallback {
        void onCallback(UserDTO user);
    }



    @Override
    public void createUser(UserDTO user) {

        Map<String, Object> userObject = new HashMap<>();

        userObject.put("fName", user.getfName());
        userObject.put("lName", user.getlName());
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




}
