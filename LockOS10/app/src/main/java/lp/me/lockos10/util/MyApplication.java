package lp.me.lockos10.util;

import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;
import lp.me.lockos10.service.MyService;

public class MyApplication extends Application {
    public static Bitmap blur;

    public void onCreate() {
        super.onCreate();
        startService(new Intent(this, MyService.class));
    }
}
