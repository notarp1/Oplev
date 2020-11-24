package DTO;

import android.net.Uri;

import java.util.ArrayList;
import java.util.Date;

public class ChatDTO {

    private String chatId;
    private ArrayList<Date> dates;
    private ArrayList<String> messages, sender, receiver;
    private ArrayList<Uri> pictures;

    public ChatDTO() {

    }

    public ChatDTO(ArrayList<String> sender, ArrayList<String> messages , String chatId, ArrayList<Date> dates, ArrayList<String> receiver, ArrayList<Uri> pictures) {
        this.chatId = chatId;
        this.dates = dates;
        this.messages = messages;
        this.sender = sender;
        this.receiver = receiver;
        this.pictures = pictures;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public ArrayList<Date> getDates() {
        return dates;
    }

    public void setDates(ArrayList<Date> dates) {
        this.dates = dates;
    }

    public ArrayList<String> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<String> messages) {
        this.messages = messages;
    }

    public ArrayList<String> getSender() {
        return sender;
    }

    public void setSender(ArrayList<String> sender) {
        this.sender = sender;
    }

    public ArrayList<String> getReceiver() {
        return receiver;
    }

    public void setReceiver(ArrayList<String> receiver) {
        this.receiver = receiver;
    }


    // Den her bruges kun når man henter ind fra firestore af fordi at man skal gemme URI'sne som strings i databasen
    public void setPictures(ArrayList<String> pics){
        ArrayList<Uri> tempUri = new ArrayList<>();
        for (String s: pics) {
            tempUri.add(Uri.parse(s));
        }
        this.pictures = tempUri;
    }

    public void setPics(ArrayList<Uri> pics){
        this.pictures = pics;
    }

    public ArrayList<Uri> getPictures(){
        return pictures;
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
