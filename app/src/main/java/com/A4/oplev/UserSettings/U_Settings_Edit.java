package com.A4.oplev.UserSettings;

import android.Manifest;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.A4.oplev.PicassoFunc;
import com.A4.oplev.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import Controller.Controller;

import static android.app.Activity.RESULT_OK;


public class U_Settings_Edit extends Fragment implements View.OnClickListener {

    public EditText about, city, job, education;
    TextView textview;
    public ImageView accept, back, p0, p1, p2, p3, p4, p5;

    Controller controller;
    Bitmap stockphotoBit;

    static public boolean[] picBoolean = new boolean[]{false, false, false, false, false, false};

    private Uri[] uris;
    private ArrayList<String> pictures;
    private int pictureCount;

    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1000;
    int picNumber;
    int indexNumbers = 0;
    int indexPlace;

    private StorageReference mStorageRef;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    StorageReference picRef;

    Context ctx;



    public View onCreateView(LayoutInflater i, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View root = i.inflate(R.layout.u_setting_edit_frag, container, false);

        this.ctx = getContext();

        uris = new Uri[]{null, null, null, null, null, null};
        controller = Controller.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        pictures = controller.getUserPictures();


        mStorageRef = FirebaseStorage.getInstance().getReference();



        //Gem stockphoto bitmap
        stockphotoBit = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.uploadpng);

        textview = (TextView) getActivity().findViewById(R.id.topbar_text);
        textview.setText("Rediger Profil");

        accept = (ImageView) getActivity().findViewById(R.id.imageView_checkmark);
        accept.setVisibility(View.VISIBLE);
        accept.setOnClickListener(this);

        back = (ImageView) getActivity().findViewById(R.id.topbar_arrow);
        back.setOnClickListener(this);


        about = root.findViewById(R.id.editText_description);
        city = root.findViewById(R.id.editText_city);
        job = root.findViewById(R.id.editText_job);
        education = root.findViewById(R.id.editText_edu);

        iniPictures(root);
        controller.iniEditProfile(this);


        return root;


    }

    @Override
    public void onStart() {
        super.onStart();
        ImageView avatar;

        for(int i = 0; i<6; i++){
            if(pictures.get(i) != null){
                avatar = getPictureNumber(i);
                Picasso.get().load(pictures.get(i)).into(avatar);
            }
        }

    }

    private void iniPictures(View root) {
        p0 = root.findViewById(R.id.pb0);
        p1 = root.findViewById(R.id.pb1);
        p2 = root.findViewById(R.id.pb2);
        p3 = root.findViewById(R.id.pb3);
        p4 = root.findViewById(R.id.pb4);
        p5 = root.findViewById(R.id.pb5);


        p0.setOnClickListener(this);
        p1.setOnClickListener(this);
        p2.setOnClickListener(this);
        p3.setOnClickListener(this);
        p4.setOnClickListener(this);
        p5.setOnClickListener(this);
    }




    @Override
    public void onClick(View v) {

        if(v == back){
            accept.setVisibility(View.INVISIBLE);
            getActivity().getSupportFragmentManager().popBackStack();
        } else if (v == accept) {
            updateUserAndGUI();
        } else if (v==p0){
            picBool(0);
        } else if (v == p1) {
            picBool(1);
        } else if (v == p2) {
            picBool(2);
        } else if (v == p3) {
            picBool(3);
        } else if (v == p4) {
            picBool(4);
        } else if (v == p5) {
            picBool(5);
        }

    }

    private void onAccept() {

        if(pictureCount > 0 )
        for (int i = 0; i < 6; i++) {

            if (uris[i] != null) {
                indexPlace = i;

                Uri file = uris[i];
                picRef = mStorageRef.child("users/" + currentUser.getUid() + "/" + i);
                picRef.putFile(file)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // Get a URL to the uploaded content

                                picRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Uri downloadUrl = uri;
                                        setPictures(indexPlace, downloadUrl);
                                        updateUserAndGUI();
                                    }
                                });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                                // ...
                            }
                        });

            }


        } else updateUserAndGUI();
    }


    private void updateUserAndGUI() {

        controller.updateUser(this, pictures);
        Toast.makeText(getContext(), "Profil opdateret!", Toast.LENGTH_SHORT).show();
    }

    private ArrayList<String> getPictures(){
       pictures = controller.getUserPictures();
       return pictures;
    }

    private void setPictures(int index, Uri uri){

        String address = String.valueOf(uri);

        pictures.set(index, address);
    }
    private void picBool(int number) {
        picNumber = number;
        picturePermission();

    }


    private void picturePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                //Permission not granted request it
                String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                //Show popup for runtime permissions
                requestPermissions(permissions, PERMISSION_CODE);
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

    private void pickImageFromGallery(){
        //Intent to pick image
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_CODE: {
                if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //Permissions was granted
                    pickImageFromGallery();
                }
                else {
                    //Permission was denis
                    Toast.makeText(getContext(), "Your Permission is needed to get access the camera", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK && requestCode== IMAGE_PICK_CODE){
            ImageView picture = getPictureNumber(picNumber);
            Uri uri = data.getData();
            pictures.set(picNumber, String.valueOf(uri));
            uris[picNumber] = uri;
            picture.setImageURI(uri);

            onAccept();

        }
    }

    private ImageView getPictureNumber(int picNumber){
        ImageView returnPic = p0;
        switch (picNumber){
            case 0:
                returnPic = p0;
                break;
            case 1:
                returnPic = p1;
                break;
            case 2:
                returnPic = p2;
                break;
            case 3:
                returnPic = p3;
                break;
            case 4:
                returnPic = p4;
                break;
            case 5:
                returnPic = p5;
                break;
        }
        return  returnPic;
    }






}


