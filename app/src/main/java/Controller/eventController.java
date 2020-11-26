package Controller;

import com.A4.oplev.Activity_Event;

import java.util.ArrayList;
import java.util.Date;

import DAL.Classes.EventDAO;
import DAL.Classes.UserDAO;
import DTO.EventDTO;

public class eventController {


    private static eventController instance = null;
    private ArrayList<String> uPictures, ePictures;
    static UserDAO userDAO;
    static EventDAO eventDAO;
    private EventDTO eventDTO;


    private eventController(){
        userDAO = new UserDAO();
        eventDAO = new EventDAO();
        this.instance = this;
    }

    public static eventController getInstance(){
        if (instance == null) instance = new eventController();
        return instance;
    }

    public void createEvent(String name, String desc, String price, String date, String time, String city,
                            String minAge, String maxAge, boolean maleOn, boolean femaleOn){
        //create event dto
        eventDTO = new EventDTO();

        /*
        TODO: set owner of the event. is it string? (also pics/applicants/participants arent set)
        event.setOwner(getCurrUser().getUserId());*/

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
        eventDTO.setCity(city);
        eventDTO.setMinAge(Integer.parseInt(minAge));
        eventDTO.setMaxAge(Integer.parseInt(maxAge));
        eventDTO.setMaleOn(maleOn);
        eventDTO.setFemaleOn(femaleOn);

        /*//testing
        System.out.println("maleOn:" + maleOn);
        System.out.println(day + "/" + month + "/" + year + "\n"
                +"date:" + event.getDate());*/

        // send event through DAO to database
        eventDAO = new EventDAO();
        eventDAO.createEvent(eventDTO);
    }


    public ArrayList<String> getEventPictures(){

        return ePictures;
    }

    public void iniEvents(Activity_Event ctx){

       String eventNameString = "";
       String eCityString = "";
       String eDateString = "";
       String ePriceString = "";
       String eAboutString = "";
       String eUnameString = "";
       String eUaboutString = "";

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
