package com.A4.oplev.CreateEvent;

import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.A4.oplev.R;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;

import java.util.Date;

import DTO.EventDTO;

import static com.google.android.libraries.places.widget.AutocompleteActivity.RESULT_ERROR;

public class Activity_Create_Event extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "Activity_Create_Event";
    ImageView back;
    static TextView title;
    private Uri pickedImgUri;
    private ConstraintLayout constraintLayout;
    private Button create1_next_btn;
    private EventDTO repostEvent = null;
    public boolean edit = false;
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        // uses the same topbar with empty frag below as u_settings...
        setContentView(R.layout.activity_u_settings);
        // get elements from activity
        title = findViewById(R.id.topbar_text);
        back = findViewById(R.id.topbar_arrow);
        //set onlick listner
        back.setOnClickListener(this);
        //set value of title text in topbar
        title.setText("Opret Event");
        //fill fragment holder with createvent 1
        getSupportFragmentManager().beginTransaction().replace(R.id.mainFragmentBox,  new createEvent1_frag(), "uSettingMainBox")
                .commit();
        //initiate picked image to null
        pickedImgUri = null;

        //stuff to check if keyboard is up, removing next_button when up, showning next_button when down
        constraintLayout = findViewById(R.id.top_bar_rootView);
        constraintLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //get values to see if layout changes
                Rect r = new Rect();
                constraintLayout.getWindowVisibleDisplayFrame(r);
                int screenHeight = constraintLayout.getRootView().getHeight();
                int keypadHeight = screenHeight - r.bottom;
                //if true then keyboard is showing
                if (keypadHeight > screenHeight * 0.15) {
                    Log.d(TAG, "onGlobalLayout: keyboard showing");
                    create1_next_btn = findViewById(R.id.create_next_btn);
                    if(create1_next_btn != null) {
                        create1_next_btn.setVisibility(View.GONE);
                    }
                } else { //keyboard not showing
                    Log.d(TAG, "onGlobalLayout: keyboard not showing");
                    create1_next_btn = findViewById(R.id.create_next_btn);
                    if(create1_next_btn != null) {
                        create1_next_btn.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        Log.d(TAG, "onCreate: (jbe) outside if-repost");
        // *** TESTING
       /* Date date = new Date();
        repostEvent = new EventDTO();
        repostEvent.setTitle("RepostTitle").setDescription("RepostDesc").setPrice(420)
                .setCity("RepostCity").setType("Kultur").setDate(date).setFemaleOn(true).setMaleOn(true)
                .setMinAge(42).setMaxAge(69);*/
        // ***
        //if coming from repost save the event object here to access it from fragment
        if(((EventDTO) getIntent().getSerializableExtra("event")) != null){
            Log.d(TAG, "onCreate: (jbe) inside if-repost");
            repostEvent = (EventDTO) getIntent().getSerializableExtra("event");
        }
        edit = getIntent().getBooleanExtra("edit", false);



    }
    @Override
    public void onClick(View v) {
        if (v == back) {
           finish();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: jbe in method \n" +
                "req code = " + requestCode + "\n" +
                "res code = " + resultCode);


        if (requestCode == 1 && resultCode == RESULT_OK) {
            //if its an image change
            Log.d(TAG, "onActivityResult: jbe, result=ok, no req code");
            //change picture source URI
            ImageView pic = findViewById(R.id.create_pic);
            pickedImgUri = data.getData();
            pic.setImageURI(pickedImgUri);
            //remove the "change pic" text
            TextView changePicTxt = findViewById(R.id.create_changepic_txt);
            changePicTxt.setVisibility(View.GONE);
        }
        else if(resultCode== RESULT_OK){
            // if city chosen with Places widget (request code is variable for some reason)
            Log.d(TAG, "onActivityResult: jbe, req=100, result=ok");
            //if google places intent (for location autocomplete)
            // and if success
            //get data into place object
            Place place = Autocomplete.getPlaceFromIntent(data);
            //set the text in the edittext view
            EditText city_in = findViewById(R.id.create__city_input);
            city_in.setText(place.getAddress());
            Log.d(TAG, "onActivityResult: jbe, adress: " + place.getAddress());
            city_in.setError(null);
        }
        else if(resultCode== RESULT_ERROR){
            Log.d(TAG, "onActivityResult: jbe, req=100, result=error");
            Status status = Autocomplete.getStatusFromIntent(data);
            Log.d(TAG, "onActivityResult: jbe"+ status.getStatusMessage());
            Toast.makeText(getApplicationContext(),
                    status.getStatusMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    public Uri getPickedImgUri() {
        Log.d(TAG, "getPickedImgUri: (jbe) get picked imgUri");
        return pickedImgUri;
    }
    public void setPickedImgUri(Uri uri) {
        Log.d(TAG, "setPickedImgUri: (jbe) set picked imgUri");
        pickedImgUri = uri;
    }

    public EventDTO getRepostEvent() {
        return repostEvent;
    }


}
