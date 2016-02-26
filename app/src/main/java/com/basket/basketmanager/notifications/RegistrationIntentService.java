package com.basket.basketmanager.notifications;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;

import com.basket.basketmanager.BasketManagerPreferences;
import com.basket.basketmanager.R;
import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.microsoft.windowsazure.messaging.NotificationHub;

import java.io.IOException;

public class RegistrationIntentService extends IntentService {
    private static final String TAG = "RegIntentService";
    private static final String[] TOPICS = {"global"};

    private GoogleCloudMessaging gcm;
    private NotificationHub hub;
    private static Context context;

    public RegistrationIntentService() {
        super(TAG);
    }

    public static void setContext(Context context) {
        RegistrationIntentService.context = context;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        try {
            InstanceID instanceID = InstanceID.getInstance(this);
            String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            //Log.i(TAG, "GCM Registration Token: " + token);
            subscribeTopics(token);
            registerWithNotificationHub();
            sharedPreferences.edit().putBoolean(BasketManagerPreferences.SENT_TOKEN_TO_SERVER, true).apply();

        } catch (Exception e) {
            //Log.d(TAG, "Failed to complete token refresh", e);
            sharedPreferences.edit().putBoolean(BasketManagerPreferences.SENT_TOKEN_TO_SERVER, false).apply();
        }
        Intent registrationComplete = new Intent(BasketManagerPreferences.REGISTRATION_COMPLETE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    @SuppressWarnings( "deprecation" )
    private void registerWithNotificationHub() throws Exception {
        gcm = GoogleCloudMessaging.getInstance(context);
        hub = new NotificationHub(getString(R.string.hub_name), getString(R.string.hub_listen_connection_string), context);
        hub.register(gcm.register(getString(R.string.gcm_defaultSenderId)));
    }

    private void subscribeTopics(String token) throws IOException {
        GcmPubSub pubSub = GcmPubSub.getInstance(this);
        for (String topic : TOPICS) {
            pubSub.subscribe(token, "/topics/" + topic, null);
        }
    }
}
