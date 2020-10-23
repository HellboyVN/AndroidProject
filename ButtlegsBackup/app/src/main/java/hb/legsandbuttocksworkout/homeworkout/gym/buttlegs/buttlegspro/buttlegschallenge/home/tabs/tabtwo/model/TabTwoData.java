package hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.home.tabs.tabtwo.model;

import java.util.List;

public class TabTwoData {
    boolean isPurchased;
    String name;
    int viewType = -1;
    int weekOffset = 0;

    public int getViewType() {
        return this.viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPurchased() {
        return this.isPurchased;
    }

    public void setPurchased(boolean purchased) {
        this.isPurchased = purchased;
    }

    public int getWeekOffset() {
        return this.weekOffset;
    }

    public void setWeekOffset(int weekOffset) {
        this.weekOffset = weekOffset;
    }

    public static TabTwoData getItemByType(List<TabTwoData> dataList, int viewType) {
        if (dataList == null) {
            return null;
        }
        for (TabTwoData data : dataList) {
            if (data.getViewType() == viewType) {
                return data;
            }
        }
        return null;
    }
}
