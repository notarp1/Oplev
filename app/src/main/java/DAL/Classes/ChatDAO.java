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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import DAL.Interfaces.IChatDAO;
import DTO.ChatDTO;

public class ChatDAO implements IChatDAO {
    public FirebaseFirestore db;

    public ChatDAO(){
        this.db = FirebaseFirestore.getInstance();
    }


    @Override
    public ChatDTO getChat(String chatId) {
        final ChatDTO[] defaultDTO = {new ChatDTO(null, null, null, null, null)};

        DocumentReference docRef = db.collection("chats").document(chatId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            private static final String TAG = "chat";

            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    assert document != null;
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        ChatDTO dto = document.toObject(ChatDTO.class);
                        defaultDTO[0] = dto;
                        System.out.println(defaultDTO[0].toString() + new Date());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });


        System.out.println(defaultDTO[0].toString() + new Date());
        return defaultDTO[0];
    }

    @Override
    public void createChat(ChatDTO chat) {
        Map<String, Object> chatObject = new HashMap<>();

        chatObject.put("sender", chat.getSender());
        chatObject.put("receiver", chat.getReceiver());
        chatObject.put("messages", chat.getMessages());
        chatObject.put("dates", chat.getDates());
        chatObject.put("chatId",null);


        db.collection("chats")
                .add(chatObject)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    private static final String TAG = "chat";

                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        db.collection("chats").document(documentReference.getId()).update("chatId", documentReference.getId());
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
    public void updateChat(FirestoreCallback firestoreCallback, ChatDTO chat) {
        Map<String, Object> chatObject = new HashMap<>();

        chatObject.put("sender", chat.getSender());
        chatObject.put("receiver", chat.getReceiver());
        chatObject.put("messages", chat.getMessages());
        chatObject.put("dates", chat.getDates());
        chatObject.put("chatId",chat.getChatId());


        db.collection("chats").document(chat.getChatId())
                .set(chatObject)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        firestoreCallback.onCallback(chat);
                        Log.d("Update","Updated chat");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        firestoreCallback.onCallback(new ChatDTO(null,null,null,null,null));
                        Log.d("Update","Not updated chat");
                    }
                });
    }

    @Override
    public void deleteChat(FirestoreCallback firestoreCallback, String chatId) {
        DocumentReference docRef = db.collection("chats").document(chatId);
        docRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                firestoreCallback.onCallback(new ChatDTO(null,null,chatId,null,null));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                firestoreCallback.onCallback(new ChatDTO(null,null,null,null,null));
            }
        });
    }

    public void readChat(FirestoreCallback firestoreCallback, String chatId){

        DocumentReference docRef = db.collection("chats").document(chatId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            private static final String TAG = "chat";

            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    assert document != null;
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        ChatDTO dto = document.toObject(ChatDTO.class);
                        firestoreCallback.onCallback(dto);
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }


    public interface FirestoreCallback {
        void onCallback(ChatDTO dto);
    }

}
