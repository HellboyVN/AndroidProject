package hellboy.bka.tlvan.os11lockscreen.util;

import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;

import hellboy.bka.tlvan.os11lockscreen.service.MyService;

public class MyApplication extends Application {
    public static Bitmap blur;

    public void onCreate() {
        super.onCreate();
        startService(new Intent(this, MyService.class));
    }
}
