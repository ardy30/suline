package boeren.com.appsuline.app.bmedical.appsuline.models;


import java.io.Serializable;


public class DateInfo implements Serializable {

    private String dateString;
    private boolean isFavourite;
    private float amount;
    private float totalEntries=1;


    private String comment;

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean isFavourite) {
        this.isFavourite = isFavourite;
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    public float getTotalEntries() {
        return totalEntries;
    }

    public void setTotalEntries(float totalEntries) {
        this.totalEntries = totalEntries;
    }
}
