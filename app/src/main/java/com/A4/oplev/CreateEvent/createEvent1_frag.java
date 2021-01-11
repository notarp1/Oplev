package com.A4.oplev.CreateEvent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

import com.A4.oplev.R;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import Controller.PictureMaker;
import DTO.EventDTO;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

public class createEvent1_frag extends Fragment implements View.OnClickListener{
    //topbar text
    TextView topbar_txt;
    //fragment elements
    ImageView pic;
    EditText title_in, desc_in, price_in, city_in;
    TextView price_txt, date_txt, city_txt, date_in, time_in;
    Button next_btn;
    Spinner dropDown;
    AdapterView.OnItemSelectedListener onItemSelectedListener;
    String currentType = "--Vælg type--";

    //dialog changelisteners
    DatePickerDialog.OnDateSetListener onDateSetListener;
    TimePickerDialog.OnTimeSetListener onTimeSetListener;

    //date time values
    int day, month, year, hour, minute;

    // for input validation
    boolean inputIsValid;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.create_event1_frag, container, false);

        //get elements from fragment
        pic = root.findViewById(R.id.create_pic);
        title_in = root.findViewById(R.id.create_title_input);
        desc_in = root.findViewById(R.id.create_desc_input);
        price_in = root.findViewById(R.id.create_price_input);
        date_in = root.findViewById(R.id.create_date_input);
        city_in = root.findViewById(R.id.create__city_input);
        next_btn = root.findViewById(R.id.create_next_btn);
        time_in = root.findViewById(R.id.create_time_input);
        dropDown = root.findViewById(R.id.create_dropDown);

        ArrayAdapter<CharSequence> dropDownAdapter = ArrayAdapter.createFromResource(getContext(),R.array.createDropDown, R.layout.support_simple_spinner_dropdown_item);
        dropDownAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropDown.setAdapter(dropDownAdapter);
        dropDown.setPrompt("Vælg type af oplevelse");

        //set city to non focusable (will open the google places API instead with onclick
        city_in.setFocusable(false);
        city_in.setOnClickListener(this);
        //initialize places (API key saved in string resources
        Places.initialize(getActivity().getApplicationContext(), getString(R.string.googlePlaces_api_key));

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

        Log.d(TAG, "onCreateView: (jbe) outside repost spottet");
        //if createevent startet from repost then fill out info
        if(((Activity_Create_Event) getActivity()).getRepostEvent() != null){
            Log.d(TAG, "onCreateView: (jbe) repost spottet!");
            EventDTO repostEvent = ((Activity_Create_Event) getActivity()).getRepostEvent();
            Log.d(TAG, "onCreateView: (jbe) repost date = " + repostEvent.getDate().toString());
            title_in.setText(repostEvent.getTitle());
            desc_in.setText(repostEvent.getDescription());
            price_in.setText("" + repostEvent.getPrice());
            //extract values from Date-obj and update ui
            Date repostDate = repostEvent.getDate();
            day = repostDate.getDate();
            month = repostDate.getMonth();
            year = repostDate.getYear() + 1900;
            minute = repostDate.getMinutes();
            hour = repostDate.getHours();
            updateDateUI();
            updateTimeUI();
            city_in.setText(repostEvent.getCity());
            currentType = repostEvent.getType();
            //set the dropdown to the position of repostevent's type
            dropDown.setSelection(getTypeIndex(repostEvent.getType()));
        }

        dropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            // todo: sæt start text, således at motion ikke er valgt fra starten.
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

        return root;
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

    @Override
    public void onClick(View v) {
        if(v == pic){
            System.out.println("BILLED PRESSED");
            //Is mainly handled from actitity
            //triggers onActivityResult in activity_create_event.java when picture is picked
            PictureMaker.getInstance().uploadPic(getActivity());
        }
        else if(v == next_btn){
            //validate that inputs are entered (shows "errors" if not)
            if(isInputValid()){
                //setup bundle to transfer data to next frag
                Bundle b = new Bundle();
                //set strings
                b.putString("title_in", title_in.getText().toString());
                b.putString("desc_in", desc_in.getText().toString());
                if(price_in.getText().toString().equals("")){
                    //if no input price the hint showing is "0", so setting text "0"
                    price_in.setText("0");
                }
                b.putString("price_in", price_in.getText().toString());
                b.putString("date_in", date_in.getText().toString());
                b.putString("time_in", time_in.getText().toString());
                b.putString("city_in", city_in.getText().toString());
                b.putString("type_in", currentType);
                // *** EVENT PIC URI IS IN ACTIVITY, AVAIL FROM NEXT FRAG ALREADY
                // WITH METHOD ((Activity_Create_Event) getActivity()).getPickedImgUri()
                Log.d(TAG, "onClick: (jbe) price = " + price_in.getText());
                //create fragment and add bundle to arguments
                Fragment create2_frag = new createEvent2_frag();
                create2_frag.setArguments(b);

                //move to createEvent2 frag
                getFragmentManager().beginTransaction()
                        .replace(R.id.mainFragmentBox, create2_frag)
                        .addToBackStack(null)
                        .commit();
            }
        }
        else if(v == date_in){
            //show date picker dialog
            //triggers "OnDateSetListener" when date is changed
            DatePickerDialog dialog = new DatePickerDialog(
                    getContext(),
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
                    getContext(),
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
            //(context getActivity might be off)
            Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList)
                    .build(getActivity());
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
        /*if( ((Activity_Create_Event) getActivity()).getPickedImgUri() == null){

            // no picture selected. Ask user if they want to proceed without pic
             TODO : fix below code to wait for user to answer before moving on (callback?)
            // from https://stackoverflow.com/questions/2478517/how-to-display-a-yes-no-dialog-box-on-android
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            //Yes button clicked
                            //input still ok
                            break;
                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            //input not ok
                            inputIsValid = false;
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("Billede ikke valgt. Fortsæt alligevel?").setPositiveButton("Ja", dialogClickListener)
                    .setNegativeButton("Nej", dialogClickListener).show();
        }*/
        if(currentType.equals("--Vælg type--")){
            inputIsValid = false;
            Toast.makeText(getActivity(), "Vælg en event type", Toast.LENGTH_SHORT).show();
        }

        if(inputIsValid == false){
            Toast.makeText(getActivity(), "Mangler event-information", Toast.LENGTH_SHORT).show();
        }
        Log.d(TAG, "isInputValid: (jbe) end. valid status:" + inputIsValid);
        return inputIsValid;
    }
}