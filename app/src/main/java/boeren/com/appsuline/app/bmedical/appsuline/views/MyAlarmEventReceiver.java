package boeren.com.appsuline.app.bmedical.appsuline.views;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import boeren.com.appsuline.app.bmedical.appsuline.utils.MyAlarmService;

/**
 * Created by Fahad Nasrullah on 03/03/15.
 */
public class MyAlarmEventReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service1 = new Intent(context, MyAlarmService.class);
        service1.putExtra("event_id", intent.getLongExtra("event_id", 0));
        Log.e("MyAlarmReceiver", intent.getLongExtra("event_id", 0) + "@");
        context.startService(service1);

    }
}
