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
        Intent alarmIntent = new Intent(this.context, AlarmServiceReciever.class);
        this.pendingIntent = PendingIntent.getBroadcast(this.context, 100, alarmIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        this.manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

    }

    public void startBreakService(int breakTimeMinutes) {
        long interval = breakTimeMinutes * 60 * 1000;
        this.manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval,
                this.pendingIntent);
        Log.e(TAG, "service started " + breakTimeMinutes + "minutes");
    }

    public void stopBreakService() {
        if (this.manager != null && this.pendingIntent != null) {
            this.manager.cancel(this.pendingIntent);
            Log.e(TAG, "service stopped");
        }
    }
}
