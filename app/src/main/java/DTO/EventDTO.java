package DTO;

import java.util.ArrayList;
import java.util.Date;

public class EventDTO {

    int eventId, price, participant;
    String name, city, description;
    Date date;
    ArrayList<String> pictures;
    ArrayList<java.lang.Integer> owner, applicants;

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public ArrayList<String> getPictures() {
        return pictures;
    }

    public void setPictures(ArrayList<String> pictures) {
        this.pictures = pictures;
    }

    public ArrayList<java.lang.Integer> getOwner() {
        return owner;
    }

    public void setOwner(ArrayList<java.lang.Integer> owner) {
        this.owner = owner;
    }

    public int getParticipant() {
        return participant;
    }

    public void setParticipant(int participant) {
        this.participant = participant;
    }

    public ArrayList<java.lang.Integer> getApplicants() {
        return applicants;
    }

    public void setApplicants(ArrayList<java.lang.Integer> applicants) {
        this.applicants = applicants;
    }
}
