package Controller;

import android.net.Uri;

import com.A4.oplev.Activity_Event;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.Date;

import DAL.Classes.EventDAO;
import DAL.Classes.UserDAO;
import DTO.EventDTO;
import DTO.UserDTO;

public class EventController {


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
                            String minAge, String maxAge, boolean maleOn, boolean femaleOn, Uri eventImgUri){
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
                .setMinAge(Integer.parseInt(minAge))
                .setMaxAge(Integer.parseInt(maxAge))
                .setMaleOn(maleOn)
                .setFemaleOn(femaleOn)
                .setOwnerId(userController.getCurrUser().getUserId())
                .setOwnerPic(avatar)
                .setEventPic(null)
                .setEventId(null)
                .setApplicants(new ArrayList<>())
                .setParticipant(null)
                .setType(type);

        eventDAO.createEvent(eventDTO, eventImgUri);
    }


    public ArrayList<String> getEventPictures(){

        return ePictures;
    }

    public void iniEvents(Activity_Event ctx, EventDTO event, UserDTO user){

       String eventNameString = event.getTitle() + " med " + user.getfName();
       String eCityString = "\uD83D\uDCCD " + event.getCity();
       String eDateString = "\uD83D\uDD52 " + event.getDate().toString();
       String ePriceString = "\uD83D\uDCB5" + String.valueOf(event.getPrice());
       String eAboutString = event.getDescription();
       String eUnameString = "Om " + user.getfName();
       String eUaboutString = user.getDescription();

        ctx.eventName.setText(eventNameString);
        ctx.eCity.setText(eCityString);
        ctx.eDate.setText(eDateString);
        ctx.ePrice.setText(ePriceString);
        //EventDescription
        ctx.eAbout.setText(eAboutString);
        ctx.eUname.setText(eUnameString);
        ctx.eUabout.setText(eUaboutString);


    }
}
