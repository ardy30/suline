package boeren.com.appsuline.app.bmedical.appsuline.models;

import java.io.Serializable;
import java.util.Date;

public class CalendarData implements Serializable{
    private DateInfo dateInfo;
    private Date calendarDate;
    private DataType type=DataType.DAY;


    public Date getCalendarDate() {
        return calendarDate;
    }

    public void setCalendarDate(Date calendarDate) {
        this.calendarDate = calendarDate;
    }

    public static enum Type{DATA_ITEM,FOOTER_ITEM;}
    public DateInfo getDateInfo() {
        return dateInfo;
    }

    public void setDateInfo(DateInfo dateInfo) {
        this.dateInfo = dateInfo;
    }

    public DataType getType() {
        return type;
    }

    public void setType(DataType type) {
        this.type = type;
    }

    public static enum  DataType{
        MONTH,DAY;
    }
}
