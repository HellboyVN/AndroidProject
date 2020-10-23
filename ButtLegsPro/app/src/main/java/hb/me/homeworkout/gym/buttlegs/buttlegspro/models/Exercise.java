package hb.me.homeworkout.gym.buttlegs.buttlegspro.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Exercise implements Serializable {
    protected int adHeight;
    protected Object adView;
    protected int adWidth;
    @SerializedName("duration")
    @Expose
    protected Integer duration;
    @SerializedName("img_key")
    @Expose
    protected String imgKey;
    @SerializedName("name_key")
    @Expose
    protected String nameKey;
    @SerializedName("repetition")
    @Expose
    protected Integer repetition;
    private float scaleFactor = 1.0f;
    @SerializedName("tm_speed")
    @Expose
    protected Integer tmSpeed;
    private int viewType = -1;

    public Exercise(Exercise exercise) {
        this.nameKey = exercise.getNameKey();
        this.duration = exercise.getDuration();
        this.imgKey = exercise.getImgKey();
    }

    public Exercise() {

    }

    public static Exercise copyExercise(Exercise exercise) {
        Exercise newExercise = new Exercise();
        exercise.setDuration(exercise.getDuration());
        exercise.setNameKey(exercise.getNameKey());
        exercise.setTmSpeed(exercise.getTmSpeed());
        exercise.setViewType(exercise.getViewType());
        exercise.setAdHeight(exercise.getAdHeight());
        exercise.setAdView(exercise.getAdView());
        exercise.setAdWidth(exercise.getAdWidth());
        exercise.setImgKey(exercise.getImgKey());
        return newExercise;
    }

    public float getScaleFactor() {
        return this.scaleFactor;
    }

    public void setScaleFactor(float scaleFactor) {
        this.scaleFactor = scaleFactor;
    }

    public Object getAdView() {
        return this.adView;
    }

    public void setAdView(Object adView) {
        this.adView = adView;
    }

    public Integer getTmSpeed() {
        return this.tmSpeed;
    }

    public void setTmSpeed(Integer tmSpeed) {
        this.tmSpeed = tmSpeed;
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

    public int getRepetition() {
        return (this.repetition == null || this.repetition.intValue() <= 1) ? 0 : this.repetition.intValue();
    }

    public void setRepetition(int repetition) {
        this.repetition = Integer.valueOf(repetition);
    }

    public String getImgKey() {
        return this.imgKey;
    }

    public void setImgKey(String imgKey) {
        this.imgKey = imgKey;
    }

    public String getDurationFormatted() {
        int duration = getDuration().intValue() / 1000;
        int sec = duration % 60;
        return (duration / 60) + ":" + (sec < 10 ? "0" + sec : Integer.valueOf(sec));
    }

    public int getAdHeight() {
        return this.adHeight;
    }

    public void setAdHeight(int adHeight) {
        this.adHeight = adHeight;
    }

    public int getAdWidth() {
        return this.adWidth;
    }

    public void setAdWidth(int adWidth) {
        this.adWidth = adWidth;
    }

    public int getViewType() {
        return this.viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }
}
