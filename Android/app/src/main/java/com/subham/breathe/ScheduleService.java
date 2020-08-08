package com.subham.breathe;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

public class ScheduleService extends JobService {
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Intent service = new Intent(getApplicationContext(), SchedulerReceiver.class);
        // TODO pass interval
        getApplicationContext().startService(service);
        BreakJob.scheduleJob(getApplicationContext(), 30); // reschedule the job
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }
}
