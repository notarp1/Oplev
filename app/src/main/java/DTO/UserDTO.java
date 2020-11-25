package DTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class UserDTO implements Serializable {

    int age;
    String fName, lName, city, email, phone, description, userId, job, education;

    ArrayList<String> pictures = new ArrayList<>(Arrays.asList(null, null, null, null, null, null));

    ArrayList<String> chatId;
    ArrayList<Integer> events, joinedEvents;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getEducation() {
        return education;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<String> getPictures() {

        return pictures;
    }

    public ArrayList<String> getChatId() {
        return chatId;
    }

    public void setChatId(ArrayList<String> chatId) {
        this.chatId = chatId;
    }

    public void setPictures(ArrayList<String> pictures) {
        this.pictures = pictures;
    }

    public ArrayList<Integer> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Integer> events) {
        this.events = events;
    }

    public ArrayList<Integer> getJoinedEvents() {
        return joinedEvents;
    }

    public void setJoinedEvents(ArrayList<Integer> joinedEvents) {
        this.joinedEvents = joinedEvents;
    }
}
