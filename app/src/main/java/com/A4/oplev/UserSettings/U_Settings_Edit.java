package com.A4.oplev.UserSettings;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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

import com.A4.oplev.Activity_Profile;
import com.A4.oplev.R;
import java.util.ArrayList;
import Controller.UserController;

import static android.app.Activity.RESULT_OK;


public class U_Settings_Edit extends Fragment implements View.OnClickListener, View.OnLongClickListener {

    public EditText about, city, job, education;
    TextView textview;
    public ImageView accept, back, p0, p1, p2, p3, p4, p5;

    UserController userController;
    Bitmap stockphotoBit;


    private Uri[] uris;
    private ArrayList<String> pictures;


    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1000;
    int picNumber;


    Context ctx;



    public View onCreateView(LayoutInflater i, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View root = i.inflate(R.layout.u_setting_edit_frag, container, false);

        userController = userController.getInstance();


        pictures = userController.getUserPictures();

        this.ctx = getContext();
        uris = new Uri[]{null, null, null, null, null, null};

        //Gem stockphoto bitmap
        stockphotoBit = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.uploadpic);

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
        userController.iniEditProfile(this);



        return root;


    }

    @Override
    public void onStart() {
        super.onStart();
        userController.iniEdit(this);

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

        p0.setOnLongClickListener(this);
        p1.setOnLongClickListener(this);
        p2.setOnLongClickListener(this);
        p3.setOnLongClickListener(this);
        p4.setOnLongClickListener(this);
        p5.setOnLongClickListener(this);


    }



    @Override
    public void onClick(View v) {
        if(userController.isSafe()) {
            if (v == back) {
                accept.setVisibility(View.INVISIBLE);
                getActivity().finish();
            } else if (v == accept) {
                userController.updateUserAndGUI(this);
            } else if (v == p0) {
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
    }

    @Override
    public boolean onLongClick(View v) {
        if(userController.isSafe()) {
            if (v == p0) {
                userController.deletePicture(0, pictures, this, stockphotoBit);
                return true;
            } else if (v == p1) {
                userController.deletePicture(1, pictures, this, stockphotoBit);
                return true;
            } else if (v == p2) {
                userController.deletePicture(2, pictures, this, stockphotoBit);
                return true;
            } else if (v == p3) {
                userController.deletePicture(3, pictures, this, stockphotoBit);
                return true;
            } else if (v == p4) {
                userController.deletePicture(4, pictures, this, stockphotoBit);
                return true;
            } else if (v == p5) {
                userController.deletePicture(5, pictures, this, stockphotoBit);
                return true;
            }
        }
        return false;

    }



    private void onAccept() {
        userController.uploadPicture(this, uris);
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


