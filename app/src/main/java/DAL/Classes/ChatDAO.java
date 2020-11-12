package DAL.Classes;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.common.api.Batch;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
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
    public IChatDAO getChat(int chatId) {
        return null;
    }

    @Override
    public void createChat(ChatDTO chat) {

        final String[] path = {"chats"};
        final boolean[] firstChat = {true};

        for (int i = 0; i < chat.getMessages().size(); i++) {
            Map<String, Object> chatObject = new HashMap<>();

            chatObject.put("afsender", chat.getSender());
            chatObject.put("modtager", chat.getReceiver());
            chatObject.put("besked", chat.getMessages().get(i));
            chatObject.put("dato", chat.getDates().get(i));


            db.collection(path[0])
                    .add(chatObject)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        private static final String TAG = "chat";

                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                            db.collection("chat").document(documentReference.getId()).update("chatId", documentReference.getId());
                            if (firstChat[0]){
                                path[0] = "chats/"+documentReference.getId();
                                firstChat[0] = false ;}
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
    }

    @Override
    public void updateChat(ChatDTO chat) {

    }

    @Override
    public void deleteChat(int chatId) {

    }
}
