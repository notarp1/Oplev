package com.A4.oplev.UserSettings;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.A4.oplev.R;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;

import static com.google.android.libraries.places.widget.AutocompleteActivity.RESULT_ERROR;

public class Activity_U_Settings extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "Activity_U_Settings";
    ImageView back, accept;
    static TextView title;
    Fragment fragment;


    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);

        setContentView(R.layout.activity_u_settings);
        title = findViewById(R.id.topbar_text);
        back = findViewById(R.id.topbar_arrow);
        accept = findViewById(R.id.imageView_checkmark);

        back.setOnClickListener(this);

        Intent intent = getIntent();
        Intent myIntent = getIntent();

        int selection = myIntent.getIntExtra("selection", 1);

        Bundle bundle = new Bundle();
        bundle.putSerializable("user", intent.getSerializableExtra("user"));

        if(selection == 1)fragment = new U_Settings_Edit();
        else if(selection == 2) fragment = new U_Settings_Options();
        fragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction().replace(R.id.mainFragmentBox, fragment, "uSettingMainBox")
                .commit();

    }


    @Override
    public void onClick(View v) {


    }

    //for google places autocomplete: (jacob)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: request code=" + requestCode + ", result code=" + resultCode);
        if(resultCode == RESULT_OK){
            // if city chosen with Places widget (request code is variable for some reason)
            Log.d(TAG, "onActivityResult: jbe, req=100, result=ok");
            //if google places intent (for location autocomplete)
            // and if success
            //get data into place object
            Place place = Autocomplete.getPlaceFromIntent(data);
            //set the text in the edittext view
            EditText city_in = findViewById(R.id.editText_city);
            city_in.setText(place.getName());
            Log.d(TAG, "onActivityResult: (jbe) place name: " + place.getName());
            Log.d(TAG, "onActivityResult: (jbe) place address: " + place.getAddress());
            Log.d(TAG, "onActivityResult: (jbe) place type: " + place.getTypes().toString());
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
}
