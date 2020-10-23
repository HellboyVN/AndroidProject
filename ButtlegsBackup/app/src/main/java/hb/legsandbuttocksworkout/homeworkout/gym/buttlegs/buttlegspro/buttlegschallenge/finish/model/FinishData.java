package hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.finish.model;

import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.finish.cards.RateQuestionCard.RateQuestionStates;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

public class FinishData {
    private Integer calories;
    private String durationFormatted;
    @SerializedName("finish_data")
    @Expose
    private List<Finish> finishData = new ArrayList();
    private int level;
    private RateQuestionStates rateQuestionState = RateQuestionStates.STATE_LIKE;

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

    public String getDurationFormatted() {
        return this.durationFormatted;
    }

    public void setDurationFormatted(String durationFormatted) {
        this.durationFormatted = durationFormatted;
    }

    public Integer getCalories() {
        return this.calories;
    }

    public void setCalories(Integer calories) {
        this.calories = calories;
    }

    public List<Finish> getFinishData() {
        return this.finishData;
    }

    public void setFinishData(List<Finish> finishData) {
        this.finishData = finishData;
    }
}
