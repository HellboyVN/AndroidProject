package hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.home.tabs.tabfour.type;

public enum ExerciseType {
    TRAIN_AT_HOME(1),
    TREADMILL(2),
    CHALLENGE(3);
    
    private int type;

    private ExerciseType(int type) {
        this.type = type;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public static ExerciseType getByType(int type) {
        switch (type) {
            case 1:
                return TRAIN_AT_HOME;
            case 2:
                return TREADMILL;
            case 3:
                return CHALLENGE;
            default:
                return TRAIN_AT_HOME;
        }
    }
}
