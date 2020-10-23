package com.tlvanelearning.ielts;

import android.util.Log;

/**
 * Created by tlvan on 11/14/2017.
 */

public class LogUtil {
    public static final String TAG = "HellBoy";
    public static void d(String locationTag, String message){
        Log.d(TAG, "[INFO]" + locationTag + " : " + message);
    }

    public static void e(String locationTag, String message){
        Log.e(TAG, "[ERROR]" + locationTag + " : " + message);
    }

    public static void d(Class<?> clss, String message){
        Log.d(TAG, "[INFO]" + clss.getName() + " : " + message);
    }

    public static void e(Class<?> clss, String message){
        Log.e(TAG, "[ERROR]" + clss.getName() + " : " + message);
    }

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
