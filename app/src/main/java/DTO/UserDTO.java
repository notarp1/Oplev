package DTO;

import java.util.ArrayList;

public class UserDTO {

    int userId, age;
    String fName, lName, city, email, phone;
    ArrayList<String> pictures;
    ArrayList<EventDTO> events, joinedEvents;

    public int getUserId(){
        return userId;
    }

    public String getfName() {
        return fName;
    }

    public String getlName() {
        return lName;
    }

    public int getAge() {
        return age;
    }

    public String getCity() {
        return city;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public ArrayList<String> getPictures() {
        return pictures;
    }

    public ArrayList<EventDTO> getEvents() {
        return events;
    }

    public ArrayList<EventDTO> getJoinedEvents() {
        return joinedEvents;
    }

    public void  setUserId(int userId){
        this.userId = userId;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPictures(ArrayList<String> pictures) {
        this.pictures = pictures;
    }

    public void setEvents(ArrayList<EventDTO> events) {
        this.events = events;
    }

    public void setJoinedEvents(ArrayList<EventDTO> joinedEvents) {
        this.joinedEvents = joinedEvents;
    }
}
