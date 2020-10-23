package hb.me.homeworkout.gym.buttlegs.buttlegspro.finish.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.finish.cards.RateQuestionCard.RateQuestionStates;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.home.type.ExerciseType;

public class Finish  {
    private Object adView;
    private String duration;
    private int level;
    private ExerciseType mExType;
    @SerializedName("name")
    @Expose
    private String name;
    private RateQuestionStates rateQuestionState = RateQuestionStates.STATE_LIKE;
    @SerializedName("view_type")
    @Expose
    private Integer viewType;

    public RateQuestionStates getRateQuestionState() {
        return this.rateQuestionState;
    }

    public void setRateQuestionState(RateQuestionStates rateQuestionState) {
        this.rateQuestionState = rateQuestionState;
    }

    public int getLevel() {
        return this.level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getDuration() {
        return this.duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setAdView(Object adView) {
        this.adView = adView;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getViewType() {
        return this.viewType;
    }

    public void setViewType(Integer viewType) {
        this.viewType = viewType;
    }

    public Object getAdView() {
        return this.adView;
    }

    public ExerciseType getExType() {
        return this.mExType;
    }

    public void setExType(ExerciseType mExType) {
        this.mExType = mExType;
    }
}
