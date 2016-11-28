package boeren.com.appsuline.app.bmedical.appsuline.controllers;


import android.content.Context;
import android.util.Log;

import boeren.com.appsuline.app.bmedical.appsuline.database.DatabaseManager;
import boeren.com.appsuline.app.bmedical.appsuline.models.CalendarData;
import boeren.com.appsuline.app.bmedical.appsuline.models.LogBookEntry;
import boeren.com.appsuline.app.bmedical.appsuline.models.User;

public class BaseController {
    private static BaseController baseController = new BaseController();
    private DatabaseManager dbManager;
    private BaseController() {}

    private User activeUser;
    private CalendarData selectedCalendarData;
    private LogBookEntry.Type selectedCalendarType;

    public static BaseController getInstance()
    {
        return baseController;
    }

    public DatabaseManager getDbManager(Context context) {
        if(null==dbManager){
            dbManager=new DatabaseManager(context);
        }
        return dbManager;
    }

    public User getActiveUser() {
        return activeUser;
    }

    public void setActiveUser(User activeUser) {
        this.activeUser = activeUser;
    }

    public CalendarData getSelectedCalendarData()
    {
        Log.i("","Getting: "+this.toString());
        return selectedCalendarData;
    }

    public void setSelectedCalendarData(CalendarData selectedCalendarData) {
        this.selectedCalendarData = selectedCalendarData;
        Log.i("","Setting: "+this.toString());
    }

    public LogBookEntry.Type getSelectedCalendarType() {
        return selectedCalendarType;
    }

    public void setSelectedCalendarType(LogBookEntry.Type selectedCalendarType) {
        this.selectedCalendarType = selectedCalendarType;
    }
}
