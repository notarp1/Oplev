package Controller;

import android.net.Uri;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URL;

import DAL.Interfaces.CallBackURL;

public class pictureGet {
    public void getUrl(CallBackURL callBackURL, String id, Uri uri){
        StorageReference mStorageRef  = FirebaseStorage.getInstance().getReference();
        StorageReference picRef = mStorageRef.child("events/" + id + "/1");
        picRef.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content picture
                        picRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                //set URL of eventpic in eventObject map
                                callBackURL.onCallBack(uri.toString());
                            }
                        });
                    }
                });
    }

}
