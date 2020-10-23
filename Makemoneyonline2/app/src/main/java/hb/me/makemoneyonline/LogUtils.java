package hb.me.makemoneyonline;

import android.util.Log;

/**
 * Created by nxtruong on 5/18/2017.
 */

public class LogUtils {
    public static final String TAG = "LAMPARD";

    public static void d(String message){
        StackTraceElement[] ste = (new Throwable()).getStackTrace();
        String text = "[" + ste[1].getFileName() + ":" + ste[1].getLineNumber() + ":" + ste[1].getMethodName() + "()]";
        Log.d(TAG, text + message);
    }

    public static void e(String message){
        StackTraceElement[] ste = (new Throwable()).getStackTrace();
        String text = "[" + ste[1].getFileName() + ":" + ste[1].getLineNumber() + ":" + ste[1].getMethodName() + "()]";
        Log.e(TAG, text + message);
    }
}
