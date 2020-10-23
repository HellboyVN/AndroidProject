package hb.me.homeworkout.gym.buttlegs.buttlegspro.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MData implements Serializable {
    @SerializedName("data")
    @Expose
    private List<LevelData> data = new ArrayList();
    @SerializedName("type")
    @Expose
    private String type;

    public LevelData getAllExercises() {
        LevelData customLevel = new LevelData();
        List<Exercise> allExercises = new ArrayList();
        for (LevelData lv : getData()) {
            List<Exercise> exercises = new ArrayList();
            exercises.addAll(lv.getExercises());
            for (Exercise exercise : exercises) {
                if (!checkIsExist(allExercises, exercise)) {
                    Exercise e = new Exercise(exercise);
                    e.setDuration(Integer.valueOf(0));
                    allExercises.add(e);
                }
            }
        }
        customLevel.setExercises(allExercises);
        customLevel.setNameKey("custom_workout");
        return customLevel;
    }

    public LevelData getDataByDay(int day, float scaleFactor) {
        for (LevelData levelData : getData()) {
            if (levelData.getLevel().intValue() == day) {
                for (Exercise exercise : levelData.getExercises()) {
                    exercise.setScaleFactor(scaleFactor);
                }
                return levelData;
            }
        }
        return null;
    }

    private boolean checkIsExist(List<Exercise> list, Exercise exercise) {
        for (Exercise ex : list) {
            if (ex.getNameKey().equals(exercise.getNameKey())) {
                return true;
            }
        }
        return false;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<LevelData> getData() {
        return this.data;
    }

    public void setData(List<LevelData> data) {
        this.data = data;
    }

    public void setDataType(String type) {
        for (LevelData levelData : this.data) {
            levelData.setType(type);
        }
    }
}
