package Controller;

import android.content.SharedPreferences;
import android.location.Location;
import android.net.Uri;
import android.util.Log;

import com.A4.oplev.Activity_Event;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.auth.User;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import DAL.Classes.EventDAO;
import DAL.Classes.UserDAO;
import DAL.Interfaces.CallbackEvent;
import DAL.Interfaces.CallbackUser;
import DTO.EventDTO;
import DTO.UserDTO;

public class EventController {


    private static final String TAG = "EventController";
    private static EventController instance = null;
    private ArrayList<String> uPictures, ePictures;
    static UserDAO userDAO;
    static EventDAO eventDAO;
    private EventDTO eventDTO;
    private UserController userController;


    private EventController(){
        userDAO = new UserDAO();
        eventDAO = new EventDAO();
        userController = userController.getInstance();
        this.instance = this;
    }

    public static EventController getInstance(){
        if (instance == null) instance = new EventController();
        return instance;
    }


    public void createEvent(String name, String desc, String price, String date, String time, String city, String type,
                            int minAge, int maxAge, boolean maleOn, boolean femaleOn, Uri eventImgUri, String coordinates){
        //create event dto
        eventDTO = new EventDTO();

        String avatar = userController.getUserAvatar();

        //set some values of DTO
        eventDTO.setTitle(name);
        eventDTO.setDescription(desc);
        eventDTO.setPrice(Integer.parseInt(price));

        //getting date and time from string method input parameters
        //date string input is DD/MM/YYYY
        String[] dateSplit = date.split("/");
        int day = Integer.parseInt(dateSplit[0]);
        int month = Integer.parseInt(dateSplit[1]) - 1; //decrement to null index months of Date()
        int year = Integer.parseInt(dateSplit[2]) - 1900; //subtract cuz wtf with this date obj
        //getting time the same way. time input format HH:MM
        String[] timeSplit = time.split(":");
        int hour = Integer.parseInt(timeSplit[0]);
        int minute = Integer.parseInt(timeSplit[1]);
        //set the date now that values are available
        eventDTO.setDate(new Date(year, month, day, hour, minute));

        //set rest of values
        eventDTO.setCity(city)
                .setMinAge(minAge)
                .setMaxAge(maxAge)
                .setMaleOn(maleOn)
                .setFemaleOn(femaleOn)
                .setOwnerId(userController.getCurrUser().getUserId())
                .setOwnerPic(avatar)
                .setEventPic(null)
                .setEventId(null)
                .setApplicants(new ArrayList<>())
                .setParticipant(null)
                .setType(type)
                .setCoordinates(coordinates);

        eventDAO.createEvent(eventDTO, eventImgUri);
    }


    public ArrayList<String> getEventPictures(){

        return ePictures;
    }

    public void iniEvents(Activity_Event ctx, EventDTO event, UserDTO user, int j){

        DateFormat format1 = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        format1.setTimeZone(TimeZone.getTimeZone("GMT+1"));


        String eventNameString = event.getTitle() + " med " + user.getfName();
        String eCityString =  event.getCity();

        String eDateString = format1.format(event.getDate());
        String ePriceString = String.valueOf(event.getPrice()) + " DKK";
        String eAboutString = event.getDescription();
        String eUnameString = "Om " + user.getfName();
        String eUaboutString = user.getDescription();
        String personTitle = user.getfName() + ", " + user.getAge();

        ctx.eventName.setText(eventNameString);
        ctx.eCity.setText(eCityString);
        ctx.eDistance.setText(calculateDistance(event,ctx.prefs) + " km");
        ctx.eDate.setText(eDateString);
        ctx.ePrice.setText(ePriceString);
        ctx.eventPname.setText(personTitle);
        //EventDescription
        ctx.eAbout.setText(eAboutString);
        ctx.eUname.setText(eUnameString);
        ctx.eUabout.setText(eUaboutString);
        if(j == 1){
            ctx.eventName.setText(event.getTitle());
        }
    }
    public int calculateDistance(EventDTO event, SharedPreferences prefs){
        int distanceRounded;
        Location userLocation = new Location("locationA");
        userLocation.setLatitude(Double.parseDouble(prefs.getString("gpsLat", "0")));
        userLocation.setLongitude(Double.parseDouble(prefs.getString("gpsLong", "0")));
        int maxDistance = prefs.getInt("distance", 150);
        //setup distance check
        Location eventLocation = new Location("locationB");
        //is saved in dto with format "latitude,longitude" so split by ","
        Log.d(TAG, "calculateDistance: coordinate dto string: " + event.getCoordinates());
        eventLocation.setLatitude(Double.parseDouble(event.getCoordinates().split(",")[0]));
        eventLocation.setLongitude(Double.parseDouble(event.getCoordinates().split(",")[1]));
        Log.d(TAG, "calculateDistance: eventlocation: " + eventLocation.toString());
        float distance = userLocation.distanceTo(eventLocation)/1000;
        Log.d(TAG, "calculateDistance: distance to event: " + distance + " km");
        distanceRounded = Math.round(distance);
        Log.d(TAG, "calculateDistance: distance rounded: " + distanceRounded + " km");
        return distanceRounded;
    }

    public void deleteEvent(String eventId){
        eventDAO.getEvent(new CallbackEvent(){
            @Override
            public void onCallback(EventDTO event) {
                for(String userID : event.getApplicants()){
                    userDAO.getUser(new CallbackUser() {
                        @Override
                        public void onCallback(UserDTO user) {
                            user.getRequestedEvents().remove(eventId);
                            userDAO.updateUser(user);
                        }
                    }, userID);
                }

            }
        }, eventId);

        eventDAO.deleteEvent(eventId);
    }

}
