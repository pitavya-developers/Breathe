package com.subham.breathe;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

public class AlarmServiceReciever extends BroadcastReceiver {
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {
        // recieve any setting or anyData
        //anyObject = intent.getExtras().getString("anyString");
        Log.e("onReceive: ", "---------------------------------------------------------------");

        BreakJob.scheduleJob(context, intent.getIntExtra("breakTime", 30));

    }
}
