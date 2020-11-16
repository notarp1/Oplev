package DTO;

import java.util.ArrayList;
import java.util.Date;

import DAL.Classes.ChatDAO;

public class ChatDTO {

    private String chatId;
    private ArrayList<Date> dates;
    private ArrayList<String> messages, sender, receiver;

    public ChatDTO() {

    }

    public ChatDTO(ArrayList<String> sender, ArrayList<String> messages , String chatId, ArrayList<Date> dates, ArrayList<String> receiver) {
        this.chatId = chatId;
        this.dates = dates;
        this.messages = messages;
        this.sender = sender;
        this.receiver = receiver;
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
