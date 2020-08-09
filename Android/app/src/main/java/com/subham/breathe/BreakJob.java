package com.subham.breathe;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import static android.content.Context.JOB_SCHEDULER_SERVICE;

public class BreakJob {

    private static String TAG = "Breakjob";

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void scheduleJob(Context context, int time) {
        ComponentName serviceComponent = new ComponentName(context, ScheduleService.class);

        JobInfo jobInfo
                = new JobInfo.Builder(7979, serviceComponent)
//                .setMinimumLatency(1 * 1000)
//                .setOverrideDeadline(3 * 1000)
//                TODO uncomment me
//                .setPeriodic(config.breakTimeInMinutes.time * 1000)
                .setPeriodic(15 * 60 * 1000)
//                .setMinimumLatency(5000)
                .setPersisted(true).build();
        //builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED); // require unmetered network
        //builder.setRequiresDeviceIdle(true); // device should be idle
        //builder.setRequiresCharging(false); // we don't care if the device is charging or not
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(JOB_SCHEDULER_SERVICE);
        int resultCode = jobScheduler.schedule(jobInfo);

        if (resultCode == JobScheduler.RESULT_SUCCESS) {
            Log.d(TAG, "job scheduled");
        }
        else{
            Log.d(TAG, "job scheduled failed");
        }

    }

}
