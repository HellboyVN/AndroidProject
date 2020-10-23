package hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.models;

import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.ads.INativeAd;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.database.model.ExerciseRealm;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.home.tabs.tabone.cards.WeightCard.WeightCardStates;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.utils.SharedPrefsService;
import android.content.Context;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LevelData implements Serializable, INativeAd {
    Object adView;
    @SerializedName("desc_key")
    @Expose
    private String descKey;
    @SerializedName("exercises")
    @Expose
    private List<Exercise> exercises = new ArrayList();
    @SerializedName("has_lock")
    @Expose
    private boolean hasLock;
    @SerializedName("img_src")
    @Expose
    private String imgSrc;
    @SerializedName("level")
    @Expose
    private Integer level;
    @SerializedName("name_key")
    @Expose
    private String nameKey;
    int viewType = -1;
    private WeightCardStates weightCardStates = WeightCardStates.STATE_ENTER;

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

    public String getNameKey() {
        return this.nameKey;
    }

    public void setNameKey(String nameKey) {
        this.nameKey = nameKey;
    }

    public Integer getLevel() {
        return this.level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getDescKey() {
        return this.descKey;
    }

    public void setDescKey(String descKey) {
        this.descKey = descKey;
    }

    public String getImgSrc() {
        return this.imgSrc;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    public List<Exercise> getExercises() {
        return this.exercises;
    }

    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises;
    }

    public boolean isHasLock() {
        return this.hasLock;
    }

    public void setHasLock(boolean hasLock) {
        this.hasLock = hasLock;
    }

    public WeightCardStates getWeightCardStates() {
        return this.weightCardStates;
    }

    public void setWeightCardStates(WeightCardStates weightCardStates) {
        this.weightCardStates = weightCardStates;
    }

    public int getDuration() {
        int duration = 0;
        for (Exercise ex : this.exercises) {
            duration += ex.getDuration().intValue();
        }
        return duration;
    }

    public String getDurationFormatted() {
        int duration = getDuration() / 1000;
        int sec = duration % 60;
        return (duration / 60) + ":" + (sec < 10 ? "0" + sec : Integer.valueOf(sec));
    }

    public String getIntensivityLevelKey() {
        if ((getDuration() / 1000) / 60 < 5) {
            return "mid_intensivity";
        }
        return "high_intensivity";
    }

    public List<ExerciseRealm> getExercisesForRealm(Context context) {
        List<ExerciseRealm> array = new ArrayList();
        int gender = SharedPrefsService.getInstance().getGender(context);
        for (Exercise ex : getExercises()) {
            ExerciseRealm obj = new ExerciseRealm();
            obj.setName(ex.getNameKey());
            obj.setDuration(ex.getDuration().intValue());
            obj.setGender(gender);
            obj.setLevel(getLevel().intValue());
            array.add(obj);
        }
        return array;
    }

    public Integer getCalories() {
        Integer calories = Integer.valueOf(0);
        for (Exercise exercise : getExercises()) {
        }
        return calories;
    }
}
