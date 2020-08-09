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
import java.util.Date;
import java.util.Random;

public class NotificationHelper {

    Context context;
    String name, description, channelId;
    int soundId;

    public NotificationHelper(Context context, String name, String description, String channelId, int soundId) {
        this.context = context;
        this.name = name;
        this.description = description;
        this.channelId = channelId;
        this.soundId = soundId;
    }

    public void showNotification() {

        Intent intent = new Intent(this.context, BreakSplash.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this.context, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this.context,
                this.channelId)
                .setSmallIcon(R.drawable.circle_toggle_button)
                .setContentTitle(this.name)
                .setContentText(this.description)
                .setPriority(NotificationCompat.FLAG_NO_CLEAR)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        createNotificationChannel();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this.context);
        notificationManager.notify(getTimeSeconds(), builder.build());
        new MusicPlayer().play(this.context, this.soundId);

    }

    public void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = this.name;
            String description = this.description;
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("7979", name, importance);
            channel.setDescription(description);
            channel.enableLights(true);
            channel.enableVibration(true);
            NotificationManager notificationManager = this.context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    private static int getTimeSeconds() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM HH:mm:ss");
        Date date = new Date();
        String dateTimeString = formatter.format(date);
        String dateString = dateTimeString.split(" ")[0];
        dateString = TrimZeroInData(dateString);
        String timeString = dateTimeString.split(" ")[1];
        return new Random().nextInt() * Integer.parseInt(getIntegerString(dateString, "/").concat(getIntegerString(timeString, ":")));
    }

    private static String getIntegerString(String sequence, String delimiter) {
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
