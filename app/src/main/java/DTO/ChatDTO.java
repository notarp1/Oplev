package DTO;

import android.net.Uri;

import java.util.ArrayList;
import java.util.Date;

public class ChatDTO {

    private String chatId, header, user1, user2;
    private ArrayList<Date> dates;
    private ArrayList<String> messages, sender, receiver;
    private ArrayList<Uri> pictures;

    // Firestore skal bruge en tom konstruktør til at bruge toObject på
    public ChatDTO() {

    }

    public ChatDTO(ArrayList<String> sender, ArrayList<String> messages , String chatId, ArrayList<Date> dates, ArrayList<String> receiver, ArrayList<Uri> pictures, String header, String user1, String user2) {
        this.chatId = chatId;
        this.dates = dates;
        this.messages = messages;
        this.sender = sender;
        this.receiver = receiver;
        this.pictures = pictures;
        this.header = header;
        this.user1 = user1;
        this.user2 = user2;
    }

    public String getChatId() {
        return chatId;
    }

    public ChatDTO setChatId(String chatId) {
        this.chatId = chatId;
        return this;
    }

    public ArrayList<Date> getDates() {
        return dates;
    }

    public ChatDTO setDates(ArrayList<Date> dates) {
        this.dates = dates;
        return this;
    }

    public ArrayList<String> getMessages() {
        return messages;
    }

    public ChatDTO setMessages(ArrayList<String> messages) {
        this.messages = messages;
        return this;
    }

    public ArrayList<String> getSender() {
        return sender;
    }

    public ChatDTO setSender(ArrayList<String> sender) {
        this.sender = sender;
        return this;
    }

    public ArrayList<String> getReceiver() {
        return receiver;
    }

    public ChatDTO setReceiver(ArrayList<String> receiver) {
        this.receiver = receiver;
        return this;
    }


    // Den her bruges kun når man henter ind fra firestore af fordi at man skal gemme URI'sne som strings i databasen
    public ChatDTO setPictures(ArrayList<String> pics){
        ArrayList<Uri> tempUri = new ArrayList<>();
        for (String s: pics) {
            tempUri.add(Uri.parse(s));
        }
        this.pictures = tempUri;
        return this;
    }

    public ChatDTO setPics(ArrayList<Uri> pics){
        this.pictures = pics;
        return this;
    }

    public ArrayList<Uri> getPictures(){
        return pictures;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getUser1() {
        return user1;
    }

    public void setUser1(String user1) {
        this.user1 = user1;
    }

    public String getUser2() {
        return user2;
    }

    public void setUser2(String user2) {
        this.user2 = user2;
    }

    @Override
    public String toString() {
        return "ChatDTO{" +
                "chatId='" + chatId + '\'' +
                ", dates=" + dates +
                ", messages=" + messages +
                ", sender=" + sender +
                ", receiver=" + receiver +
                '}';
    }
}
