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

    // Hvis man skal oprette en ny chat som skal være i firestore
    @Override
    public void createChat(ChatDTO chat) {
        // Først skal vi opbygge et chat objekt med de attributter der skal være i firestore
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


        // Dernæst vil vi adde objektet i vores chats collection
        db.collection("chats")
                .add(chatObject)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    private static final String TAG = "chat";

                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        // Hvis vi har succesfuldt lavet en ny chat så sætter vi dets chatid til det id dokumentet har givet det
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


    // Når vi sender en besked i vores chat bruges den her funktion
    @Override
    public void updateChat(FirestoreCallback firestoreCallback, ChatDTO chat) {
        // Vi mapper et objekt som skal sendes
        Map<String, Object> chatObject = new HashMap<>();
        ArrayList<String> tempPics = new ArrayList<>();
        // Hvis der findes nogle billeder i vores chat så kan man ikke sende URIs og vi skal derfor stringe dem
        for (int i = 0; i < chat.getPictures().size(); i++) {
            tempPics.add(i,chat.getPictures().get(i).toString());
        }

        chatObject.put("sender", chat.getSender());
        chatObject.put("receiver", chat.getReceiver());
        chatObject.put("messages", chat.getMessages());
        chatObject.put("dates", chat.getDates());
        chatObject.put("chatId",chat.getChatId());
        chatObject.put("pictures",tempPics);


        // Vi opdaterer en chat der eksisterer i forvejen og derfor bruger vi documentreference ved chatid'et hvor vi indsætter det chatobjekt vii har lavet
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


    // Hvis vi ønsker at slette en chat bruges denne funktion
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


    // Når det er vi skal indlæse en chat bruges denne funktion
    public void readChat(FirestoreCallback firestoreCallback, String chatId){

        // Først finder vi frem til dokumentet gennem chatId'et og sætter en completelistener på den
        DocumentReference docRef = db.collection("chats").document(chatId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            private static final String TAG = "chat";

            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                // Hvis vi har fundet dokumentet så checker vi først om det tasken er succesfuld og om det eksisterer
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    assert document != null;
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        // Vi laver et nyt ChatDTO objekt gennem resultatet fra tasken
                        ChatDTO dto = document.toObject(ChatDTO.class);
                        if (dto.getChatId() == null){
                            // Det her bliver redundant senere men ikke lige nu hvis man prøver at indlæse et helt tomt dokument
                            dto = new ChatDTO(new ArrayList<>(), new ArrayList<>(), chatId, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
                            firestoreCallback.onCallback(dto);
                        }
                        else {
                            // Vi kalder på oncallback der hvor man vil indlæse chatten
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

    // Den her funktion bruges lige foreløbigt til at uploade billeder til firebase storage
    public void uploadFile(Bitmap bitmap, FirestoreCallbackPic firestoreCallbackPic, String chat_id) {
        // Først instantierer vi nogle objekter der skal bruges
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference mountainImagesRef = storageRef.child("images/" + chat_id + new Date() + ".jpg");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = mountainImagesRef.putBytes(data);
        // Når vi prøver på at sende billedet/bitmappen så lytter vi efter fejl eller success
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // Hvis vi successfuldt har fået uploadet billedet til firestore storage så skal vi gerne gemme URI'en et sted (I vores tilfælde i ChatDTO'en)
                // Siden task'en for at få download URI'en er asynkron og vil crashe hvis man prøver på at hente den inden den er loadet færdig så laver vi en listener
                mountainImagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // Når vi kan hente URL'en så kalder vi callbackpic i den klasse der ville uploade et billede og giver den URI'en
                        System.out.println(uri);
                        Log.d("downloadUrl-->", "" + uri);
                        firestoreCallbackPic.onCallBackPic(uri);
                    }
                });
            }
        });
    }


    // Interface for callback hvor man skal have en ChatDTO eller URI
    public interface FirestoreCallback {
        void onCallback(ChatDTO dto);
    }

    public interface FirestoreCallbackPic{
        void onCallBackPic(Uri url);
    }


}
