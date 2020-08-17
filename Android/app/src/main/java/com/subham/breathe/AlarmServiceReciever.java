package com.subham.breathe;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class AlarmServiceReciever extends BroadcastReceiver {

    private boolean satisfyBreakCriteria(Context context) {

        ConfigPersistanceStorage configPersistanceStorage = new ConfigPersistanceStorage(context);

        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

        Time currentTime = new Time(mHour, mMinute);
        Time startTime = configPersistanceStorage.getStartTime();
        Time endTime = configPersistanceStorage.getEndTime();

        if (configPersistanceStorage.getWeekDays().indexOf(dayOfWeek) != -1) {
            return currentTime.compare(currentTime, startTime) > -1
                    && currentTime.compare(currentTime, endTime) < 1;
        }

        return false;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        new Thread(() -> AlarmServiceReciever.this.showNotification(context)).start();
    }

    private void showNotification(Context context) {
        if (!satisfyBreakCriteria(context)) {
            return;
        }
        Intent fullScreenIntent = new Intent(context, BreakSplash.class);
        fullScreenIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(context, 0,
                fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        Uri sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                "://" + context.getPackageName() + "/" + R.raw.pristine);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Notification.Builder builder = new Notification.Builder(context,
                    context.getString(R.string.notification_channel_id))
                    .setSmallIcon(R.drawable.notification_icon)
                    .setContentTitle(context.getString(R.string.notification_notification_name))
                    .setContentText(context.getString(R.string.notification_notification_desc))
                    .setOngoing(true)
                    .setAutoCancel(true)
                    .setCategory(Notification.CATEGORY_CALL)
                    .setVisibility(Notification.VISIBILITY_PUBLIC)
                    .setContentIntent(fullScreenPendingIntent)
                    .setPriority(Notification.PRIORITY_MAX)
                    .setFullScreenIntent(fullScreenPendingIntent, true);

            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();

            CharSequence name = "Break Time";
            String description = "Your new break activity is ready";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("Break Time", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            channel.setSound(sound, attributes);
            notificationManager.createNotificationChannel(channel);
            notificationManager.notify(getTimeSeconds(), builder.build());
        } else {

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context,
                    context.getString(R.string.notification_channel_id))
                    .setSmallIcon(R.drawable.notification_icon)
                    .setContentTitle(context.getString(R.string.notification_notification_name))
                    .setContentText(context.getString(R.string.notification_notification_desc))
                    .setOngoing(true)
                    .setAutoCancel(true)
                    .setCategory(Notification.CATEGORY_CALL)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setContentIntent(fullScreenPendingIntent)
                    .setPriority(Notification.PRIORITY_MAX)
                    .setSound(sound)
                    .setFullScreenIntent(fullScreenPendingIntent, true);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.notify(getTimeSeconds(), builder.build());

        }

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

}
