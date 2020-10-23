package hb.me.homeworkout.gym.buttlegs.buttlegspro.home.models;

public class ChallengeItem  {
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
