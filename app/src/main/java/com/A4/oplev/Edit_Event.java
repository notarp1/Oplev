package com.A4.oplev;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.A4.oplev.CreateEvent.Activity_Create_Event;
import com.A4.oplev.CreateEvent.createEvent2_frag;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import Controller.PictureMaker;
import Controller.pictureGet;
import DAL.Classes.EventDAO;
import DAL.Interfaces.CallBackURL;
import DTO.EventDTO;

import static android.content.ContentValues.TAG;

public class Edit_Event extends AppCompatActivity implements View.OnClickListener {
    //topbar text
    TextView topbar_txt;
    //fragment elements
    ImageView pic;
    Uri pickedImgUri;
    EditText title_in, desc_in, price_in, city_in;
    TextView price_txt, date_txt, city_txt, date_in, time_in;
    Button next_btn;
    Spinner dropDown;
    AdapterView.OnItemSelectedListener onItemSelectedListener;
    String currentType = "--Vælg type--";
    EventDTO eventDTO; 
    //dialog changelisteners
    DatePickerDialog.OnDateSetListener onDateSetListener;
    TimePickerDialog.OnTimeSetListener onTimeSetListener;

    //date time values
    int day, month, year, hour, minute;

    // for input validation
    boolean inputIsValid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__event);
        //get elements from fragment
        pic = findViewById(R.id.edit_event_pic);
        title_in = findViewById(R.id.edit_event_title_input);
        desc_in = findViewById(R.id.edit_event_desc_input);
        price_in = findViewById(R.id.edit_event_price_input);
        date_in = findViewById(R.id.edit_event_date_input);
        city_in = findViewById(R.id.edit_event_city_input);
        next_btn = findViewById(R.id.edit_event_next_btn);
        time_in = findViewById(R.id.edit_event_time_input);
        dropDown = findViewById(R.id.edit_event_dropDown);

        ArrayAdapter<CharSequence> dropDownAdapter = ArrayAdapter.createFromResource(this,R.array.createDropDown, R.layout.support_simple_spinner_dropdown_item);
        dropDownAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropDown.setAdapter(dropDownAdapter);
        dropDown.setPrompt("Vælg type af oplevelse");

        //set city to non focusable (will open the google places API instead with onclick
        city_in.setFocusable(false);
        city_in.setOnClickListener(this);
        //initialize places (API key saved in string resources
        Places.initialize(getApplicationContext(), getString(R.string.googlePlaces_api_key));

        //set onclick listeners
        pic.setOnClickListener(this);
        next_btn.setOnClickListener(this);
        date_in.setOnClickListener(this);
        time_in.setOnClickListener(this);

        //set current date and time
        Calendar cal = Calendar.getInstance();
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);
        hour = cal.get(Calendar.HOUR_OF_DAY);
        minute = cal.get(Calendar.MINUTE);

        eventDTO = (EventDTO) getIntent().getSerializableExtra("EventDTO");

        pickedImgUri = Uri.parse(eventDTO.getEventPic());
        Log.d("imgload", "uri: "+ pickedImgUri.toString());
        Log.d(TAG, "onCreateView: (jbe) outside repost spottet");
        //if createevent startet from repost then fill out info
        if(eventDTO != null){
            Log.d(TAG, "onCreateView: (jbe) repost spottet!");
            Log.d(TAG, "onCreateView: (jbe) repost date = " + eventDTO.getDate().toString());
            Picasso.get().load(eventDTO.getEventPic()).into(pic);
            //check if repost pic is default pic (no reupload)
            FirebaseStorage.getInstance().getReference().child(getString(R.string.defaultpic_db_path)).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    //download and set image URI
                    Log.d(TAG, "onSuccess: defaultpic download uri: " + uri.toString() + "\n repostpic: " + eventDTO.getEventPic());
                    if(eventDTO.getEventPic().equals(uri.toString())) {
                        Log.d(TAG, "onSuccess: eventpic is defaultpic");
                         pickedImgUri = null;
                        Log.d(TAG, "onSuccess: eventpicUri: " + pickedImgUri);
                    }
                    else{
                        Log.d(TAG, "onSuccess: eventpic is not deafaultpic");
                        imageDownload(eventDTO.getEventPic());
                    }
                }
            });

            //set values from repost event
            title_in.setText(eventDTO.getTitle());
            desc_in.setText(eventDTO.getDescription());
            price_in.setText("" + eventDTO.getPrice());
            //extract values from Date-obj and update ui
            Date repostDate = eventDTO.getDate();
            day = repostDate.getDate();
            month = repostDate.getMonth();
            year = repostDate.getYear() + 1900;
            minute = repostDate.getMinutes();
            hour = repostDate.getHours();
            updateDateUI();
            updateTimeUI();
            city_in.setText(eventDTO.getCity());
            currentType = eventDTO.getType();
            //set the dropdown to the position of eventDTO's type
            dropDown.setSelection(getTypeIndex(eventDTO.getType()));
        }

        dropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // An item was selected. You can retrieve the selected item using
                System.out.println(parent.getItemAtPosition(position));
                //set the newly selected type to local string
                currentType = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });


        //When date is changed update current values and UI to show new date
        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int yearNew, int monthNew, int dayNew) {
                //setting values (not to parse, but for next time datepicker is opened)
                day = dayNew;
                month = monthNew;
                year = yearNew;
                updateDateUI();
            }
        };
        //when time is changed update current values and show new time in UI
        onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourNew, int minuteNew) {
                //setting values
                hour = hourNew;
                minute = minuteNew;
                updateTimeUI();
            }
        };
        
    }
    private int getTypeIndex(String typeToFind){
        //takes a string with a type, returns the position it lies on in the spinner(dropdown)
        int index = 0;
        for (int i=0;i<dropDown.getCount();i++){
            if (dropDown.getItemAtPosition(i).equals(typeToFind)){
                index = i;
            }
        }
        return index;
    }
    private void updateDateUI(){
        //update UI
        // increment month since monthNew is zero indexed (jan = 0)
        String dateString = day + "/" + (month+1) + "/" + year;
        date_in.setText(dateString);
        //remove error of missing date input
        date_in.setError(null);
    }
    private void updateTimeUI(){
        //update UI
        //handle setting zeroes if one ciffer on time
        String hourString = "" + hour;
        String minuteString = "" + minute;
        if(hour < 10){
            hourString = "0" + hour;
        }
        if(minute < 10){
            minuteString = "0" + minute;
        }
        String timeString = hourString + ":" + minuteString;
        time_in.setText(timeString);
        //remove error of missing time input
        time_in.setError(null);
    }

    ImageView targetHolder;
    //save image from repost locally
    private void imageDownload(String url){
        Log.d(TAG, "imageDownload: (jbe) trying to save img");
        targetHolder = new ImageView(this);
        targetHolder.setTag((Target) getTarget(url));
        Picasso.get()
                .load(url)
                .into((Target) targetHolder.getTag());
    }
    //target to save
    private Target getTarget(final String url){
        Log.d(TAG, "getTarget: (jbe) trying to find target");
        Target target = new Target(){
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                Log.d(TAG, "onBitmapLoaded: (jbe) bitmaploaded");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "run: (jbe)");
                        ContextWrapper cw = new ContextWrapper(getApplicationContext());
                        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);

                        File file = new File(directory,"OplevRepostTemp.jpg");
                        try {
                            FileOutputStream ostream = new FileOutputStream(file, false);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream);
                            ostream.flush();
                            ostream.close();
                            Log.d(TAG, "run: (jbe) file saved to " + file.getPath());
                            //setting picked img URI
                            setPickedImgUri(Uri.fromFile(file));
                            Log.d(TAG, "run: (jbe) file absoulute path " + file.getAbsolutePath());
                            Log.d(TAG, "run: (jbe) uri set to " + Uri.fromFile(file).toString());
                        } catch (IOException e) {
                            Log.e("IOException", e.getLocalizedMessage());
                            Log.d(TAG, "run: (jbe) file save exception" + e.getLocalizedMessage()+ "\n"+
                                    e.getStackTrace().toString());
                        }
                    }
                }).start();
            }
            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                Log.d(TAG, "onBitmapFailed: (jbe)" + e.getLocalizedMessage());
            }
            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                Log.d(TAG, "onPrepareLoad: (jbe)");
            }
        };
        return target;
    }

    @Override
    public void onClick(View v) {
        if(v == pic){
            System.out.println("BILLED PRESSED");
            //Is mainly handled from actitity
            //triggers onActivityResult in activity_create_event.java when picture is picked
            PictureMaker.getInstance().uploadPic(this);
        }
        else if(v == next_btn){
            //validate that inputs are entered (shows "errors" if not)
           if(isInputValid()){
               eventDTO.setType(currentType);
               Date newDate = new Date();
               newDate.setDate(day);
               newDate.setMonth(month);
               newDate.setYear(year);
               newDate.setHours(hour);
               newDate.setMinutes(minute);
               eventDTO.setCity(city_in.getText().toString());
               eventDTO.setTitle(title_in.getText().toString());
               eventDTO.setDescription(desc_in.getText().toString());
               eventDTO.setPrice(Integer.parseInt(price_in.getText().toString()));

              /* pictureGet picG = new pictureGet();
               picG.getUrl(new CallBackURL() {
                   @Override
                   public void onCallBack(String url) {
                       eventDTO.setEventPic(url);
                       EventDAO dataA = new EventDAO();
                       dataA.updateEvent(eventDTO);

                       finish();

                   }
               }, eventDTO.getEventId(), pickedImgUri);
            */
               EventDAO dataA = new EventDAO();
               dataA.updateEvent(eventDTO);

               finish();
           }
        }
        else if(v == date_in){
            //show date picker dialog
            //triggers "OnDateSetListener" when date is changed
            DatePickerDialog dialog = new DatePickerDialog(
                    this,
                    android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    onDateSetListener,
                    year, month, day);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            //set min and max date mindate=today, maxdate=one year from now
            // (the minus 1000 avoids crash..."min date doesnt precede date, error")
            dialog.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis()-1000);
            dialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis() +
                    DateUtils.YEAR_IN_MILLIS);

            dialog.show();
        }
        else if(v == time_in){
            //show time picker dialog
            //triggers "OnTimeSetListener" when time is changed
            TimePickerDialog dialog = new TimePickerDialog(
                    this,
                    onTimeSetListener,
                    hour, day, true);
            dialog.show();
        }
        else if(v == city_in){
            /*
            https://youtu.be/t8nGh4gN1Q0
            https://developers.google.com/places/android-sdk/autocomplete#add_an_autocomplete_widget

             */

            //open the places autocomplete api
            List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS,
                    Place.Field.NAME,
                    Place.Field.LAT_LNG);
            //create intent for activity overlay
            //(context this might be off)
            Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList)
                    .build(this);
            startActivityForResult(intent, 100);
            // ***OBS*** onActivityResult (result of intent) handled in activity! (create event activity)
        }
    }

    // Method for checking if all inputs have data. If not, prompt users with "setErrors"
    // return true if all have inputs, return false if not.
    private boolean isInputValid() {
        Log.d(TAG, "isInputValid: (jbe) start");
        inputIsValid = true;
        if(title_in.getText().toString().equals("")){
            inputIsValid = false;
            title_in.setError("Indsæt titel");
        }
        if(desc_in.getText().toString().equals("")){
            inputIsValid = false;
            desc_in.setError("Indsæt beskrivelse");
        }
        if(date_in.getText().toString().equals("DD/MM/YYYY")){
            inputIsValid = false;
            date_in.setError("Indsæt dato");
        }
        if(time_in.getText().toString().equals("HH:MM")){
            inputIsValid = false;
            time_in.setError("Indsæt dato");
        }
        if(city_in.getText().toString().equals("")){
            inputIsValid = false;
            city_in.setError("Indsæt by");
        }
        if(currentType.equals("--Vælg type--")){
            inputIsValid = false;
            Toast.makeText(this, "Vælg en event type", Toast.LENGTH_SHORT).show();
        }

        if(inputIsValid == false){
            Toast.makeText(this, "Mangler event-information", Toast.LENGTH_SHORT).show();
        }
        Log.d(TAG, "isInputValid: (jbe) end. valid status:" + inputIsValid);
        return inputIsValid;
    }
    public Uri getPickedImgUri() {
        Log.d(TAG, "getPickedImgUri: (jbe) get picked imgUri");
        return pickedImgUri;
    }
    public void setPickedImgUri(Uri uri) {
        Log.d(TAG, "setPickedImgUri: (jbe) set picked imgUri");
        pickedImgUri = uri;
    }
   
}

