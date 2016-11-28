package boeren.com.appsuline.app.bmedical.appsuline.utils;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Jamil on 12-1-2015.
 */
public class Utilities
{
    private static Utilities utilities=new Utilities();
    private SimpleDateFormat timeFormat;
    Context context;

    public Utilities( Context context )
    {
        this.context = context;
    }
    private Utilities()
    {
        this.timeFormat=new SimpleDateFormat(Constants.TIME_FORMAT);
    }

    public static Utilities getInstance(){
        return utilities;
    }

    public String getCurrentFormattedTime(){
        Calendar calendar=Calendar.getInstance();
        return timeFormat.format(calendar.getTime());
    }
    public String getFormattedTime(int hourOfDay, int minute){
        Calendar calendar=Calendar.getInstance(Locale.getDefault());
        calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
        calendar.set(Calendar.MINUTE,minute);
        return timeFormat.format(calendar.getTime());
    }

    public static String formatInputDate(String inputDate,String outPutFormat){
        Calendar calNow=Calendar.getInstance();
        calNow.setTimeInMillis(Long.parseLong(inputDate));
        DateFormat format=new SimpleDateFormat(outPutFormat);
        String date=format.format(calNow.getTime());
        return date;
    }

    public void showCurrentTimePickerDialog(Context context,TimePickerDialog.OnTimeSetListener timeSetListener){
        Calendar calendar=Calendar.getInstance();
        showTimePickerDialog(context,calendar,timeSetListener,true);
    }

    public void showTimePickerDialogWithTime(Context context,String time,TimePickerDialog.OnTimeSetListener timeSetListener) throws ParseException {
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(timeFormat.parse(time));

        showTimePickerDialog(context,calendar,timeSetListener,true);
    }

    public void showTimePickerDialog(Context context,Calendar calendar,TimePickerDialog.OnTimeSetListener timeSetListener,boolean is24HourView){
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog tpd = new TimePickerDialog(context,timeSetListener, hour, minute, true);
        tpd.show();
    }

    public void showDatePickerDialog(Context context,Calendar calendar,DatePickerDialog.OnDateSetListener dateSetListener){
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth=calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(context,dateSetListener,year,month,dayOfMonth);
        datePickerDialog.show();
    }
    public void saveValueToSharedPrefs(String key, String value)
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();

    }

    public String getSharedPrefValue(String key)
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String language = preferences.getString(key, "");
        return language;
    }
}
