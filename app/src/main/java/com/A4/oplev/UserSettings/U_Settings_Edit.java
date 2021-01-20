package com.A4.oplev.UserSettings;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Picture;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.A4.oplev.Activity_Profile;
import com.A4.oplev.R;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import java.lang.invoke.ConstantCallSite;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import Controller.PictureMaker;
import Controller.UserController;

import static android.app.Activity.RESULT_OK;
import static com.google.android.libraries.places.widget.AutocompleteActivity.RESULT_ERROR;


public class U_Settings_Edit extends Fragment implements View.OnClickListener, View.OnLongClickListener {

    private static final String TAG = "Settings";
    public EditText about, city, job, education;
    public TextView textview;
    public ImageView accept, back, p0, p1, p2, p3, p4, p5;
    public ArrayList<String> pictures;
    public Bitmap stockphotoBit;

    UserController userController;
    ConstraintLayout editPage;
    PictureMaker pictureHandler;
    private Uri[] uris;


    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1000;
    public int picNumber;


    Context ctx;



    public View onCreateView(LayoutInflater i, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View root = i.inflate(R.layout.u_setting_edit_frag, container, false);

        this.ctx = getContext();
        userController = UserController.getInstance();
        pictureHandler = PictureMaker.getInstance();

        uris = new Uri[]{null, null, null, null, null, null};

        textview = (TextView) getActivity().findViewById(R.id.topbar_text);
        accept = (ImageView) getActivity().findViewById(R.id.imageView_checkmark);
        back = (ImageView) getActivity().findViewById(R.id.topbar_arrow);

        back.setOnClickListener(this);
        accept.setOnClickListener(this);

        about = root.findViewById(R.id.editText_description);
        city = root.findViewById(R.id.editText_city);
        editPage = root.findViewById(R.id.edit_id);
        job = root.findViewById(R.id.editText_job);
        education = root.findViewById(R.id.editText_edu);
        //setup choose city places google widget (jacob)
        city.setFocusable(false);
        city.setOnClickListener(this);
        Places.initialize(getActivity().getApplicationContext(), getString(R.string.googlePlaces_api_key));


        about.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                editPage.getWindowVisibleDisplayFrame(r);
                int screenHeight = editPage.getRootView().getHeight();
                int keypadHeight = screenHeight - r.bottom;
                //if true then keyboard is showing
                if (keypadHeight > screenHeight * 0.15) {
                    accept.setVisibility(View.VISIBLE);

                }
            }
        });

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
               pictureHandler.picBool(0, this, uris);
            } else if (v == p1) {
                pictureHandler.picBool(1, this, uris);
            } else if (v == p2) {
                pictureHandler.picBool(2, this, uris);
            } else if (v == p3) {
                pictureHandler.picBool(3, this, uris);
            } else if (v == p4) {
                pictureHandler.picBool(4, this, uris);
            } else if (v == p5) {
                pictureHandler.picBool(5, this, uris);
            }
            else if(v == city){
                // (jacob)
                //open the places autocomplete api
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS,
                        Place.Field.NAME,
                        Place.Field.LAT_LNG,
                        Place.Field.TYPES);
                //create intent for activity overlay
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList)
                        .setCountry("DK")
                        .setTypeFilter(TypeFilter.CITIES)
                        .build(getActivity());
                startActivityForResult(intent, 100);
                // ***OBS*** onActivityResult (result of intent) handled in activity! (activity_u_settings)
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


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_CODE: {
                if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //Permissions was granted
                    pictureHandler.pickImageFromGallery();
                    System.out.println("HHEHHE");
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
            pictureHandler.setUris(uris);
            pictureHandler.onAccept();
        } else if(resultCode == RESULT_OK) {
            // if city chosen with Places widget (request code is variable for some reason)
            Log.d(TAG, "onActivityResult: jbe, req=100, result=ok");
            //if google places intent (for location autocomplete)
            // and if success
            //get data into place object
            Place place = Autocomplete.getPlaceFromIntent(data);
            //set the text in the edittext view
            EditText city_in = getActivity().findViewById(R.id.editText_city);
            city_in.setText(place.getName());
            Log.d(TAG, "onActivityResult: (jbe) place name: " + place.getName());
            Log.d(TAG, "onActivityResult: (jbe) place address: " + place.getAddress());
            Log.d(TAG, "onActivityResult: (jbe) place type: " + place.getTypes().toString());
        }
        else if(resultCode== RESULT_ERROR){
            Log.d(TAG, "onActivityResult: jbe, req=100, result=error");
            //Status status = Autocomplete.getStatusFromIntent(data);
            //Log.d(TAG, "onActivityResult: jbe"+ status.getStatusMessage());
            //Toast.makeText(getApplicationContext(),
              //      status.getStatusMessage(),
                //    Toast.LENGTH_SHORT).show();
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


