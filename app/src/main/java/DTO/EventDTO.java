package DTO;

import android.util.EventLog;

import java.util.ArrayList;
import java.util.Date;

public class EventDTO {

    private int price, minAge, maxAge;
    private String ownerId, ownerPic, eventId, eventPic,  participant, title, city, description, type;
    private Date date;
    private boolean maleOn, femaleOn;
    private ArrayList<String>  applicants;

    public String getEventId() {
        return eventId;
    }

    public EventDTO setEventId(String eventId) {
        this.eventId = eventId;
        return this;
    }

    public int getPrice() {
        return price;
    }

    public EventDTO setPrice(int price) {
        this.price = price;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public EventDTO setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getCity() {
        return city;
    }

    public EventDTO setCity(String city) {
        this.city = city;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public EventDTO setDescription(String description) {
        this.description = description;
        return this;
    }

    public Date getDate() {
        return date;
    }

    public EventDTO setDate(Date date) {
        this.date = date;
        return this;
    }

    public String getEventPic() {
        return eventPic;
    }

    public EventDTO setEventPic(String eventPic) {
        this.eventPic = eventPic;
        return this;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public EventDTO setOwnerId(String ownerId) {
        this.ownerId = ownerId;
        return this;
    }

    public String getParticipant() {
        return participant;
    }

    public EventDTO setParticipant(String participant) {
        this.participant = participant;
        return this;
    }

    public ArrayList<String> getApplicants() {
        return applicants;
    }

    public EventDTO setApplicants(ArrayList<String> applicants) {
        this.applicants = applicants;
        return this;
    }

    public boolean isMaleOn() {
        return maleOn;
    }

    public EventDTO setMaleOn(boolean maleOn) {
        this.maleOn = maleOn;
        return this;
    }

    public boolean isFemaleOn() {
        return femaleOn;
    }

    public EventDTO setFemaleOn(boolean femaleOn) {
        this.femaleOn = femaleOn;
        return this;
    }

    public int getMinAge() {
        return minAge;
    }

    public EventDTO setMinAge(int minAge) {
        this.minAge = minAge;
        return this;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public EventDTO setMaxAge(int maxAge) {
        this.maxAge = maxAge;
        return this;
    }

    public String getOwnerPic() {
        return ownerPic;
    }

    public EventDTO setOwnerPic(String ownerPic) {
        this.ownerPic = ownerPic;
        return this;
    }

    public String getType() {
        return type;
    }

    public EventDTO setType(String type) {
        this.type = type;
        return this;
    }


}
