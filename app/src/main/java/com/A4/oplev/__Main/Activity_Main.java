package com.A4.oplev.__Main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.A4.oplev.Activity_Ini;
import com.A4.oplev.Activity_NoInstance;
import com.A4.oplev.Activity_Profile;
import com.A4.oplev.GpsTracker;
import com.A4.oplev.Like_Hjerte_Side.Activity_Likeside;
import com.A4.oplev.R;
import com.A4.oplev.SearchFilter.Activity_Search_Filter;
import com.A4.oplev.UserSettings.Activity_U_Settings;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.A4.oplev._Adapters.Event_Adapter;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

//import DAL.DBAccess;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Controller.UserController;
import DAL.Classes.EventDAO;
import DAL.Classes.UserDAO;
import DAL.Interfaces.CallBackEventList;
import DAL.Interfaces.CallBackList;
import DAL.Interfaces.CallbackEvent;
import DAL.Interfaces.CallbackUser;
import DTO.EventDTO;
import DTO.UserDTO;
import swipeable.com.layoutmanager.OnItemSwiped;
import swipeable.com.layoutmanager.SwipeableLayoutManager;
import swipeable.com.layoutmanager.SwipeableTouchHelperCallback;
import swipeable.com.layoutmanager.touchelper.ItemTouchHelper;

