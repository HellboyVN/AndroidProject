package hb.me.homeworkout.gym.buttlegs.buttlegspro.home.type;

import hb.me.homeworkout.gym.buttlegs.buttlegspro.R;

public enum ChallengeDaysType {
    NONE(0),
    BEGINNER(1, R.string.beginner, 3, 1.0f),
    MEDIUM(2, R.string.medium, 5, 1.5f),
    PROFI(3, R.string.profi, 6, 2.0f),
    CHALLENGE(4);
    
    private int restInterval;
    private float scaleFactor;
    private int titleResId;
    private int type;

    private ChallengeDaysType(int type) {
        this.type = type;
    }

    private ChallengeDaysType(int type, int titleResId) {
        this.type = type;
        this.titleResId = titleResId;
    }

    private ChallengeDaysType(int type, int titleResId, int restInterval, float scaleFactor) {
        this.type = type;
        this.titleResId = titleResId;
        this.restInterval = restInterval;
        this.scaleFactor = scaleFactor;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public static ChallengeDaysType getByType(int type) {
        switch (type) {
            case 0:
                return NONE;
            case 1:
                return BEGINNER;
            case 2:
                return MEDIUM;
            case 3:
                return PROFI;
            default:
                return CHALLENGE;
        }
    }

    public int getTitleResId() {
        return this.titleResId;
    }

    public int getRestInterval() {
        return this.restInterval;
    }

    public float getScaleFactor() {
        return this.scaleFactor;
    }
}
