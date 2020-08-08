package com.subham.breathe;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

public class SchedulerReceiver extends BroadcastReceiver {
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "broadcast", Toast.LENGTH_SHORT).show();
        BreakJob.scheduleJob(context, intent.getIntExtra("breakTime", 30));
    }
}