public class Activity_Main extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "Activity_Main";
    private GpsTracker gpsTracker;
    ImageView options, match, user;
    RecyclerView rcEvent;
    SharedPreferences prefs;
    Boolean onInstance, facebook;
    Event_Adapter event_Adapter;
    Context ctx;
    private CallbackManager callbackManager;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        // sets up a crash listener but only if the app is not in a emulator
        boolean EMULATOR = Build.PRODUCT.contains("sdk") || Build.MODEL.contains("Emulator");
        Log.d("EMULATOR",EMULATOR+"");
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(!EMULATOR);
        FirebaseAnalytics.getInstance(this).setAnalyticsCollectionEnabled(!EMULATOR);

        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "onCreate: gps permission not already granted on start");
                //permission not granted
                prefs.edit().putBoolean("gpsOn", false).apply();
                //request for permission
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
                //response activates onRequestPermissionResult
            }
            else {
                Log.d(TAG, "onCreate: gps permission already granted on start");
                //permission granted
                prefs.edit().putBoolean("gpsOn", true).apply();
                // save location in SharedPref
                saveLocationPref();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        EventDAO dataA = new EventDAO();



        this.ctx = this;

        //Tjekker om hvorvidt man er logget ind
        onInstance = prefs.getBoolean("onInstance", false);
        facebook = prefs.getBoolean("facebook",false);

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();


        if (isLoggedIn){
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email", "user_birthday"));
            callbackManager = CallbackManager.Factory.create();
            LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    Profile profile = Profile.getCurrentProfile();
                    db = FirebaseFirestore.getInstance();
                    CollectionReference usersRef = db.collection("users");
                    Query query = usersRef.whereEqualTo("userId",profile.getId());
                    query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                for(DocumentSnapshot documentSnapshot : task.getResult()){
                                    String user = documentSnapshot.getString("userId");

                                    if(user.equals(profile.getId())){
                                        Log.d(TAG+"123", "User Exists");
                                        UserController.getInstance().setCurrUser((UserDTO) documentSnapshot.toObject(UserDTO.class));
                                        prefs.edit().putBoolean("onInstance", true).apply();
                                        prefs.edit().putBoolean("facebook",true).apply();
                                        onInstance = true;
                                        facebook = true;
                                        dataA.getEventIDs(new CallBackList() {
                                            @Override
                                            public void onCallback(List<String> list) {
                                                eventIni(list);
                                            }
                                        },prefs);
                                    }
                                }
                            }

                            if(task.getResult().size() == 0 ){
                                Log.d(TAG+"123", "User not Exists");
                                // Facebook Email address
                                GraphRequest request = GraphRequest.newMeRequest(
                                        loginResult.getAccessToken(),
                                        new GraphRequest.GraphJSONObjectCallback() {
                                            @Override
                                            public void onCompleted(
                                                    JSONObject object,
                                                    GraphResponse response) {
                                                Log.v("LoginActivity Response ", response.toString());

                                                try {
                                                    UserDTO userdto = new UserDTO();
                                                    String name = object.getString("name");
                                                    String fEmail = object.getString("email");
                                                    String birthday = object.getString("birthday");
                                                    Toast.makeText(ctx,"Birthday: " + birthday, Toast.LENGTH_SHORT).show();
                                                    Toast.makeText(ctx,"email: " + fEmail, Toast.LENGTH_SHORT).show();
                                                    Toast.makeText(ctx, "Name " + name, Toast.LENGTH_LONG).show();

                                                    userdto.setEmail(fEmail);
                                                    userdto.setfName(profile.getFirstName());
                                                    userdto.setlName(profile.getLastName());
                                                    userdto.setUserPicture(profile.getProfilePictureUri(200,200)+"");
                                                    ArrayList<String> billeder = new ArrayList<>();
                                                    billeder.add(profile.getProfilePictureUri(200,200)+"");
                                                    for (int i = 0; i < 5; i++) {
                                                        billeder.add(null);
                                                    }
                                                    userdto.setPictures(billeder);
                                                    int age = getAge(birthday);
                                                    userdto.setAge(age);
                                                    Toast.makeText(ctx,age+"",Toast.LENGTH_SHORT).show();
                                                    userdto.setUserId(profile.getId());
                                                    UserDAO userDAO = new UserDAO();
                                                    userDAO.createUser(userdto);
                                                    UserController.getInstance().setCurrUser(userdto);
                                                    prefs.edit().putBoolean("onInstance", true).apply();
                                                    prefs.edit().putBoolean("facebook",true).apply();
                                                    onInstance = true;
                                                    facebook = true;
                                                    dataA.getEventIDs(new CallBackList() {
                                                        @Override
                                                        public void onCallback(List<String> list) {
                                                            eventIni(list);
                                                        }
                                                    },prefs);

                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                Bundle parameters = new Bundle();
                                parameters.putString("fields", "id,name,email,birthday");
                                request.setParameters(parameters);
                                request.executeAsync();
                            }
                        }
                    });
                }

                @Override
                public void onCancel() {
                    Toast.makeText(ctx,"Cancelled",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(FacebookException error) {
                    Toast.makeText(ctx,error.toString(),Toast.LENGTH_SHORT).show();
                }
            });
        }


        //skal optimeres og ændres til at vi skal hente data ude fra.


        List<EventDTO> eventList = new ArrayList<>();

        rcEvent = findViewById(R.id.eventRecycleView);



        dataA.getEventIDs(new CallBackList() {
            @Override
            public void onCallback(List<String> list) {
                eventIni(list);
            }
        },prefs);


        options = findViewById(R.id.options_btn);
        match = findViewById(R.id.match_btn);
        user = findViewById(R.id.user_btn);


        options.setOnClickListener(this);
        match.setOnClickListener(this);
        user.setOnClickListener(this);


    }

    public int getAge(String birthday){
        Date now = new Date();
        int day = now.getDate();
        int month = now.getMonth()+1;
        int year = now.getYear()+1900;

        String[] dates = birthday.split("/");
        int birthdayDay = Integer.parseInt(dates[0]);
        int birthdayMonth = Integer.parseInt(dates[1]);
        int birthdayYear = Integer.parseInt(dates[2]);

        int yearDiff = year-birthdayYear;
        int monthDiff = month-birthdayMonth;
        int dayDiff = day-birthdayDay;

        if (monthDiff < 0 || dayDiff < 0){
            yearDiff--;
        }
        return yearDiff;
    }


    private void eventIni( List<String> ids) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        Event_Adapter.setInstance(null);
        event_Adapter = Event_Adapter.getInstance(ids, this, height, width);
        rcEvent.setLayoutManager(layoutManager);
        rcEvent.setAdapter(event_Adapter);
        rcEvent.setOnFlingListener(null);
        PagerSnapHelper snap = new PagerSnapHelper();
        snap.attachToRecyclerView(rcEvent);

    }

    @Override
    public void onClick(View v) {
        if(!onInstance){
            Intent i = new Intent(this, Activity_NoInstance.class);
            startActivity(i);


        } else if(v == options){
            Intent i = new Intent(this, Activity_Search_Filter.class);
            startActivity(i);

        } else if(v == match){

            Intent i = new Intent(this, Activity_Likeside.class);
            startActivity(i);

        } else if(v == user){

            Intent i = new Intent(this, Activity_Profile.class);
            startActivity(i);

        }
    }

    public void onResume() {
        super.onResume();
        Event_Adapter eventA = Event_Adapter.getInstance();
        if(eventA != null) {
            if (eventA.getDataChanged()) {
                eventIni(eventA.getIds());
                eventA.setDataChanged(false);
            }
        }

    }

    public GpsTracker getLocation(){
        gpsTracker = new GpsTracker(Activity_Main.this);
        if(gpsTracker.canGetLocation()){
            return gpsTracker;
        }else{
            gpsTracker.showSettingsAlert();
        }
        return null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case 101:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // gps permission granted
                    prefs.edit().putBoolean("gpsOn", true).apply();
                    //save location in SharedPref
                    saveLocationPref();
                    Log.d(TAG, "onRequestPermissionsResult: gps permission granted");

                }  else {
                    //permission not granted
                    prefs.edit().putBoolean("gpsOn", false).apply();
                    Log.d(TAG, "onRequestPermissionsResult: gps permission not granted");
                }
                return;
        }
        // Other 'case' lines to check for other
        // permissions this app might request.
    }
    private void saveLocationPref(){
        gpsTracker = getLocation();
        Log.d(TAG, "saveLocationPref: gpslat: " + gpsTracker.getLatitude() +" gpslong: " + gpsTracker.getLongitude());
        prefs.edit().putString("gpsLat", String.valueOf(gpsTracker.getLatitude())).apply();
        prefs.edit().putString("gpsLong", String.valueOf(gpsTracker.getLongitude())).apply();
        Log.d(TAG, "saveLocationPref: " + prefs.getString("gpsLat","0"));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

}