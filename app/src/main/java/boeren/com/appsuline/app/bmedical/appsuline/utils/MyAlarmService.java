package boeren.com.appsuline.app.bmedical.appsuline.utils;



import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import java.util.Calendar;

import boeren.com.appsuline.app.bmedical.appsuline.MainActivity;
import boeren.com.appsuline.app.bmedical.appsuline.R;
import boeren.com.appsuline.app.bmedical.appsuline.controllers.BaseController;
import boeren.com.appsuline.app.bmedical.appsuline.models.CalendarEvent;
import boeren.com.appsuline.app.bmedical.appsuline.models.User;


public class MyAlarmService extends Service {

    private NotificationManagerCompat mManager;
   // public static final int NOTIFICATION_ID = 1;
   private String eventtitle;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        showNotification(intent);
        return START_NOT_STICKY;
    }
   private void setEventTitle(CalendarEvent event){
       if(event.getEventCategory() == 0)
           eventtitle = getString(R.string.eventbloodleveltitle);
       if(event.getEventCategory() == 1)
           eventtitle = getString(R.string.eventinjectiontitle);
       if(event.getEventCategory() == 2)
           eventtitle = getString(R.string.eventsnacktitle);
       if(event.getEventCategory() == 3)
           eventtitle = getString(R.string.eventproductordertitle);
   }
    private void showNotification(Intent intent) {
        Log.e("MyAlarmService", intent.getLongExtra("event_id", 0) + "@");
        intent.getExtras();
        CalendarEvent event = getEventDetail(intent.getLongExtra("event_id", 0));
        setEventTitle(event);
        if (event.getEventCategory() == 3) {
            showNotification(event);
        } else {
            Calendar calendar = Calendar.getInstance();
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            switch (dayOfWeek) {
                case Calendar.MONDAY:
                    if (event.isMandage()) {
                        showNotification(event);
                    }
                    break;
                case Calendar.TUESDAY:
                    if (event.isDinsdag()) {
                        showNotification(event);
                    }
                    break;
                case Calendar.WEDNESDAY:
                    if (event.isWoensdag()) {
                        showNotification(event);
                    }
                    break;
                case Calendar.THURSDAY:
                    if (event.isDonderdag()) {
                        showNotification(event);
                    }
                    break;
                case Calendar.FRIDAY:
                    if (event.isVrijdag()) {
                        showNotification(event);
                    }
                    break;
                case Calendar.SATURDAY:
                    if (event.isZaterdag()) {
                        showNotification(event);
                    }
                    break;
                case Calendar.SUNDAY:
                    if (event.isZondage()) {
                        showNotification(event);
                    }
                    break;
            }
        }
    }

    private CalendarEvent getEventDetail(long eventId) {
        User activeUser = BaseController.getInstance().getDbManager(this).getUserTable().getActiveUser();
        return BaseController.getInstance().getDbManager(this).getEventsTable().getCalenderEvent(activeUser.getUserId(), eventId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    private void showNotification(CalendarEvent event) {
        mManager = NotificationManagerCompat.from(this);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.ic_launcher);
        Intent intent = new Intent(this.getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, (int)event.getEventID(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));
        builder.setContentTitle(getString(R.string.app_name));

        builder.setContentText(event.getEventTitle());

        builder.setContentText(eventtitle);

        builder.setLights(Color.BLUE, 500, 500);
        long[] pattern = {500,500,500,500,500,500,500,500,500};
        builder.setVibrate(pattern);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(alarmSound);
        mManager.notify((int)event.getEventID(), builder.build());

        if(Build.VERSION.SDK_INT>=19){
            CalendarManagerNew calendarManagerNew=new CalendarManagerNew();
            Calendar calendar=Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_YEAR,1);
            calendarManagerNew.addRepeatingReminder(this,calendar,event.getEventID());
        }
        stopSelf();
    }

}
