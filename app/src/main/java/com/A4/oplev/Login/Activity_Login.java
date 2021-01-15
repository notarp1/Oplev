package com.A4.oplev.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.A4.oplev.Activity_Ini;
import com.A4.oplev.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import Controller.UserController;
import DAL.Classes.UserDAO;
import DAL.Interfaces.CallbackUser;
import DTO.UserDTO;

public class Activity_Login extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private static final String EMAIL = "email";
    private static final String TAG = "login";
    EditText email, pass;
    Button loginButton, createButton;
    Context ctx;
    UserController userController;
    SharedPreferences prefs;
    private LoginButton fb_loginButton;
    private FirebaseFirestore db;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        this.ctx = this;

        email = findViewById(R.id.email);
        pass = findViewById(R.id.password);
        loginButton = findViewById(R.id.login);
        createButton = findViewById(R.id.create);

        email.setText("johnjohn@gmail.com");
        pass.setText("johnjohn123");

        loginButton.setOnClickListener(this);
        createButton.setOnClickListener(this);

        fb_loginButton = findViewById(R.id.fb_login_button);
        fb_loginButton.setReadPermissions(EMAIL);

        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email", "user_birthday"));
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

    @Override
    public void onClick(View v) {
        if(v == loginButton){
            signIn();

        } else if (v == createButton){
            Intent i = new Intent(this, Activity_CreateUser.class);
            startActivity(i);
        }
    }

    private void signIn(){

        String emailInput;
        String passInput;

        if(pass.getText().toString().equals("")){
            passInput = " ";
        } else passInput = String.valueOf(pass.getText());

        if(email.getText().toString().equals("")){
            emailInput = " ";
        } else emailInput = String.valueOf(email.getText());

        System.out.println(emailInput);
        System.out.println(passInput);
        prefs.edit().putString("username", emailInput).apply();
        prefs.edit().putString("password", passInput).apply();
        mAuth.signInWithEmailAndPassword(emailInput, passInput)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");

                            userController = UserController.getInstance();
                            userController.getUser(new CallbackUser() {
                                @Override
                                public void onCallback(UserDTO user) {
                                    userController.setCurrUser(user);
                                    Intent i = new Intent(ctx, Activity_Ini.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(i);
                                }
                            }, task.getResult().getUser().getUid());
                            //prefs = PreferenceManager.getDefaultSharedPreferences(ctx);


                    } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(Activity_Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

}