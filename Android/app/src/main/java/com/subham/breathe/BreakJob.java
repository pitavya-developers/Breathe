package com.subham.breathe;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;



public class BreakJob {

    private static String TAG = "Breakjob";
    private Context context;
    
    BreakJob(Context context) {
        this.context = context;
    }


    private boolean satisfyBreakCriteria() {

        ConfigPersistanceStorage configPersistanceStorage = new ConfigPersistanceStorage(this.context);

        /*
        if(configPersistanceStorage.getFirstTimeActivated()) {
            return true;
        }
         */

        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK) - 1; // weekday starts with 0 and calender with 1

        Time currentTime = new Time(mHour, mMinute);
        Time startTime = configPersistanceStorage.getStartTime();
        Time   endTime = configPersistanceStorage.getEndTime();

        if (configPersistanceStorage.getWeekDays().indexOf(dayOfWeek) != -1) {
            return currentTime.compare(currentTime, startTime) > -1
                    && currentTime.compare(currentTime, endTime) < 1;
        }

        return false;
    }

    public void showNotification() {
//        if (! satisfyBreakCriteria()) {
//            return;
//        }
        Intent intent = new Intent(this.context, BreakSplash.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this.context, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this.context,
                this.context.getString(R.string.notification_channel_id))
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(this.context.getString(R.string.notification_notification_name))
                .setContentText(this.context.getString(R.string.notification_notification_desc))
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setAutoCancel(true)
                .setFullScreenIntent(pendingIntent, true);
        createNotificationChannel(this.context);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this.context);
        notificationManager.notify(getTimeSeconds(), builder.build());

        new MusicPlayer().play(this.context, R.raw.pristine);
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
            CharSequence name = "Break Time";
            String description = "Your new break activity is ready";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("Break Time", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}
