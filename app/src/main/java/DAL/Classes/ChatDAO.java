package DAL.Classes;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
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
        final ChatDTO[] defaultDTO = {new ChatDTO(null, null, null, null, null, null)};

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
        ArrayList<String> tempPics = new ArrayList<>();
        for (int i = 0; i < chat.getPictures().size(); i++) {
            tempPics.add(i,chat.getPictures().get(i).toString());
        }

        chatObject.put("sender", chat.getSender());
        chatObject.put("receiver", chat.getReceiver());
        chatObject.put("messages", chat.getMessages());
        chatObject.put("dates", chat.getDates());
        chatObject.put("chatId",null);
        chatObject.put("pictures",tempPics);


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
        ArrayList<String> tempPics = new ArrayList<>();
        for (int i = 0; i < chat.getPictures().size(); i++) {
            tempPics.add(i,chat.getPictures().get(i).toString());
        }

        chatObject.put("sender", chat.getSender());
        chatObject.put("receiver", chat.getReceiver());
        chatObject.put("messages", chat.getMessages());
        chatObject.put("dates", chat.getDates());
        chatObject.put("chatId",chat.getChatId());
        chatObject.put("pictures",tempPics);


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
                        firestoreCallback.onCallback(new ChatDTO(null,null,null,null,null, null));
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
                firestoreCallback.onCallback(new ChatDTO(null,null,chatId,null,null, null));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                firestoreCallback.onCallback(new ChatDTO(null,null,null,null,null, null));
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
                        if (dto.getChatId() == null){
                            dto = new ChatDTO(new ArrayList<>(), new ArrayList<>(), chatId, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
                            firestoreCallback.onCallback(dto);
                        }
                        else {
                            firestoreCallback.onCallback(dto);
                        }
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    public void uploadFile(Bitmap bitmap, FirestoreCallbackPic firestoreCallbackPic, String chat_id) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference mountainImagesRef = storageRef.child("images/" + chat_id + new Date().getDate() + ".jpg");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = mountainImagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                mountainImagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        System.out.println(uri);
                        Log.d("downloadUrl-->", "" + uri);
                        firestoreCallbackPic.onCallBackPic(uri);
                    }
                });
            }
        });
    }


    public interface FirestoreCallback {
        void onCallback(ChatDTO dto);
    }

    public interface FirestoreCallbackPic{
        void onCallBackPic(Uri url);
    }


}
