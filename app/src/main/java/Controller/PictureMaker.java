package Controller;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;

import androidx.core.content.FileProvider;

import com.A4.oplev.BuildConfig;

import java.io.ByteArrayOutputStream;
import java.io.File;

import static androidx.core.app.ActivityCompat.startActivityForResult;

public class PictureMaker {
    private static PictureMaker instance = null;
    private static int RESULT_LOAD_IMAGE = 1;

    // Vi laver en singleton for at kunne uploade eller tage billeder
    private PictureMaker(){
        instance = this;
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



}
