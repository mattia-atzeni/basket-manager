package com.basket.basketmanager;


import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

public class BasketManager extends Application {

    private static boolean active = false;

    @Override
    public void onCreate (){
        super.onCreate();
        registerActivityLifecycleCallbacks(new BasketManagerActivityLifecycleCallbacks());
    }

    public static boolean isActive() {
        return active;
    }

    private static final class BasketManagerActivityLifecycleCallbacks implements ActivityLifecycleCallbacks {

        public void onActivityCreated(Activity activity, Bundle bundle) {

        }

        public void onActivityDestroyed(Activity activity) {

        }

        public void onActivityPaused(Activity activity) {
            active = false;
        }

        public void onActivityResumed(Activity activity) {
            active = true;
        }

        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        public void onActivityStarted(Activity activity) {

        }

        public void onActivityStopped(Activity activity) {

        }
    }
}
