package DTO;

import java.util.ArrayList;
import java.util.Date;

public class EventDTO {

    int owner, eventId, price, participant;
    String name, city, description, headline;
    Date date;
    ArrayList<String> pictures;
    ArrayList<java.lang.Integer>  applicants;

    public EventDTO setHeadline(String headline){this.headline = headline; return this;}

    public String getHeadline(){return this.headline;}

    public int getEventId() {
        return eventId;
    }

    public EventDTO setEventId(int eventId) {
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

    public String getName() {
        return name;
    }

    public EventDTO setName(String name) {
        this.name = name;
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

    public ArrayList<String> getPictures() {
        return pictures;
    }

    public EventDTO setPictures(ArrayList<String> pictures) {
        this.pictures = pictures;
        return this;
    }

    public int getOwner() {
        return owner;
    }

    public EventDTO setOwner(int owner) {
        this.owner = owner;
        return this;
    }

    public int getParticipant() {
        return participant;
    }

    public EventDTO setParticipant(int participant) {
        this.participant = participant;
        return this;
    }

    public ArrayList<java.lang.Integer> getApplicants() {
        return applicants;
    }

    public EventDTO setApplicants(ArrayList<java.lang.Integer> applicants) {
        this.applicants = applicants;
        return this;
    }
}
