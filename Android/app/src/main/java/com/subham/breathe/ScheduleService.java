package com.subham.breathe;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class ScheduleService extends JobService {

    private static final String TAG = "JobService";
    private  boolean stopped;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.d(TAG, "service started");
//        Intent service = new Intent(getApplicationContext(), SchedulerReceiver.class);
        // TODO pass interval

//        getApplicationContext().startService(service);
//        BreakJob.scheduleJob(getApplicationContext(), 30); // reschedule the job
        doInBackground(jobParameters);
        return true;
    }

    private void doInBackground(final JobParameters params) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                if (stopped) return;
                Log.d(TAG, "job in progress");
                showNotification();
                Log.d(TAG, "job finished");
            }

        }).start();
        jobFinished(params, false);
    }

    private void showNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),
                "7979")
                .setSmallIcon(R.drawable.circle_toggle_button)
                .setContentTitle("Breathe")
                .setContentText("Take Break")
                .setPriority(NotificationCompat.FLAG_NO_CLEAR)
                // Set the intent that will fire when the user taps the notification
//                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        createNotificationChannel(getApplicationContext());
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
        notificationManager.notify(getTimeSeconds(), builder.build());

    }

    private static int getTimeSeconds() {
        // return new Random().nextInt();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM HH:mm:ss");
        Date date = new Date();
        String dateTimeString = formatter.format(date);
        String dateString = dateTimeString.split(" ")[0];
        dateString = TrimZeroInData(dateString);
        String timeString = dateTimeString.split(" ")[1];
        return new Random().nextInt()*Integer.parseInt(getIntegerString(dateString, "/").concat(getIntegerString(timeString, ":")));
    }

    private static String getIntegerString(String sequence, String delimiter)  {
        String[] intPieces = sequence.split(delimiter);
        StringBuilder intString = new StringBuilder();
        for (String intPiece :
                intPieces) {
            intString.append(intPiece);
        }
        return intString.toString();
    }

    @NonNull
    private static String TrimZeroInData(String dateString) {
        if (dateString.charAt(0) == '0') {
            dateString = dateString.substring(1);
        }
        return dateString;
    }

    public static void createNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Breathe";
            String description = "Take Break";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("7979", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
        context.startActivity(new Intent(context, BreakSplash.class));
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.d(TAG, "job cancelled before completion");
        stopped = true;
        return false;
    }
}
