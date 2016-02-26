package com.basket.basketmanager.notifications;

import android.content.Intent;
import com.google.android.gms.iid.InstanceIDListenerService;

public class BasketManagerInstanceIDListenerService extends InstanceIDListenerService {

    @Override
    public void onTokenRefresh() {
        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);
    }
}
