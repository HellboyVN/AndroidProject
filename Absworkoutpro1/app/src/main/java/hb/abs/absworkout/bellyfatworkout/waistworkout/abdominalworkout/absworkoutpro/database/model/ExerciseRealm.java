package hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.database.model;

import io.realm.RealmObject;
import io.realm.internal.RealmObjectProxy;

public class ExerciseRealm extends RealmObject   {
    private int duration;
    private int gender;
    private long id;
    private String image;
    private int level;
    private String name;
    private long time;

    public int realmGet$duration() {
        return this.duration;
    }

    public int realmGet$gender() {
        return this.gender;
    }

    public long realmGet$id() {
        return this.id;
    }

    public String realmGet$image() {
        return this.image;
    }

    public int realmGet$level() {
        return this.level;
    }

    public String realmGet$name() {
        return this.name;
    }

    public long realmGet$time() {
        return this.time;
    }

    public void realmSet$duration(int i) {
        this.duration = i;
    }

    public void realmSet$gender(int i) {
        this.gender = i;
    }

    public void realmSet$id(long j) {
        this.id = j;
    }

    public void realmSet$image(String str) {
        this.image = str;
    }

    public void realmSet$level(int i) {
        this.level = i;
    }

    public void realmSet$name(String str) {
        this.name = str;
    }

    public void realmSet$time(long j) {
        this.time = j;
    }

    public ExerciseRealm() {
        if (this instanceof RealmObjectProxy) {
            ((RealmObjectProxy) this).realm$injectObjectContext();
        }
    }

    public long getId() {
        return realmGet$id();
    }

    public void setId(long id) {
        realmSet$id(id);
    }

    public long getTime() {
        return realmGet$time();
    }

    public void setTime(long time) {
        realmSet$time(time);
    }

    public int getDuration() {
        return realmGet$duration();
    }

    public void setDuration(int duration) {
        realmSet$duration(duration);
    }

    public int getLevel() {
        return realmGet$level();
    }

    public void setLevel(int level) {
        realmSet$level(level);
    }

    public String getName() {
        return realmGet$name();
    }

    public void setName(String name) {
        realmSet$name(name);
    }

    public String getImage() {
        return realmGet$image();
    }

    public void setImage(String image) {
        realmSet$image(image);
    }

    public int getGender() {
        return realmGet$gender();
    }

    public void setGender(int gender) {
        realmSet$gender(gender);
    }

    public String getDurationFormatted() {
        int duration = getDuration() / 1000;
        int sec = duration % 60;
        return (duration / 60) + ":" + (sec < 10 ? "0" + sec : Integer.valueOf(sec));
    }
}
