package com.A4.oplev;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

public class Activity_CreateUser extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private static final String TAG = "login";
    public EditText fName, lName, city, email, password, age;
    Button createUser;
    Context ctx;
    Controller controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__create_user);

        controller = Controller.getInstance();
        mAuth = FirebaseAuth.getInstance();

        this.ctx = this;

        fName = findViewById(R.id.editFName);
        lName = findViewById(R.id.editLName);
        city = findViewById(R.id.editCity);
        age = findViewById(R.id.editAge);
        email = findViewById(R.id.editEmail);
        password = findViewById(R.id.editPassword);
        createUser = findViewById(R.id.buttonCreate);

        createUser.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        createAccount();

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

                            controller.createUser(user.getUid(), (Activity_CreateUser) ctx);
                            finish();


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Activity_CreateUser.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                    }
                });
    }
}