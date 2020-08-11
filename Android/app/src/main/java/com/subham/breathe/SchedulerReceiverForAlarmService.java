package com.subham.breathe;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class SchedulerReceiverForAlarmService extends BroadcastReceiver {
    public static final int REQUEST_CODE = 12345;
    public static final String ACTION = "com.subham.breathe.schedulebreak";

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, SchedulerServiceViaIntentService.class);
        i.putExtra("foo", "bar");
        context.startService(i);
    }
}
