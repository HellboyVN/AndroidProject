package com.workout.workout.managers;

import android.content.Context;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdRequest.Builder;
import com.workout.workout.modal.NativeAdsRowItem;
import com.workout.workout.modal.News;
import com.workout.workout.modal.Plan;
import com.workout.workout.modal.RowItem;
import com.workout.workout.modal.Training;
import com.workout.workout.modal.Utility;
import com.workout.workout.modal.Workout;
import java.util.ArrayList;
import java.util.List;

public class NativeAdsTaskManager {
    public static List<RowItem> prepareNativeAdsInWorkoutRowItemList(Context context, List<Workout> appInfoRowItemList) {
        List<RowItem> rowItemListWithNativeAds = null;
        int rowItemCount = 0;
        if (appInfoRowItemList != null && appInfoRowItemList.size() > 0) {
            rowItemListWithNativeAds = new ArrayList();
            if (NetworkManager.isNetworkAvailable(context) && !PersistenceManager.isAdsFreeVersion()) {
                rowItemListWithNativeAds.add(getNativeAdsRowInfoItem());
            }
            if (isNativeAdsAllowedToAdd() && NetworkManager.isNetworkAvailable(context)) {
                for (Workout appInfoRowItem : appInfoRowItemList) {
                    rowItemListWithNativeAds.add(appInfoRowItem);
                    rowItemCount++;
                    if (!PersistenceManager.isAdsFreeVersion() && rowItemCount == PersistenceManager.getWorkout_list_native_ads_repeat_interval()) {
                        rowItemListWithNativeAds.add(getNativeAdsRowInfoItem());
                        rowItemCount = 0;
                    }
                }
            } else {
                for (Workout appInfoRowItem2 : appInfoRowItemList) {
                    rowItemListWithNativeAds.add(appInfoRowItem2);
                }
            }
        }
        return rowItemListWithNativeAds;
    }

    public static AdRequest getNativeAdsRequest() {
        return new Builder().addTestDevice("1EB585F2A4030961337C34C7E41A3DF7").addTestDevice("4E48DAD63C1197852F54C4A9ED8ED793").addTestDevice("03A7AB2DA6606DAF0CBDC5146CA9648D").addTestDevice("EA8BCBCF166EC0AEC98310F727B79898").build();
    }

    private static NativeAdsRowItem getNativeAdsRowInfoItem() {
        return new NativeAdsRowItem();
    }

    public static boolean isNativeAdsAllowedToAdd() {
        return true;
    }

    public static List<RowItem> prepareNativeAdsInTrainingRowItemList(Context context, List<Training> appInfoRowItemList) {
        List<RowItem> rowItemListWithNativeAds = null;
        int rowItemCount = 0;
        if (appInfoRowItemList != null && appInfoRowItemList.size() > 0) {
            rowItemListWithNativeAds = new ArrayList();
            if (isNativeAdsAllowedToAdd() && NetworkManager.isNetworkAvailable(context)) {
                for (Training appInfoRowItem : appInfoRowItemList) {
                    rowItemListWithNativeAds.add(appInfoRowItem);
                    rowItemCount++;
                    if (!PersistenceManager.isAdsFreeVersion() && rowItemCount == PersistenceManager.getExercise_list_native_ads_repeat_interval()) {
                        rowItemListWithNativeAds.add(getNativeAdsRowInfoItem());
                        rowItemCount = 0;
                    }
                }
            } else {
                for (Training appInfoRowItem2 : appInfoRowItemList) {
                    rowItemListWithNativeAds.add(appInfoRowItem2);
                }
            }
        }
        return rowItemListWithNativeAds;
    }

    public static List<RowItem> prepareNativeAdsInPlanWorkoutRowItemList(Context context, List<Plan> appInfoRowItemList) {
        List<RowItem> rowItemListWithNativeAds = null;
        int rowItemCount = 0;
        if (appInfoRowItemList != null && appInfoRowItemList.size() > 0) {
            rowItemListWithNativeAds = new ArrayList();
            if (isNativeAdsAllowedToAdd() && NetworkManager.isNetworkAvailable(context)) {
                for (Plan appInfoRowItem : appInfoRowItemList) {
                    rowItemListWithNativeAds.add(appInfoRowItem);
                    rowItemCount++;
                    if (!PersistenceManager.isAdsFreeVersion() && rowItemCount == PersistenceManager.getPlan_workout_list_native_ads_repeat_interval()) {
                        rowItemListWithNativeAds.add(getNativeAdsRowInfoItem());
                        rowItemCount = 0;
                    }
                }
            } else {
                for (Plan appInfoRowItem2 : appInfoRowItemList) {
                    rowItemListWithNativeAds.add(appInfoRowItem2);
                }
            }
        }
        return rowItemListWithNativeAds;
    }

    public static List<RowItem> prepareNativeAdsInNewsNotificationRowItemList(Context context, List<News> appInfoRowItemList) {
        List<RowItem> rowItemListWithNativeAds = null;
        int rowItemCount = 0;
        if (appInfoRowItemList != null && appInfoRowItemList.size() > 0) {
            rowItemListWithNativeAds = new ArrayList();
            if (isNativeAdsAllowedToAdd() && NetworkManager.isNetworkAvailable(context)) {
                for (News appInfoRowItem : appInfoRowItemList) {
                    rowItemListWithNativeAds.add(appInfoRowItem);
                    rowItemCount++;
                    if (!PersistenceManager.isAdsFreeVersion() && rowItemCount == PersistenceManager.getPlan_workout_list_native_ads_repeat_interval()) {
                        rowItemListWithNativeAds.add(getNativeAdsRowInfoItem());
                        rowItemCount = 0;
                    }
                }
            } else {
                for (News appInfoRowItem2 : appInfoRowItemList) {
                    rowItemListWithNativeAds.add(appInfoRowItem2);
                }
            }
        }
        return rowItemListWithNativeAds;
    }

    public static List<RowItem> prepareNativeAdsInUtilityRowItemList(Context context, List<Utility> appInfoRowItemList) {
        List<RowItem> rowItemListWithNativeAds = null;
        int rowItemCount = 0;
        if (appInfoRowItemList != null && appInfoRowItemList.size() > 0) {
            rowItemListWithNativeAds = new ArrayList();
            if (isNativeAdsAllowedToAdd() && NetworkManager.isNetworkAvailable(context)) {
                for (Utility utilityRowItem : appInfoRowItemList) {
                    rowItemListWithNativeAds.add(utilityRowItem);
                    rowItemCount++;
                    if (!PersistenceManager.isAdsFreeVersion() && rowItemCount == 3) {
                        rowItemListWithNativeAds.add(getNativeAdsRowInfoItem());
                        rowItemCount = 0;
                    }
                }
            } else {
                for (Utility utilityRowItem2 : appInfoRowItemList) {
                    rowItemListWithNativeAds.add(utilityRowItem2);
                }
            }
        }
        return rowItemListWithNativeAds;
    }
}
