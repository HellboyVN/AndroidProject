package hb.me.instagramsave;

import android.app.Service;
import android.content.ClipboardManager;
import android.content.ClipboardManager.OnPrimaryClipChangedListener;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.regex.Pattern;

public class ClipService extends Service {
    ClipboardManager cm;

    class ClipboardListener implements OnPrimaryClipChangedListener {
        ClipboardListener() {
        }

        public void onPrimaryClipChanged() {
            Log.d("CLIP CHANGED: ", "onPrimaryClipChanged called..");
            if (ClipService.this.cm != null) {
                String s1 = ClipService.this.cm.getPrimaryClip().getItemAt(0).coerceToText(ClipService.this).toString();
                Log.d("url", s1);
                if (Pattern.compile("instagram.com").matcher(s1).find()) {
                    Log.d("Yes: ", "success");
                    ClipService.this.startActivity(ClipService.this.getPackageManager().getLaunchIntentForPackage(BuildConfig.APPLICATION_ID));
                    return;
                }
                Log.d("No: ", "unsuccessful");
            }
        }
    }

    public void onCreate() {
        Log.d("ONCREATE: ", "onCreate called..");
        super.onCreate();
        this.cm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        this.cm.addPrimaryClipChangedListener(new ClipboardListener());
    }

    @Nullable
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onDestroy() {
        super.onDestroy();
        this.cm.removePrimaryClipChangedListener(new ClipboardListener());
    }
}
