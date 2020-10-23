package hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.ads.INativeAd;

public class Exercise implements Serializable, Cloneable, INativeAd {
    private Object adView;
    @SerializedName("calories")
    @Expose
    private Integer calories;
    @SerializedName("duration")
    @Expose
    private Integer duration;
    @SerializedName("img_key")
    @Expose
    private String imgKey;
    @SerializedName("name_key")
    @Expose
    private String nameKey;
    private float scaleFactor = 1.0f;
    private int viewType = -1;

    public Object getAdView() {
        return this.adView;
    }

    public void setAdView(Object adView) {
        this.adView = adView;
    }

    public Exercise(){

    }
    public Exercise(Exercise exercise) {
        this.nameKey = exercise.getNameKey();
        this.duration = exercise.getDuration();
        this.imgKey = exercise.getImgKey();
    }

    public String getNameKey() {
        return this.nameKey;
    }

    public void setNameKey(String nameKey) {
        this.nameKey = nameKey;
    }

    public Integer getDuration() {
        return Integer.valueOf((int) (((float) this.duration.intValue()) * this.scaleFactor));
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getImgKey() {
        return this.imgKey;
    }

    public void setImgKey(String imgKey) {
        this.imgKey = imgKey;
    }

    public Integer getCalories() {
        return this.calories;
    }

    public void setCalories(Integer calories) {
        this.calories = calories;
    }

    public String getDurationFormatted() {
        int duration = getDuration().intValue() / 1000;
        int min = duration / 60;
        int sec = duration % 60;
        return (min < 10 ? "0" : "") + min + ":" + (sec < 10 ? "0" : "") + sec;
    }

    public String getDurationForStatFormatted() {
        int duration = getDuration().intValue() / 1000;
        int min = duration / 60;
        int sec = duration % 60;
        int hour = min / 60;
        min -= hour * 60;
        return (hour < 10 ? "0" : "") + hour + ":" + (min < 10 ? "0" : "") + min + ":" + (sec < 10 ? "0" : "") + sec;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public int getViewType() {
        return this.viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public void setScaleFactor(float scaleFactor) {
        this.scaleFactor = scaleFactor;
    }
}
