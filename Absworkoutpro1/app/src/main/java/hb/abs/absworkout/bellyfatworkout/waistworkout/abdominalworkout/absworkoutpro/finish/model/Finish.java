package hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.finish.model;

import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.finish.cards.RateQuestionCard.RateQuestionStates;
import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.home.tabs.tabfour.type.ExerciseType;
import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.reminder.data.TimeData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Finish {
    private String duration;
    private int level;
    private ExerciseType mExType;
    @SerializedName("name")
    @Expose
    private String name;
    private RateQuestionStates rateQuestionState = RateQuestionStates.STATE_LIKE;
    public TimeData reminderCardData;
    @SerializedName("view_type")
    @Expose
    private Integer viewType;

    public RateQuestionStates getRateQuestionState() {
        return this.rateQuestionState;
    }

    public void setRateQuestionState(RateQuestionStates rateQuestionState) {
        this.rateQuestionState = rateQuestionState;
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

    public TimeData getReminderCardData() {
        return this.reminderCardData;
    }

    public void setReminderCardData(TimeData reminderCardData) {
        this.reminderCardData = reminderCardData;
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

    public ExerciseType getExType() {
        return this.mExType;
    }

    public void setExType(ExerciseType mExType) {
        this.mExType = mExType;
    }
}
