package hb.me.instagramsave;

import android.app.Application;
import android.content.Intent;

public class App extends Application {
    public void onCreate() {
        super.onCreate();
        startService(new Intent(this, ClipService.class));
    }
}
