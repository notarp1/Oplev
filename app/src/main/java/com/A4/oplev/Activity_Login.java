package com.A4.oplev;

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

import com.A4.oplev.__Main.Activity_Main;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import Controller.Controller;
import DAL.Interfaces.CallbackUser;
import DTO.UserDTO;

public class Activity_Login extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private static final String TAG = "login";
    EditText email, pass;
    Button loginButton, createButton;
    Context ctx;
    Controller controller;
    SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        this.ctx = this;

        email = findViewById(R.id.email);
        pass = findViewById(R.id.password);
        loginButton = findViewById(R.id.login);
        createButton = findViewById(R.id.create);

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


        mAuth.signInWithEmailAndPassword(emailInput, passInput)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");

                            controller = Controller.getInstance();
                            //prefs = PreferenceManager.getDefaultSharedPreferences(ctx);

                            Intent i = new Intent(ctx, Activity_Ini.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);


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