package com.subham.breathe;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class RebootAlarmServiceReciever extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        new Thread(() -> {
            new BreakService(context).startBreakService(new ConfigPersistanceStorage(context).getBreakTime().time);
        }).start();
    }

}
