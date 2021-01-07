package com.A4.oplev.CreateEvent;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.A4.oplev.R;

public class Activity_Create_Event extends AppCompatActivity implements View.OnClickListener {
    ImageView back;
    static TextView title;
    private Uri pickedImgUri;
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
        System.out.println("jbe in actres act");
        if (resultCode == RESULT_OK) {
            //change picture source URI
            ImageView pic = findViewById(R.id.create_pic);
            pickedImgUri = data.getData();
            pic.setImageURI(pickedImgUri);
            //remove the "change pic" text
            TextView changePicTxt = findViewById(R.id.create_changepic_txt);
            changePicTxt.setVisibility(View.GONE);
        }
    }

    public Uri getPickedImgUri() {
        System.out.println("jbe picked img method woop");
        return pickedImgUri;
    }
}
