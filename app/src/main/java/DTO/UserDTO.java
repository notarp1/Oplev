package DTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class UserDTO implements Serializable {

    int age;
    String fName, lName, city, email, userPicture, description, userId, job, education;

    ArrayList<String> pictures = new ArrayList<>(Arrays.asList(null, null, null, null, null, null));

    ArrayList<String> chatId, events, requestedEvents, likedeEvents;

    public String getUserId() {
        return userId;
    }

    public UserDTO setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public int getAge() {
        return age;
    }

    public UserDTO setAge(int age) {
        this.age = age;
        return this;
    }


    public String getDescription() {
        return description;
    }

    public UserDTO setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getJob() {
        return job;
    }

    public UserDTO setJob(String job) {
        this.job = job;
        return this;
    }

    public UserDTO setEducation(String education) {
        this.education = education;
        return this;
    }

    public void setUserPicture(String userPicture) {
        this.userPicture = userPicture;
    }

    public String getUserPicture() {
        return userPicture;
    }

    public String getEducation() {
        return education;
    }

    public String getfName() {
        return fName;
    }

    public UserDTO setfName(String fName) {
        this.fName = fName;
        return this;
    }

    public String getlName() {
        return lName;
    }

    public UserDTO setlName(String lName) {
        this.lName = lName;
        return this;
    }

    public String getCity() {
        return city;
    }

    public UserDTO setCity(String city) {
        this.city = city;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserDTO setEmail(String email) {
        this.email = email;
        return this;
    }

    public ArrayList<String> getPictures() {

        return pictures;
    }

    public ArrayList<String> getChatId() {
        return chatId;
    }

    public UserDTO setChatId(ArrayList<String> chatId) {
        this.chatId = chatId;
        return this;
    }

    public UserDTO setPictures(ArrayList<String> pictures) {
        this.pictures = pictures;
        return this;
    }

    public ArrayList<String> getEvents() {
        return events;
    }

    public UserDTO setEvents(ArrayList<String> events) {
        this.events = events;
        return this;
    }

    public ArrayList<String> getRequestedEvents() {
        return requestedEvents;
    }

    public UserDTO setRequestedEvents(ArrayList<String> requestedEvents) {
        this.requestedEvents = requestedEvents;
        return this;
    }

    public ArrayList<String> getLikedeEvents() {
        return likedeEvents;
    }

    public void setLikedeEvents(ArrayList<String> likedeEvents) {
        this.likedeEvents = likedeEvents;
    }
}
