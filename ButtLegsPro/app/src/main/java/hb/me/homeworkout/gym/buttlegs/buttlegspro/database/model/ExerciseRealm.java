package hb.me.homeworkout.gym.buttlegs.buttlegspro.database.model;

public class ExerciseRealm {
    private int duration;
    private long id;
    private String image;
    private int level;
    private String name;
    private long time;

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

    public int getDuration() {
        return this.duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getLevel() {
        return this.level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDurationFormatted() {
        int duration = getDuration() / 1000;
        int sec = duration % 60;
        return (duration / 60) + ":" + (sec < 10 ? "0" + sec : Integer.valueOf(sec));
    }
}
