package lp.me.lockos10.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import lp.me.lockos10.broadcast.ScreenOn;

public class MyService extends Service {
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        IntentFilter intentFilter = new IntentFilter("android.intent.action.SCREEN_ON");
        intentFilter.addAction("android.intent.action.SCREEN_OFF");
        intentFilter.setPriority(4);
        registerReceiver(ScreenOn.newInstance(), intentFilter);
    }

    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(ScreenOn.newInstance());
    }
}
