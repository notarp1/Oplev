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
}
