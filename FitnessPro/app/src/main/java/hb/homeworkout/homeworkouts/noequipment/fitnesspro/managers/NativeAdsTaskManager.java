package hb.homeworkout.homeworkouts.noequipment.fitnesspro.managers;

import android.content.Context;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdRequest.Builder;

import java.util.ArrayList;
import java.util.List;

import hb.homeworkout.homeworkouts.noequipment.fitnesspro.modal.News;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.modal.Plan;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.modal.RowItem;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.modal.Training;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.modal.Utility;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.modal.Workout;

public class NativeAdsTaskManager {
    public static List<RowItem> prepareNativeAdsInWorkoutRowItemList(Context context, List<Workout> appInfoRowItemList) {
        List<RowItem> rowItemListWithNativeAds = null;
        int rowItemCount = 0;
        if (appInfoRowItemList != null && appInfoRowItemList.size() > 0) {
            rowItemListWithNativeAds = new ArrayList();


                for (Workout appInfoRowItem2 : appInfoRowItemList) {
                    rowItemListWithNativeAds.add(appInfoRowItem2);
                }

        }
        return rowItemListWithNativeAds;
    }

    public static AdRequest getNativeAdsRequest() {
        return new Builder().addTestDevice("1EB585F2A4030961337C34C7E41A3DF7").addTestDevice("4E48DAD63C1197852F54C4A9ED8ED793").addTestDevice("03A7AB2DA6606DAF0CBDC5146CA9648D").addTestDevice("EA8BCBCF166EC0AEC98310F727B79898").build();
    }




    public static List<RowItem> prepareNativeAdsInTrainingRowItemList(Context context, List<Training> appInfoRowItemList) {
        List<RowItem> rowItemListWithNativeAds = null;
        int rowItemCount = 0;
        if (appInfoRowItemList != null && appInfoRowItemList.size() > 0) {
            rowItemListWithNativeAds = new ArrayList();

                for (Training appInfoRowItem2 : appInfoRowItemList) {
                    rowItemListWithNativeAds.add(appInfoRowItem2);
                }

        }
        return rowItemListWithNativeAds;
    }

    public static List<RowItem> prepareNativeAdsInPlanWorkoutRowItemList(Context context, List<Plan> appInfoRowItemList) {
        List<RowItem> rowItemListWithNativeAds = null;
        int rowItemCount = 0;
        if (appInfoRowItemList != null && appInfoRowItemList.size() > 0) {
            rowItemListWithNativeAds = new ArrayList();

                for (Plan appInfoRowItem2 : appInfoRowItemList) {
                    rowItemListWithNativeAds.add(appInfoRowItem2);
                }

        }
        return rowItemListWithNativeAds;
    }

    public static List<RowItem> prepareNativeAdsInNewsNotificationRowItemList(Context context, List<News> appInfoRowItemList) {
        List<RowItem> rowItemListWithNativeAds = null;
        int rowItemCount = 0;
        if (appInfoRowItemList != null && appInfoRowItemList.size() > 0) {
            rowItemListWithNativeAds = new ArrayList();

                for (News appInfoRowItem2 : appInfoRowItemList) {
                    rowItemListWithNativeAds.add(appInfoRowItem2);
                }

        }
        return rowItemListWithNativeAds;
    }

    public static List<RowItem> prepareNativeAdsInUtilityRowItemList(Context context, List<Utility> appInfoRowItemList) {
        List<RowItem> rowItemListWithNativeAds = null;
        int rowItemCount = 0;
        if (appInfoRowItemList != null && appInfoRowItemList.size() > 0) {
            rowItemListWithNativeAds = new ArrayList();

                for (Utility utilityRowItem2 : appInfoRowItemList) {
                    rowItemListWithNativeAds.add(utilityRowItem2);
                }

        }
        return rowItemListWithNativeAds;
    }
}
