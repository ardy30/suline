package boeren.com.appsuline.app.bmedical.appsuline.models;

import java.io.Serializable;


public class CalendarEvent implements Serializable {
    long eventID;
    long userID;
    long calenderEventID;
    String eventTitle;
    String eventEndDate;
    String eventDescription;
    String setEventStartTime;
    String eventEndTime;
    int eventCategory;
    boolean isMandage;
    boolean isDinsdag;
    boolean isWoensdag;
    boolean isDonderdag;
    boolean isVrijdag;
    boolean isZaterdag;
    boolean isZondage;

    public void copy(CalendarEvent event)
    {
        this.setEventID(event.getEventID());
        this.setUserID(event.getUserID());
        this.setCalenderEventID(event.getCalenderEventID());
        this.setEventTitle(event.getEventTitle());
        this.setEventEndDate(event.getEventEndDate());
        this.setEventDescription(event.getEventDescription());
        this.setEventStartTime(event.getSetEventStartTime());
        this.setEventEndTime(event.getEventEndTime());
        this.setEventCategory(event.getEventCategory());
        this.setMandage(event.isMandage());
        this.setDinsdag(event.isDinsdag());
        this.setWoensdag(event.isWoensdag());
        this.setDonderdag(event.isDonderdag());
        this.setVrijdag(event.isVrijdag());
        this.setZaterdag(event.isZaterdag());
        this.setZondage(event.isZondage());
    }

    public CalendarEvent() {
    }


    public long getCalenderEventID() {
        return calenderEventID;
    }

    public void setCalenderEventID(long calenderEventID) {
        this.calenderEventID = calenderEventID;
    }

    public long getEventID() {
        return eventID;
    }

    public long getUserID() {
        return userID;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public int getEventCategory() {
        return eventCategory;
    }

    public String getEventEndDate() {
        return eventEndDate;
    }

    public String getEventEndTime() {
        return eventEndTime;
    }

    public void setEventID(long eventID) {
        this.eventID = eventID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public void setEventCategory(int eventCategory) {
        this.eventCategory = eventCategory;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public void setEventEndDate(String eventStartTime) {
        this.eventEndDate = eventStartTime;
    }

    public void setEventEndTime(String eventEndTime) {
        this.eventEndTime = eventEndTime;
    }

    public boolean isMandage() {
        return isMandage;
    }

    public void setMandage(boolean isMandage) {
        this.isMandage = isMandage;
    }

    public boolean isDinsdag() {
        return isDinsdag;
    }

    public void setDinsdag(boolean isDinsdag) {
        this.isDinsdag = isDinsdag;
    }

    public boolean isWoensdag() {
        return isWoensdag;
    }

    public void setWoensdag(boolean isWoensdag) {
        this.isWoensdag = isWoensdag;
    }

    public boolean isDonderdag() {
        return isDonderdag;
    }

    public void setDonderdag(boolean isDonderdag) {
        this.isDonderdag = isDonderdag;
    }

    public boolean isVrijdag() {
        return isVrijdag;
    }

    public void setVrijdag(boolean isVrijdag) {
        this.isVrijdag = isVrijdag;
    }

    public boolean isZaterdag() {
        return isZaterdag;
    }

    public void setZaterdag(boolean isZaterdag) {
        this.isZaterdag = isZaterdag;
    }

    public boolean isZondage() {
        return isZondage;
    }

    public void setZondage(boolean isZondage) {
        this.isZondage = isZondage;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getSetEventStartTime() {
        return setEventStartTime;
    }

    public void setEventStartTime(String setEventStartTime) {
        this.setEventStartTime = setEventStartTime;
    }

}
