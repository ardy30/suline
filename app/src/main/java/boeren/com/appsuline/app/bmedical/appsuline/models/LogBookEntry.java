package boeren.com.appsuline.app.bmedical.appsuline.models;


import java.io.Serializable;

public class LogBookEntry implements Serializable{
    private long userId=-1;
    private long entryId=-1;
    private String entryName="";
    private String entryDate="";
    private String entryTime="";
    private String entryDuration="";
    private double entryAmount;
    private String entryComment="";
    private Type entryType;
    private boolean isFavourite;
    private boolean isLongInsulin;

    public boolean isLongInsulin() {
        return isLongInsulin;
    }

    public void setLongInsulin(boolean isLongInsulin) {
        this.isLongInsulin = isLongInsulin;
    }
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
    public String getEntryName() {
        return entryName;
    }

    public void setEntryName(String entryName) {
        this.entryName = entryName;
    }

    public String getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(String entryTime) {
        this.entryTime = entryTime;
    }

    public String getEntryDuration() {
        return entryDuration;
    }

    public void setEntryDuration(String entryDuration) {
        this.entryDuration = entryDuration;
    }

    public double getEntryAmount() {
        return entryAmount;
    }

    public void setEntryAmount(double entryAmount) {
        this.entryAmount = entryAmount;
    }

    public String getEntryComment() {
        return entryComment;
    }

    public void setEntryComment(String entryComment) {
        this.entryComment = entryComment;
    }

    public Type getEntryType() {
        return entryType;
    }

    public void setEntryType(Type entryType) {
        this.entryType = entryType;
    }


    public long getEntryId() {
        return entryId;
    }

    public void setEntryId(long entryId) {
        this.entryId = entryId;
    }
    public String getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;

    }
    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean isFavourite) {
        this.isFavourite = isFavourite;
    }


    public static enum Type{
        INSULIN,
        BLOOD,
        ACTIVITY,
        MEAL,
        BREAK_FAST,
        SNACK,
        LUNCH,
        DRINK,
        DINNER,
        DATE_INFO,
        NON;
    }
}
