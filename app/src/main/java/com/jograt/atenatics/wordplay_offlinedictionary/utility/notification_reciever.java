package com.jograt.atenatics.wordplay_offlinedictionary.utility;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.jograt.atenatics.wordplay_offlinedictionary.R;
import com.jograt.atenatics.wordplay_offlinedictionary.RepeatingActivity;

/**
 * Created by John on 2017/10/04.
 */

public class notification_reciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        Intent repeating_intent = new Intent(context, RepeatingActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(context,100,repeating_intent,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.wordplayorig)
                .setContentTitle("Word Play")
                .setContentText("New Word of the day check it out!!!")
                .setAutoCancel(true);
        notificationManager.notify(100, builder.build());
    }
}
