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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.A4.oplev.CreateEvent.Activity_Create_Event;
import com.A4.oplev.CreateEvent.createEvent2_frag;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import Controller.PictureMaker;
import DAL.Classes.EventDAO;
import DTO.EventDTO;

import static android.content.ContentValues.TAG;

public class Edit_Event extends AppCompatActivity implements View.OnClickListener {
    TextView date, time, desc, titel, city, price;
    Spinner dropDown;
    ImageView pic;
    Button btn_save;
    EventDTO eventDTO;
    String currentType = "--Vælg type--";
    int year, month, day, hour, min;
    private Uri pickedImgUri;
    Boolean inputIsValid;
    //dialog changelisteners
    DatePickerDialog.OnDateSetListener onDateSetListener;
    TimePickerDialog.OnTimeSetListener onTimeSetListener;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__event);

        date = findViewById(R.id.edit_event_date_input);
        time = findViewById(R.id.edit_event_time_input);
        desc = findViewById(R.id.edit_event_desc_input);
        titel = findViewById(R.id.edit_event_title_input);
        city = findViewById(R.id.edit_event_city_input);
        price = findViewById(R.id.edit_event_price_input);
        dropDown = findViewById(R.id.edit_event_dropDown);
        eventDTO = (EventDTO)getIntent().getSerializableExtra("EventDTO");
        btn_save = findViewById(R.id.edit_event_next_btn);
        pic = findViewById(R.id.edit_event_pic);
        //set city to non focusable (will open the google places API instead with onclick
        city.setFocusable(false);
        city.setOnClickListener(this);
        //initialize places (API key saved in string resources
        Places.initialize(getApplicationContext(), getString(R.string.googlePlaces_api_key));


        //set current date and time
        Calendar cal = Calendar.getInstance();
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);
        hour = cal.get(Calendar.HOUR_OF_DAY);
        min = cal.get(Calendar.MINUTE);


        try {
            price.setText(eventDTO.getPrice());
        }catch (Exception e){
            Log.d(TAG, "onCreate: ");
        }
            desc.setText(eventDTO.getDescription());
            titel.setText(eventDTO.getTitle());
            city.setText(eventDTO.getTitle());
            year = eventDTO.getDate().getYear();
            day = eventDTO.getDate().getDate();
            month = eventDTO.getDate().getMonth();
            hour = eventDTO.getDate().getHours();
            min = eventDTO.getDate().getMinutes();

            updateDateUI();
            updateTimeUI();


        ArrayAdapter<CharSequence> dropDownAdapter = ArrayAdapter.createFromResource(this,R.array.createDropDown, R.layout.support_simple_spinner_dropdown_item);
        dropDownAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //set onclick listeners
        pic.setOnClickListener(this);
        date.setOnClickListener(this);
        time.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        currentType = eventDTO.getType();
        //set the dropdown to the position of repostevent's type
        dropDown.setSelection(getTypeIndex(eventDTO.getType()));

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
                min = minuteNew;
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
        date.setText(dateString);
        //remove error of missing date input
        date.setError(null);
    }
    private void updateTimeUI(){
        //update UI
        //handle setting zeroes if one ciffer on time
        String hourString = "" + hour;
        String minuteString = "" + min;
        if(hour < 10){
            hourString = "0" + hour;
        }
        if(min < 10){
            minuteString = "0" + min;
        }
        String timeString = hourString + ":" + minuteString;
        time.setText(timeString);
        //remove error of missing time input
        time.setError(null);
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
                            pickedImgUri = Uri.fromFile(file);
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
        if(v == btn_save){
            eventDTO.setPrice(Integer.parseInt(price.getText().toString()));
            eventDTO.setDescription(desc.getText().toString());
            eventDTO.setTitle(titel.getText().toString());
            eventDTO.setCity(city.getText().toString());

            Date date_ = new Date();
            String[] sHM = time.getText().toString().split(":");
            String[] sDate = date.getText().toString().split("/");
            date_.setDate(Integer.parseInt(sDate[0]));
            date_.setMonth(Integer.parseInt(sDate[1]));
            date_.setYear(Integer.parseInt(sDate[2]));
            date_.setHours(Integer.parseInt(sHM[0]));
            date_.setMinutes(Integer.parseInt(sHM[1]));
            eventDTO.setDate(date_);
           // eventDTO.setType((String) type.getText());
            EventDAO eventDAO = new EventDAO();
            eventDAO.updateEvent(eventDTO);
        } if(v == pic){
            System.out.println("BILLED PRESSED");
            //Is mainly handled from actitity
            //triggers onActivityResult in activity_create_event.java when picture is picked
            PictureMaker.getInstance().uploadPic(this);
        }
        else if(v == btn_save){
            //validate that inputs are entered (shows "errors" if not)

        }
        else if(v == date){
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
        else if(v == time){
            //show time picker dialog
            //triggers "OnTimeSetListener" when time is changed
            TimePickerDialog dialog = new TimePickerDialog(
                    this,
                    onTimeSetListener,
                    hour, day, true);
            dialog.show();
        }
        else if(v == city){
            /*
            https://youtu.be/t8nGh4gN1Q0
            https://developers.google.com/places/android-sdk/autocomplete#add_an_autocomplete_widget

             */

            //open the places autocomplete api
            List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS,
                    Place.Field.NAME,
                    Place.Field.LAT_LNG);
            //create intent for activity overlay
            //(context getActivity might be off)
            Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList)
                    .build(this);
            startActivityForResult(intent, 100);
            // ***OBS*** onActivityResult (result of intent) handled in activity! (create event activity)
        }
    }
    private boolean isInputValid() {
        Log.d(TAG, "isInputValid: (jbe) start");
        inputIsValid = true;
        if(titel.getText().toString().equals("")){
            inputIsValid = false;
            titel.setError("Indsæt titel");
        }
        if(desc.getText().toString().equals("")){
            inputIsValid = false;
            desc.setError("Indsæt beskrivelse");
        }
        if(date.getText().toString().equals("DD/MM/YYYY")){
            inputIsValid = false;
            date.setError("Indsæt dato");
        }
        if(time.getText().toString().equals("HH:MM")){
            inputIsValid = false;
            time.setError("Indsæt dato");
        }
        if(city.getText().toString().equals("")){
            inputIsValid = false;
            city.setError("Indsæt by");
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

}

