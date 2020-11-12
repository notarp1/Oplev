package DAL.Classes;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import DAL.DBAccess;
import DAL.Interfaces.IUserDAO;
import DTO.UserDTO;

public class UserDAO implements IUserDAO {

    DBAccess dbAccess;
    public FirebaseFirestore db;
    public UserDAO(){

        this.db = FirebaseFirestore.getInstance();
    }

    @Override
    public UserDTO getUser(int userId) {
        return null;
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
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    private static final String TAG = "user" ;

                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        db.collection("users").document(documentReference.getId()).update("userId", documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    private static final String TAG = "userFail";

                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    @Override
    public void updateUser(UserDTO user) {

    }

    @Override
    public void deleteUser(int userId) {

    }
}
