package hb.me.homeworkout.gym.buttlegs.buttlegspro.database.model;

public class Workout {
    private int duration;
    private long id;
    private int level;
    private long time;

    public static class WeeklyWorkoutForChart {
        int currentDay;
        int maxValue;
        float[] result;

        public float[] getResult() {
            return this.result;
        }

        public void setResult(float[] result) {
            this.result = result;
        }

        public int getCurrentDay() {
            return this.currentDay;
        }

        public void setCurrentDay(int currentDay) {
            this.currentDay = currentDay;
        }

        public int getMaxValue() {
            return this.maxValue;
        }

        public void setMaxValue(int maxValue) {
            this.maxValue = maxValue;
        }
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTime() {
        return this.time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getLevel() {
        return this.level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getDuration() {
        return this.duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    private static float roundOneDecimal(float d) {
        return ((float) ((int) (d * 10.0f))) / 10.0f;
    }
}
