package com.A4.oplev.CreateEvent;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.A4.oplev.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.google.firebase.firestore.auth.User;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.InputStream;
import java.util.ArrayList;

import Controller.UserController;
import DAL.Classes.EventDAO;
import DAL.Interfaces.CallbackEvent;
import DTO.EventDTO;

import static com.facebook.FacebookSdk.getApplicationContext;

public class createEvent3_frag extends Fragment implements View.OnClickListener {
    TextView congrat_txt, askfriend_txt, shareby_txt;
    Button done_btn;
    ImageView fb_img, fbmsg_img, sms_img, email_img;
    private CallbackManager callbackManager;
    private ShareDialog shareDialog;
    private Target target;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FacebookSdk.sdkInitialize(getApplicationContext());
        View root = inflater.inflate(R.layout.create_event3_frag, container, false);
        //get elements from fragment
        done_btn = root.findViewById(R.id.create3_skip_btn);
        fb_img = root.findViewById(R.id.create3_fb_img);
        fbmsg_img = root.findViewById(R.id.create3_fbmsg_img);
        sms_img = root.findViewById(R.id.create3_sms_img);
        email_img = root.findViewById(R.id.create3_email_img);

        //set onclicklisteners (only "done-button" implemented so far)
        fb_img.setOnClickListener(this);
        fbmsg_img.setOnClickListener(this);
        sms_img.setOnClickListener(this);
        email_img.setOnClickListener(this);

        done_btn.setOnClickListener(this);


        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(getActivity());

        target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                SharePhoto photo = new SharePhoto.Builder()
                        .setBitmap(bitmap)
                        .build();

                ArrayList<SharePhoto> photos = new ArrayList<>();
                photos.add(photo);

                if (ShareDialog.canShow(SharePhotoContent.class)) {
                    SharePhotoContent content = new SharePhotoContent.Builder()
                            .setPhotos(photos)
                            .build();
                    shareDialog.show(content);
                }
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        return root;
    }

    @Override
    public void onClick(View v) {
        if(v == done_btn){
            // go back to main fragment
            getActivity().finish();
        }
        else if(v == fb_img){
            shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
                @Override
                public void onSuccess(Sharer.Result result) {
                    Toast.makeText(getActivity(),"SHARE SUCCESS",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancel() {
                    Toast.makeText(getActivity(),"SHARE CANCEL",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(FacebookException error) {
                    Toast.makeText(getActivity(),"SHARE ERROR",Toast.LENGTH_SHORT).show();
                }
            });
            EventDAO eventDAO = new EventDAO();

            //eventDAO.getEvent(new CallbackEvent() {
            //    @Override
            //    public void onCallback(EventDTO event) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setQuote(UserController.getInstance().getCurrUser().getfName() + " har lige oprettet et opslag på oplev.\nHvis du vil være med til at deltage i " + "blabla" + " så gå ind på oplev og ansøg nu!")
                    .setImageUrl(Uri.parse("https://firebasestorage.googleapis.com/v0/b/opleva4.appspot.com/o/events%2FsD7wyGW1mzPFqeZIKC2w%2F1?alt=media&token=9263b677-144c-48b9-af28-10e5914daacf"))
                    .setContentUrl(Uri.parse("https://firebasestorage.googleapis.com/v0/b/opleva4.appspot.com/o/events%2FsD7wyGW1mzPFqeZIKC2w%2F1?alt=media&token=9263b677-144c-48b9-af28-10e5914daacf"))
                    .setContentTitle("blabla")
                    .setShareHashtag(new ShareHashtag.Builder().setHashtag("#Oplev").build())
                    .setContentDescription("test")
                    .build();
            if (ShareDialog.canShow(ShareLinkContent.class)){
                shareDialog.show(linkContent, ShareDialog.Mode.FEED);
            }
            //     }
            // }, UserController.getInstance().getCurrUser().getEvents().get(UserController.getInstance().getCurrUser().getEvents().size() - 1));
        }
        else if(v==fbmsg_img){
            shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
                @Override
                public void onSuccess(Sharer.Result result) {
                    Toast.makeText(getActivity(),"SHARE SUCCESS",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancel() {
                    Toast.makeText(getActivity(),"SHARE CANCEL",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(FacebookException error) {
                    Toast.makeText(getActivity(),"SHARE ERROR",Toast.LENGTH_SHORT).show();
                }
            });
            Picasso.get().load(UserController.getInstance().getCurrUser().getUserPicture()).placeholder(R.drawable.load2).error(R.drawable.question).into(target);
        }
        else if(v == sms_img){
            //start sharing by sms
        }
        else if(v == email_img){
            //start sharing by email
        }
    }
}