package Controller;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.A4.oplev.BuildConfig;
import com.A4.oplev.UserSettings.U_Settings_Edit;
import com.google.firebase.firestore.auth.User;

import java.io.File;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
import static androidx.core.app.ActivityCompat.startActivityForResult;

public class PictureMaker {
    private static PictureMaker instance = null;
    UserController userController;
    private static int RESULT_LOAD_IMAGE = 1;
    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1000;
    U_Settings_Edit ctx;
    private Uri[] uris;

    // Vi laver en singleton for at kunne uploade eller tage billeder
    private PictureMaker(){
        this.instance = this;
        userController = UserController.getInstance();
    }

    public static PictureMaker getInstance(){
        if (instance == null) new PictureMaker();
        return instance;
    }

    // Hvis man har et billedet liggende på telefonen bruges denne funktion
    public void uploadPic(Activity activity){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(activity,photoPickerIntent, RESULT_LOAD_IMAGE, null);
    }

    // Hvis man vil tage et nyt billede og sende det videre bruges denne funktion
    public void takePic(Activity activity){
        Intent imageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File imagesFolder = new File(Environment.getExternalStorageDirectory(), "MyImages");
        imagesFolder.mkdirs(); // <----
        File image = new File(imagesFolder, "image_001.jpg");
        Uri uri = FileProvider.getUriForFile(activity, BuildConfig.APPLICATION_ID + ".provider",image);
        imageIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        imageIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(activity,imageIntent,0, null);

    }

    public void picBool(int number, U_Settings_Edit ctx, Uri[] uris) {
        ctx.picNumber = number;
        this.uris = uris;
        this.ctx = ctx;
        picturePermission();

    }

    private void picturePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ctx.getContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                //Permission not granted request it
                String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                //Show popup for runtime permissions
                ctx.requestPermissions(permissions, PERMISSION_CODE);
            }
            else {
                //Permission already granted
                pickImageFromGallery();
            }
        }
        else {
            //System os is less than marshmellow
            pickImageFromGallery();
        }
    }



    public void onAccept() {
        userController.uploadPicture(ctx, uris);
    }

    public void setUris(Uri[] uris) {
        this.uris = uris;
    }

    public Uri[] getUris() {
        return uris;
    }

    public void pickImageFromGallery(){
        //Intent to pick image
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        ctx.startActivityForResult(intent, IMAGE_PICK_CODE);
    }



}
