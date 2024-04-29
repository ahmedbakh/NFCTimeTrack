package org.timetrack.model;

public class CardEvent {
    private String owner;
    private String uid;
    private String date;
    private String eventType;

    public CardEvent(String owner, String uid, String date) {
        this.owner = owner;
        this.uid = uid;
        this.date = date;
    }

    // Getters and Setters
    public String getOwner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }
    public String getUid() { return uid; }
    public void setUid(String uid) { this.uid = uid; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String  getEventType() { return eventType;}
    public void setEventType(String uid) { this.eventType = eventType; }

}
