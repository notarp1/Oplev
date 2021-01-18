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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

import Controller.UserController;
import DAL.Interfaces.CallbackUser;
import DTO.UserDTO;

public class Activity_Login extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private static final String TAG = "login";
    EditText email, pass;
    Button loginButton, createButton;
    Context ctx;
    UserController userController;
    SharedPreferences prefs;
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
                                    FirebaseCrashlytics.getInstance().setUserId(user.getUserId());
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
}