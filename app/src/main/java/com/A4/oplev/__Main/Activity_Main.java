package com.A4.oplev.__Main;

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
import java.util.List;
import java.util.Locale;

import com.A4.oplev._Adapters.Event_Adapter;

//import DAL.DBAccess;
import Controller.UserController;
import DAL.Classes.EventDAO;
import DAL.Interfaces.CallBackEventList;
import DAL.Interfaces.CallBackList;
import DAL.Interfaces.CallbackEvent;
import DTO.EventDTO;
import swipeable.com.layoutmanager.OnItemSwiped;
import swipeable.com.layoutmanager.SwipeableLayoutManager;
import swipeable.com.layoutmanager.SwipeableTouchHelperCallback;
import swipeable.com.layoutmanager.touchelper.ItemTouchHelper;

public class Activity_Main extends AppCompatActivity implements View.OnClickListener{
    private GpsTracker gpsTracker;
    ImageView options, match, user;
    RecyclerView rcEvent;
    SharedPreferences prefs;
    Boolean onInstance;
    Event_Adapter event_Adapter;
    Context ctx;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
         */

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        this.ctx = this;

        //Tjekker om hvorvidt man er logget ind
        onInstance = prefs.getBoolean("onInstance", false);

        //skal optimeres og ændres til at vi skal hente data ude fra.


        List<EventDTO> eventList = new ArrayList<>();

        rcEvent = findViewById(R.id.eventRecycleView);


        EventDAO dataA = new EventDAO();


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

    private void eventIni( List<String> ids) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
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

    /*
    public GpsTracker getLocation(){
        gpsTracker = new GpsTracker(Activity_Main.this);
        if(gpsTracker.canGetLocation()){
            return gpsTracker;
        }else{
            gpsTracker.showSettingsAlert();
        }
        return null;
    }
    */

    /*
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case 101:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // TODO bestem afstand til andre events
                    gpsTracker = getLocation();

                    Geocoder geocoder;
                    List<Address> addresses;
                    geocoder = new Geocoder(this, Locale.getDefault());

                    try {
                        addresses = geocoder.getFromLocation(gpsTracker.getLatitude(), gpsTracker.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                        String city = addresses.get(0).getLocality();
                        String country = addresses.get(0).getCountryName();
                        Location location1 = new Location("locationA");
                        location1.setLatitude(gpsTracker.getLatitude());
                        location1.setLongitude(gpsTracker.getLongitude());
                        Location location2 = new Location("locationB");
                        location2.setLongitude(133.2813);
                        location2.setLatitude(-26.4391);
                        Toast.makeText(this, address + "\t" + city + "\t" + country + "\nAfstand til australien " + (location1.distanceTo(location2) / 1000) + " km", Toast.LENGTH_LONG).show();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }  else {
                    Toast.makeText(this, "No permission given", Toast.LENGTH_SHORT).show();
                }
                return;
        }
        // Other 'case' lines to check for other
        // permissions this app might request.
    }
     */
}