package hb.me.homeworkout.gym.buttlegs.buttlegspro.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import hb.me.homeworkout.gym.buttlegs.buttlegspro.database.model.ExerciseRealm;

public class LevelData implements Serializable {
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
    private String type = null;
    int viewType = -1;

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setAdView(Object adView) {
        this.adView = adView;
    }

    public Object getAdView() {
        return this.adView;
    }

    public static LevelData copyLevelData(LevelData levelData) {
        LevelData newLevelData = new LevelData();
        newLevelData.setAdView(levelData.getAdView());
        newLevelData.setType(levelData.getType());
        newLevelData.setNameKey(levelData.getNameKey());
        newLevelData.setDescKey(levelData.getDescKey());
        newLevelData.setHasLock(levelData.hasLock);
        newLevelData.setImgSrc(levelData.getImgSrc());
        newLevelData.setLevel(levelData.getLevel());
        List<Exercise> exercises = new ArrayList();
        for (Exercise ex : levelData.getExercises()) {
            exercises.add(Exercise.copyExercise(ex));
        }
        return newLevelData;
    }

    public int getViewType() {
        return this.viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
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

    public List<ExerciseRealm> getExercisesForRealm() {
        List<ExerciseRealm> array = new ArrayList();
        for (Exercise ex : getExercises()) {
            ExerciseRealm obj = new ExerciseRealm();
            obj.setName(ex.getNameKey());
            obj.setDuration(ex.getDuration().intValue());
            obj.setLevel(getLevel().intValue());
            array.add(obj);
        }
        return array;
    }

    public int getDuration() {
        int duration = 0;
        for (Exercise ex : this.exercises) {
            int repetition = ex.getRepetition();
            if (repetition == 0) {
                repetition++;
            }
            duration += ex.getDuration().intValue() * repetition;
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

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LevelData levelData = (LevelData) o;
        if (this.hasLock != levelData.hasLock || this.viewType != levelData.viewType) {
            return false;
        }
        if (this.nameKey != null) {
            if (!this.nameKey.equals(levelData.nameKey)) {
                return false;
            }
        } else if (levelData.nameKey != null) {
            return false;
        }
        if (this.level != null) {
            if (!this.level.equals(levelData.level)) {
                return false;
            }
        } else if (levelData.level != null) {
            return false;
        }
        if (this.descKey != null) {
            if (!this.descKey.equals(levelData.descKey)) {
                return false;
            }
        } else if (levelData.descKey != null) {
            return false;
        }
        if (this.adView != null) {
            if (!this.adView.equals(levelData.adView)) {
                return false;
            }
        } else if (levelData.adView != null) {
            return false;
        }
        if (this.imgSrc != null) {
            if (!this.imgSrc.equals(levelData.imgSrc)) {
                return false;
            }
        } else if (levelData.imgSrc != null) {
            return false;
        }
        if (this.exercises != null) {
            z = this.exercises.equals(levelData.exercises);
        } else if (levelData.exercises != null) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        int result;
        int hashCode;
        int i = 0;
        if (this.nameKey != null) {
            result = this.nameKey.hashCode();
        } else {
            result = 0;
        }
        int i2 = result * 31;
        if (this.level != null) {
            hashCode = this.level.hashCode();
        } else {
            hashCode = 0;
        }
        i2 = (i2 + hashCode) * 31;
        if (this.descKey != null) {
            hashCode = this.descKey.hashCode();
        } else {
            hashCode = 0;
        }
        i2 = (i2 + hashCode) * 31;
        if (this.hasLock) {
            hashCode = 1;
        } else {
            hashCode = 0;
        }
        i2 = (i2 + hashCode) * 31;
        if (this.adView != null) {
            hashCode = this.adView.hashCode();
        } else {
            hashCode = 0;
        }
        i2 = (((i2 + hashCode) * 31) + this.viewType) * 31;
        if (this.imgSrc != null) {
            hashCode = this.imgSrc.hashCode();
        } else {
            hashCode = 0;
        }
        hashCode = (i2 + hashCode) * 31;
        if (this.exercises != null) {
            i = this.exercises.hashCode();
        }
        return hashCode + i;
    }
}
