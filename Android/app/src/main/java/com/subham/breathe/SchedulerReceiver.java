package com.subham.breathe;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import static android.content.Context.JOB_SCHEDULER_SERVICE;

public class SchedulerReceiver extends BroadcastReceiver {
    private String TAG = "broadcast";

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "starting breathe", Toast.LENGTH_SHORT).show();
        startBreakService(context);
    }

    private void startBreakService(Context ctx) {

        ComponentName serviceComponent = new ComponentName(ctx, ScheduleService.class);
        int breakTime = new ConfigPersistanceStorage(ctx).getBreakTime().time * 60 * 1000;
        JobInfo jobInfo
                = new JobInfo.Builder(7979, serviceComponent)
                .setPeriodic(breakTime)
                .setPersisted(true).build();
        JobScheduler jobScheduler = (JobScheduler) ctx.getSystemService(JOB_SCHEDULER_SERVICE);
        int resultCode = jobScheduler.schedule(jobInfo);

        if (resultCode == JobScheduler.RESULT_SUCCESS) {
            Log.d(TAG, "job scheduled");
        } else {
            Log.d(TAG, "job scheduled failed");
        }
    }
}
