package com.subham.breathe;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

class BreakService {

    AlarmManager manager;
    PendingIntent pendingIntent;
    Context context;
    private String TAG = "break service";

    BreakService(Context context) {
        this.context = context;
    }

    public void startBreakService(int breakTimeMinutes) {
        Intent alarmIntent = new Intent(this.context, AlarmServiceReciever.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.context, 100, alarmIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        long interval = breakTimeMinutes * 60 * 1000;
        manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
        Log.e(TAG, "service started " + breakTimeMinutes + "minutes");
    }

    public void stopBreakService() {
        if (manager != null) {
            manager.cancel(pendingIntent);
            Log.e(TAG, "service stopped");
        }
    }
}
