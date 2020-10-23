package hb.homeworkout.homeworkouts.noequipment.fitnesspro.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdRequest.Builder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import hb.homeworkout.homeworkouts.noequipment.fitnesspro.constant.AppConstants;

public class AppUtil {
    public static ArrayList<Integer> arrayListCount = null;

    public static float convertMilliterToOunce(float ml) {
        return (float) (Math.round(10.0f * (ml * 0.0338f)) / 10);
    }

    public static float convertOunceToMillimiter(float ounce) {
        return (float) (Math.round(10.0f * (ounce * 29.54f)) / 10);
    }

    public static long getSelectedStartDateInMillisecond(int date) {
        Calendar currentDateCalender = Calendar.getInstance();
        currentDateCalender.add(5, -date);
        Calendar setDateCalender = Calendar.getInstance();
        setDateCalender.set(2, currentDateCalender.get(2));
        setDateCalender.set(5, currentDateCalender.get(5));
        setDateCalender.set(1, currentDateCalender.get(1));
        setDateCalender.set(10, 0);
        setDateCalender.set(12, 0);
        setDateCalender.set(13, 0);
        setDateCalender.set(14, 0);
        setDateCalender.set(9, 0);
        return setDateCalender.getTimeInMillis();
    }

    public static long getYesterdayDateInMillisecond(int date) {
        Calendar currentDateCalender = Calendar.getInstance();
        currentDateCalender.add(5, -date);
        Calendar setDateCalender = Calendar.getInstance();
        setDateCalender.set(2, currentDateCalender.get(2));
        setDateCalender.set(5, currentDateCalender.get(5));
        setDateCalender.set(1, currentDateCalender.get(1));
        setDateCalender.set(10, currentDateCalender.get(10));
        setDateCalender.set(12, currentDateCalender.get(12));
        setDateCalender.set(13, currentDateCalender.get(13));
        setDateCalender.set(14, currentDateCalender.get(14));
        setDateCalender.set(9, currentDateCalender.get(9));
        return setDateCalender.getTimeInMillis();
    }

    public static long getPastDayEndOfDay1159PMInMillisecond(int date) {
        Calendar currentDateCalender = Calendar.getInstance();
        currentDateCalender.add(5, -date);
        return getEndOfDay1159PMInMillisecond(currentDateCalender);
    }

    public static long getCurrentDateInMillisecond() {
        return Calendar.getInstance().getTimeInMillis();
    }

    public static long getTodayStartOfDay1200AMInMillisecond() {
        Calendar currentDateCalender = Calendar.getInstance();
        Calendar setDateCalender = Calendar.getInstance();
        setDateCalender.set(2, currentDateCalender.get(2));
        setDateCalender.set(5, currentDateCalender.get(5));
        setDateCalender.set(1, currentDateCalender.get(1));
        setDateCalender.set(10, 0);
        setDateCalender.set(12, 0);
        setDateCalender.set(13, 0);
        setDateCalender.set(14, 0);
        setDateCalender.set(9, 0);
        return setDateCalender.getTimeInMillis();
    }

    public static long getTodayEndOfDay1159PMInMillisecond() {
        return getEndOfDay1159PMInMillisecond(Calendar.getInstance());
    }

    public static long getEndOfDay1159PMInMillisecond(Calendar calendar) {
        Calendar setDateCalender = Calendar.getInstance();
        setDateCalender.set(2, calendar.get(2));
        setDateCalender.set(5, calendar.get(5));
        setDateCalender.set(1, calendar.get(1));
        setDateCalender.set(10, 11);
        setDateCalender.set(12, 59);
        setDateCalender.set(13, 59);
        setDateCalender.set(14, 999);
        setDateCalender.set(9, 1);
        return setDateCalender.getTimeInMillis();
    }

    public static long getNextDateInMillisecond() {
        Calendar currentDateCalender = Calendar.getInstance();
        currentDateCalender.add(5, 1);
        Calendar setDateCalender = Calendar.getInstance();
        setDateCalender.set(2, currentDateCalender.get(2));
        setDateCalender.set(5, currentDateCalender.get(5));
        setDateCalender.set(1, currentDateCalender.get(1));
        setDateCalender.set(10, 0);
        setDateCalender.set(12, 0);
        setDateCalender.set(13, 0);
        setDateCalender.set(14, 0);
        setDateCalender.set(9, 0);
        return setDateCalender.getTimeInMillis();
    }

    public static long getNextEndDateInMillisecond() {
        Calendar currentDateCalender = Calendar.getInstance();
        currentDateCalender.add(5, 1);
        Calendar setDateCalender = Calendar.getInstance();
        setDateCalender.set(2, currentDateCalender.get(2));
        setDateCalender.set(5, currentDateCalender.get(5));
        setDateCalender.set(1, currentDateCalender.get(1));
        setDateCalender.set(10, 11);
        setDateCalender.set(12, 59);
        setDateCalender.set(13, 59);
        setDateCalender.set(14, 999);
        setDateCalender.set(9, 1);
        return setDateCalender.getTimeInMillis();
    }

    public static boolean containsData(String data) {
        if (data == null || data.trim().equals("")) {
            return false;
        }
        return true;
    }

    public static boolean isEmpty(String data) {
        return !containsData(data);
    }

    public static boolean isCollectionEmpty(Collection<? extends Object> collection) {
        if (collection == null || collection.isEmpty()) {
            return true;
        }
        return false;
    }

    public static void hideKeyboard(Context context) {
        if (context != null) {
            try {
                ((InputMethodManager) context.getSystemService("input_method")).hideSoftInputFromWindow(((Activity) context).getCurrentFocus().getWindowToken(), 2);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    public static boolean isEmailValid(String email) {
        String emailRegx = AppConstants.VALID_EMAIL_REGEX;
        boolean isFieldValid = true;
        if (email == null) {
            return false;
        }
        if (!email.matches(emailRegx)) {
            isFieldValid = false;
        }
        return isFieldValid;
    }

    public static AdRequest getAdRequest() {
        Log.e("----adsFull----", "Loaded");
        return new Builder().addTestDevice("3F90C25D51474DDEA1C2B2319E1ADE19").build();
    }

    public static float getDimenstionInDp(Activity activityContext, int dimenResourceId) {
        if (activityContext == null) {
            return 0.0f;
        }
        Resources resources = activityContext.getResources();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activityContext.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return resources.getDimension(dimenResourceId) / displayMetrics.density;
    }

    public static int dpToPx(int dp) {
        return (int) (((float) dp) * Resources.getSystem().getDisplayMetrics().density);
    }
}
