package Controller;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import com.A4.oplev.Activity_Event;
import com.A4.oplev.CreateEvent.Activity_Create_Event;
import com.A4.oplev.CreateEvent.createEvent1_frag;
import com.A4.oplev.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
import DAL.Interfaces.IEventDAO;
import DAL.Interfaces.IUserDAO;
import DTO.EventDTO;
import DTO.UserDTO;

import static android.content.ContentValues.TAG;

public class EventController {


    private static final String TAG = "EventController";
    private static EventController instance = null;
    private ArrayList<String> uPictures, ePictures;
    static IUserDAO userDAO;
    static IEventDAO eventDAO;
    private EventDTO eventDTO;
    private UserController userController;


    private EventController(IUserDAO userDAO, IEventDAO eventDAO){
        this.userDAO = userDAO;
        this.eventDAO = eventDAO;
        userController = userController.getInstance();
        this.instance = this;
    }

    public static EventController getInstance(IUserDAO userDAO, IEventDAO eventDAO){
        if (instance == null) instance = new EventController(userDAO, eventDAO);
        return instance;
    }
    public static EventController getInstance(){
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
    public void iniRepost(createEvent1_frag ctx){
        Log.d(TAG, "onCreateView: (jbe) repost spottet!");
        EventDTO repostEvent = ((Activity_Create_Event) ctx.getActivity()).getRepostEvent();
        Log.d(TAG, "onCreateView: (jbe) repost date = " + repostEvent.getDate().toString());
        Picasso.get().load(repostEvent.getEventPic()).into(ctx.pic);
        //check if repost pic is default pic (no reupload)
        FirebaseStorage.getInstance().getReference().child(ctx.getString(R.string.defaultpic_db_path)).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                //download and set image URI
                Log.d(TAG, "onSuccess: defaultpic download uri: " + uri.toString() + "\n repostpic: " + repostEvent.getEventPic());
                if(repostEvent.getEventPic().equals(uri.toString())) {
                    Log.d(TAG, "onSuccess: eventpic is defaultpic");
                    ((Activity_Create_Event) ctx.getActivity()).setPickedImgUri(null);
                    Log.d(TAG, "onSuccess: eventpicUri: " + ((Activity_Create_Event) ctx.getActivity()).getPickedImgUri());
                }
                else{
                    Log.d(TAG, "onSuccess: eventpic is not deafaultpic");
                    imageDownload(repostEvent.getEventPic(), ctx);
                }
            }
        });
        //set values from repost event
        ctx.title_in.setText(repostEvent.getTitle());
        ctx.desc_in.setText(repostEvent.getDescription());
        ctx.price_in.setText("" + repostEvent.getPrice());
        ((Activity_Create_Event) ctx.getActivity()).setCoordinates(repostEvent.getCoordinates());
        //extract values from Date-obj and update ui
        Date repostDate = repostEvent.getDate();
        ctx.day = repostDate.getDate();
        ctx.month = repostDate.getMonth();
        ctx.year = repostDate.getYear() + 1900;
        ctx.minute = repostDate.getMinutes();
        ctx.hour = repostDate.getHours();
        ctx.updateDateUI();
        ctx.updateTimeUI();
        ctx.city_in.setText(repostEvent.getCity());
        ctx.currentType = repostEvent.getType();
        //set the dropdown to the position of repostevent's type
        ctx.dropDown.setSelection(getTypeIndex(repostEvent.getType(), ctx));
    }
    private int getTypeIndex(String typeToFind, createEvent1_frag ctx){
        //takes a string with a type, returns the position it lies on in the spinner(dropdown)
        int index = 0;
        for (int i=0;i<ctx.dropDown.getCount();i++){
            if (ctx.dropDown.getItemAtPosition(i).equals(typeToFind)){
                index = i;
            }
        }
        return index;
    }
    ImageView targetHolder;
    //save image from repost locally
    private void imageDownload(String url, createEvent1_frag ctx){
        Log.d(TAG, "imageDownload: (jbe) trying to save img");
        targetHolder = new ImageView(ctx.getContext());
        targetHolder.setTag((Target) getTarget(url, ctx));
        Picasso.get()
                .load(url)
                .into((Target) targetHolder.getTag());
    }
    //target to save
    private Target getTarget(final String url, createEvent1_frag ctx){
        Log.d(TAG, "getTarget: (jbe) trying to find target");
        Target target = new Target(){
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                Log.d(TAG, "onBitmapLoaded: (jbe) bitmaploaded");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "run: (jbe)");
                        ContextWrapper cw = new ContextWrapper(ctx.getActivity().getApplicationContext());
                        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);

                        File file = new File(directory,"OplevRepostTemp.jpg");
                        try {
                            FileOutputStream ostream = new FileOutputStream(file, false);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream);
                            ostream.flush();
                            ostream.close();
                            Log.d(TAG, "run: (jbe) file saved to " + file.getPath());
                            //setting picked img URI
                            ((Activity_Create_Event) ctx.getActivity()).setPickedImgUri(Uri.fromFile(file));
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
}
