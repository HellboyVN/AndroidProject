package hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.home.tabs.tabfour.model;

import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.ads.INativeAd;

public class ChallengeItem implements INativeAd {
    Object adView;
    private int viewType;

    public int getViewType() {
        return this.viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public void setAdView(Object adView) {
        this.adView = adView;
    }

    public Object getAdView() {
        return this.adView;
    }
}
