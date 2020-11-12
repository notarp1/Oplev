package DAL.Classes;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.common.api.Batch;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import DAL.DBAccess;
import DAL.Interfaces.IChatDAO;
import DTO.ChatDTO;

public class ChatDAO implements IChatDAO {
    DBAccess dbAccess;
    public FirebaseFirestore db;

    public ChatDAO(){
        this.db = FirebaseFirestore.getInstance();
    }


    @Override
    public ChatDTO getChat(String chatId) {
        final ChatDTO[] dto = new ChatDTO[1];

        DocumentReference docRef = db.collection("chats").document(chatId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            private static final String TAG = "chat";

            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        dto[0] = document.toObject(ChatDTO.class);
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
        return dto[0];
    }

    @Override
    public void createChat(ChatDTO chat) {
        Map<String, Object> chatObject = new HashMap<>();

        chatObject.put("afsender", chat.getSender());
        chatObject.put("modtager", chat.getReceiver());
        chatObject.put("besked", chat.getMessages());
        chatObject.put("dato", chat.getDates());


        db.collection("chats")
                .add(chatObject)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    private static final String TAG = "chat";

                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        db.collection("chat").document(documentReference.getId()).update("chatId", documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    private static final String TAG = "chatFail";

                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    @Override
    public void updateChat(ChatDTO chat) {

    }

    @Override
    public void deleteChat(String chatId) {

    }
}
