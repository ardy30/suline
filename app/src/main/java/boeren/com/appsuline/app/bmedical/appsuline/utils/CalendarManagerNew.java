package boeren.com.appsuline.app.bmedical.appsuline.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import java.util.Calendar;

import boeren.com.appsuline.app.bmedical.appsuline.views.MyAlarmEventReceiver;

public class CalendarManagerNew {


    public void addRepeatingReminder(Context context,Calendar calendar, long eventId) {


        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute=calendar.get(Calendar.MINUTE);
        calendar.set(Calendar.SECOND, 0);
        Intent myIntent = new Intent(context, MyAlarmEventReceiver.class);
        myIntent.putExtra("event_id", eventId);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int)eventId, myIntent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        Log.e("addRepeatingReminder", eventId + "# " + hour + ":" + minute);
        Log.e("addRepeatingReminder", "Difference In Millis: "+(calendar.getTimeInMillis()-Calendar.getInstance().getTimeInMillis()));

        //alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 10000, pendingIntent);
        if(Build.VERSION.SDK_INT>=19){

            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }else{
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }


    public void addReminder(Context context, int day, int month, int year, int hour, int minute, long eventId) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        Intent myIntent = new Intent(context, MyAlarmEventReceiver.class);
        myIntent.putExtra("event_id", eventId);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int)eventId, myIntent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }


}
