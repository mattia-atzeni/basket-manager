package com.basket.basketmanager.notifications;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;

import com.basket.basketmanager.BaseActivity;
import com.basket.basketmanager.BasketManager;
import com.basket.basketmanager.BasketManagerPreferences;
import com.basket.basketmanager.R;
import com.basket.basketmanager.database.MatchHandler;
import com.google.android.gms.gcm.GcmListenerService;

public class BasketManagerGcmListenerService extends GcmListenerService {

    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");
        sendNotification(message);
        new MatchHandler(getApplicationContext()).newRandomProposedMatch();
        incrementNotificationsCount();
    }

    private void sendNotification(String message) {
        Intent intent = new Intent(this, BaseActivity.class);
        intent.putExtra(BaseActivity.EXTRA_NOTIFICATION, true);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);

        if (!BasketManager.isActive()) {
            notificationBuilder.setSmallIcon(R.drawable.ic_action_basketball)
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent);
        }

        notificationBuilder.setSound(defaultSoundUri);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }

    private void incrementNotificationsCount() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        int count = sharedPreferences.getInt(BasketManagerPreferences.NOTIFICATIONS_COUNT, 0);
        sharedPreferences.edit().putInt(BasketManagerPreferences.NOTIFICATIONS_COUNT, count + 1).apply();
    }
}
