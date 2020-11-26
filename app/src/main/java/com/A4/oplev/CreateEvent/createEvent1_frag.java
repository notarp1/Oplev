package com.A4.oplev.CreateEvent;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

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

import com.A4.oplev.R;

import java.util.Calendar;

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


    //dialog changelisteners
    DatePickerDialog.OnDateSetListener onDateSetListener;
    TimePickerDialog.OnTimeSetListener onTimeSetListener;

    //date time values
    int day, month, year, hour, minute;
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


        dropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            // todo: sæt start text, således at motion ikke er valgt fra starten.
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // An item was selected. You can retrieve the selected item using
                System.out.println(parent.getItemAtPosition(position));
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
                //setting values
                day = dayNew;
                month = monthNew + 1; //increment since monthNew is zero indexed (jan = 0)
                year = yearNew;
                //update UI
                String dateString = day + "/" + month + "/" + year;
                date_in.setText(dateString);
            }
        };
        //when time is changed update current values and show new time in UI
        onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourNew, int minuteNew) {
                //setting values
                hour = hourNew;
                minute = minuteNew;
                //update UI
                String timeString = hour + ":" + minute;
                time_in.setText(timeString);
            }
        };

        return root;
    }

    @Override
    public void onClick(View v) {
        if(v == pic){
            //TODO: make possible for user to change source of "pic", when pressing it
        }
        else if(v == next_btn){
            //validate that inputs are entered (shows "errors" if not)
            if(isInputValid()){
                //setup bundle to transfer data to next frag
                Bundle b = new Bundle();
                //set strings
                b.putString("title_in", title_in.getText().toString());
                b.putString("desc_in", desc_in.getText().toString());
                b.putString("price_in", price_in.getText().toString());
                b.putString("date_in", date_in.getText().toString());
                b.putString("time_in", time_in.getText().toString());
                b.putString("city_in", city_in.getText().toString());

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
    }

    // Method for checking if all inputs have data. If not, prompt users with "setErrors"
    // return true if all have inputs, return false if not.
    private boolean isInputValid() {
        String invalidInputToast = "Manglende input: \r\n";
        boolean inputIsValid = true;
        if(title_in.getText().toString().equals("")){
            inputIsValid = false;
            title_in.setError("Indsæt titel");
        }
        if(desc_in.getText().toString().equals("")){
            inputIsValid = false;
            desc_in.setError("Indsæt beskrivelse");
        }
        if(price_in.getText().toString().equals("")){
            inputIsValid = false;
            price_in.setError("Indsæt pris");
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

        return inputIsValid;
    }
}