package com.A4.oplev.UserSettings;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.A4.oplev.BuildConfig;
import com.A4.oplev.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import Controller.Controller;

import static android.app.Activity.RESULT_OK;

public class U_Settings_Edit extends Fragment implements View.OnClickListener {

    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int REQUEST_CAMERARESULT = 201;
    TextView textview;
    ImageView accept, p0, p1, p2, p3, p4, p5;
    //ArrayList<String> pictures;
   // String bitHolder;
    //static String[] bitmapStringArray = new String[]{null, null, null, null, null, null};
    //static public boolean[] picBoolean = new boolean[] {false, false, false, false, false, false};


    public EditText about, city, job, education;
    Controller controller;
    Bitmap bitmap, stockphotoBit;



    public View onCreateView(LayoutInflater i, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View root = i.inflate(R.layout.u_setting_edit_frag, container, false);

        controller =  Controller.getInstance();

        textview = (TextView)getActivity().findViewById(R.id.topbar_text);
        textview.setText("Rediger Profil");

        accept = (ImageView) getActivity().findViewById(R.id.imageView_checkmark);
        accept.setVisibility(View.VISIBLE);
        accept.setOnClickListener(this);
        about = root.findViewById(R.id.editText_description);
        city = root.findViewById(R.id.editText_city);
        job = root.findViewById(R.id.editText_job);
        education = root.findViewById(R.id.editText_edu);

        stockphotoBit = BitmapFactory.decodeResource(getContext().getResources(),
                R.drawable.uploadpng);

        iniPictures(root);

        controller.iniEditProfile(this);



        return root;


    }

    private void iniPictures(View root) {
       // pictures = new ArrayList<String>();
       /* p0 = root.findViewById(R.id.pb1);
        p1 = root.findViewById(R.id.pb2);
        p2 = root.findViewById(R.id.pb3);
        p3 = root.findViewById(R.id.pb4);
        p4 = root.findViewById(R.id.pb5);
        p5 = root.findViewById(R.id.pb6);

        p0.setOnClickListener(this);
        p1.setOnClickListener(this);
        p2.setOnClickListener(this);
        p3.setOnClickListener(this);
        p4.setOnClickListener(this);
        p5.setOnClickListener(this); */
    }


    @Override
    public void onClick(View v) {

        // code block
        if(v == accept){

/*
            for(int i = 0; i<picBoolean.length; i++){
                if(picBoolean[i]){
                    pictures.add(bitmapStringArray[i]);
                }
            } */


            //pictures.add("dada");
            controller.updateUser(this);
            accept.setVisibility(View.INVISIBLE);
            getActivity().getSupportFragmentManager().popBackStack();

        } /*else if (p0.equals(v)) {
            picBool(0, p0);
        } else if (p1.equals(v)) {
            picBool(1, p1);
        } else if (p2.equals(v)) {
            picBool(2, p2);
        } else if (p3.equals(v)) {
            picBool(3, p3);
        } else if (p4.equals(v)) {
            picBool(4, p4);
        } else if (p5.equals(v)) {
            picBool(5, p5);
        } */

    }

    /*

    private void picBool(int number, ImageView img) {
        if(!picBoolean[number]) {
            selectPicture();
            img.setImageBitmap(bitmap);

            bitmapStringArray[number] = bitHolder;
            picBoolean[number] = true;
        } else {
            img.setImageBitmap(stockphotoBit);
            bitmapStringArray[number] = null;
            picBoolean[number] = false;
        }
    }

    private void selectPicture() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getContext().checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                Uri uri = takePic();
                System.out.println(uri);

            } else {
                if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                    Toast.makeText(getContext(), "Your Permission is needed to get access the camera", Toast.LENGTH_LONG).show();
                }
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, REQUEST_CAMERARESULT);
            }
        } else {
            Uri uri = takePic();
            System.out.println(uri);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
        {
            Uri imageUri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imageUri);
                bitHolder = BitMapToString(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public Uri takePic(){
        File imagesFolder = new File(Environment.getExternalStorageDirectory(), "MyImages");
        imagesFolder.mkdirs(); // <----
        File image = new File(imagesFolder, "image_001.jpg");
        Uri uri = FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID + ".provider",image);

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, RESULT_LOAD_IMAGE);
        return uri;

    }

    public String BitMapToString(Bitmap bitmap){

        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    } */
}
