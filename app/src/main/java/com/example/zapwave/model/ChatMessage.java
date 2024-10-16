package com.example.zapwave.model;

import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class ChatMessage {

    String senderID;
    String text;
    long time;
    boolean isMine;

    public ChatMessage() {
    }

    public ChatMessage(String senderID, String text, long time) {
        this.senderID = senderID;
        this.text = text;
        this.time = time;
    }

    public boolean isMine(){
        return senderID.equals(FirebaseAuth.getInstance().getCurrentUser().getUid());
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setMine(boolean mine) {
        isMine = mine;
    }

    public String convertTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");  // Use 'mm' for minutes
        Date date = new Date(getTime());
        sdf.setTimeZone(TimeZone.getDefault());
        return sdf.format(date);
    }

}
