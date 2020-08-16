package com.subham.breathe;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import ca.antonious.materialdaypicker.MaterialDayPicker;

public class AlarmServiceReciever extends BroadcastReceiver {


    private static Intent createBreakSplashIntent() {
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
                | Intent.FLAG_ACTIVITY_NO_USER_ACTION);
        intent.setClassName("com.subham.breathe", BreakSplash.class.getName());
        return intent;
    }

    private boolean satisfyBreakCriteria(Context context) {

        ConfigPersistanceStorage configPersistanceStorage = new ConfigPersistanceStorage(context);

        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK) - 1; // weekday starts with 0 and calender with 1

        Time currentTime = new Time(mHour, mMinute);
        Time startTime = configPersistanceStorage.getStartTime();
        Time endTime = configPersistanceStorage.getEndTime();

        if (configPersistanceStorage.getWeekDays().indexOf(MaterialDayPicker.Weekday.values()[dayOfWeek]) != -1) {
            return currentTime.compare(currentTime, startTime) > -1
                    && currentTime.compare(currentTime, endTime) < 1;
        }

        return false;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        new Thread(() -> {
            showNotification(context);
        }).start();
    }

    private void showNotification(Context context) {
        if (!satisfyBreakCriteria(context)) {
            return;
        }
        Intent intent = new Intent(context, BreakSplash.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                AlarmServiceReciever.createBreakSplashIntent(), 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,
                context.getString(R.string.notification_channel_id))
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(context.getString(R.string.notification_notification_name))
                .setContentText(context.getString(R.string.notification_notification_desc))
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setOngoing(true)
                .setAutoCancel(true)
                .setCategory(Notification.CATEGORY_CALL)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .addAction(R.drawable.logo, "Reject", null)
                .addAction(R.drawable.notification_icon, "Answer", null)
                .setContentIntent(pendingIntent)
                .setFullScreenIntent(pendingIntent, true);

        builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE), AudioManager.STREAM_RING);
        builder.setVibrate(new long[]{500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500});

        createNotificationChannel(context);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(getTimeSeconds(), builder.build());
//        new MusicPlayer().play(context, R.raw.pristine);

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
