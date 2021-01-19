package com.A4.oplev.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.A4.oplev.Activity_Ini;
import com.A4.oplev.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

import Controller.UserController;

import static com.google.android.libraries.places.widget.AutocompleteActivity.RESULT_ERROR;

public class Activity_CreateUser extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private static final String TAG = "Activity_CreateUser";
    public EditText fName, lName, city, email, password, age;
    Button createUser;
    View back;
    Context ctx;
    UserController userController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__create_user);

        userController = userController.getInstance();
        mAuth = FirebaseAuth.getInstance();

        this.ctx = this;

        fName = findViewById(R.id.editFName);
        lName = findViewById(R.id.editLName);
        city = findViewById(R.id.editCity);
        age = findViewById(R.id.editAge);
        email = findViewById(R.id.editEmail);
        password = findViewById(R.id.editPassword);
        createUser = findViewById(R.id.buttonCreate);
        back = findViewById(R.id.createUser_backBtn);

        back.setOnClickListener(this);
        createUser.setOnClickListener(this);

        //setup choose city places google widget (jacob)
        city.setFocusable(false);
        city.setOnClickListener(this);
        Places.initialize(getApplicationContext(), getString(R.string.googlePlaces_api_key));
    }

    @Override
    public void onClick(View v) {

        if(v==back){
            finish();
        } else if (v == createUser) {
            createAccount();
        }
        else if(v==city){
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
                    .build(this);
            startActivityForResult(intent, 100);
            // result handled in this activity onActivityResult method
        }

    }



    private void createAccount(){
        String emailInput = String.valueOf(email.getText());
        String passInput = String.valueOf(password.getText());

        mAuth.createUserWithEmailAndPassword(emailInput, passInput)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            userController.createUser(user.getUid(), (Activity_CreateUser) ctx);
                            Intent i = new Intent(ctx, Activity_Ini.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Activity_CreateUser.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                    }
                });
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
            city.setText(place.getName());
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