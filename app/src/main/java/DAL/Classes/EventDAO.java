package DAL.Classes;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import DAL.Interfaces.CallbackEvent;
import DAL.Interfaces.CallbackUser;
import DAL.Interfaces.IEventDAO;
import DTO.EventDTO;
import DTO.UserDTO;

public class EventDAO implements IEventDAO {
    public FirebaseFirestore db;
    String TAG = "eventDAO";
    public EventDAO()
    {
        this.db = FirebaseFirestore.getInstance();
    }

    public void getEvent( String eventId) {
        DocumentReference docRef = db.collection("events").document(eventId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    @Override
    public EventDTO getEvent(int eventId) {
        return null;
    }

    @Override
    public void createEvent(EventDTO event) {

    }

    @Override
    public void updateEvent(EventDTO event) {

    }

    @Override
    public void deleteEvent(int eventId) {

    }


}
