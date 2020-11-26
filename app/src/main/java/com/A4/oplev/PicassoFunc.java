package com.A4.oplev;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import Controller.UserController;

public class PicassoFunc {
    private static PicassoFunc instance = null;
    UserController userController;
    ArrayList<String> pictures;

    private PicassoFunc(){

        this.instance = this;

    }
    public static PicassoFunc getInstance(){
        if (instance == null) instance = new PicassoFunc();
        return instance;
    }


    Target picassoImageTarget(Context context, final String imageDir, final String imageName) {
        Log.d("picassoImageTarget", " picassoImageTarget");
        ContextWrapper cw = new ContextWrapper(context);


        final File directory = cw.getDir(imageDir, Context.MODE_PRIVATE); // path to /data/data/yourapp/app_imageDir
        return new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap,com.squareup.picasso.Picasso.LoadedFrom from) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final File myImageFile = new File(directory, imageName); // Create image file
                        FileOutputStream fos = null;
                        try {
                            fos = new FileOutputStream(myImageFile);
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                            System.out.println("TEST123");
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                fos.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        Log.i("image", "image saved to >>>" + myImageFile.getAbsolutePath());

                    }
                }).start();
            }



            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                if (placeHolderDrawable != null) {}
            }
        };
    }


    public void deleteUserPictures(Context ctx){
        for(int i=0; i<6; i++) {
            ContextWrapper cw = new ContextWrapper(ctx.getApplicationContext());
            File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
            File myImageFile = new File(directory, "ppic"+i+".png");
            if (myImageFile.delete()) System.out.println("image on the disk deleted successfully!");
        }

    }

    public void getUserPictures(Context ctx) {
        userController = userController.getInstance();

        try {
            pictures = userController.getUserPictures();
        }catch (Exception e){
        FirebaseAuth.getInstance().signOut();
        PreferenceManager.getDefaultSharedPreferences(ctx).edit().clear().apply();
        }


        for(int i = 0; i<pictures.size(); i++){

            if(pictures.get(i) != null){
                Picasso.get().load(pictures.get(i)).into(picassoImageTarget(ctx.getApplicationContext(), "imageDir", "ppic"+i+".png"));
            }

        }

    }
}
