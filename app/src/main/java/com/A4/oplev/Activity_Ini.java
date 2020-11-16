package com.A4.oplev;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import DAL.Classes.UserDAO;
import DTO.UserDTO;

public class Activity_Ini extends AppCompatActivity {
    UserDAO userDAO = new UserDAO();
    UserDTO userDTO;
    SharedPreferences prefs;
    Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__ini);
        ctx = this;
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        userDAO.getUser(new UserDAO.MyCallback() {
            @Override
            public void onCallback(UserDTO user) {
                setUserDTO(user);
                prefs.edit().putString("UserID",user.getUserId()).apply();
                Intent i = new Intent(ctx, Activity_Main.class);
                i.putExtra("fName",user.getfName()+"");
                i.putExtra("lName",user.getlName()+"");
                i.putExtra("age",user.getAge()+"");
                i.putExtra("desc",user.getDescription()+"");
                i.putExtra("city",user.getCity()+"");
                startActivity(i);
            }
        }, "KHbc7vhvqHEd2bLjgTZM");



    }

    private void setUserDTO(UserDTO dto){
        this.userDTO = dto;
    }


}