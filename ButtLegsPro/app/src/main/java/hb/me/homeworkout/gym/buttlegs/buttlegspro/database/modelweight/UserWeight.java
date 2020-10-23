package hb.me.homeworkout.gym.buttlegs.buttlegspro.database.modelweight;

import android.support.annotation.NonNull;

import hb.me.homeworkout.gym.buttlegs.buttlegspro.utils.MetricHelper;
import io.realm.RealmObject;

public class UserWeight extends RealmObject implements Comparable<UserWeight> {
    private long id;
    private long time;
    private long user_id;
    private float weight;

    public long realmGet$id() {
        return this.id;
    }

    public long realmGet$time() {
        return this.time;
    }

    public long realmGet$user_id() {
        return this.user_id;
    }

    public float realmGet$weight() {
        return this.weight;
    }

    public void realmSet$id(long j) {
        this.id = j;
    }

    public void realmSet$time(long j) {
        this.time = j;
    }

    public void realmSet$user_id(long j) {
        this.user_id = j;
    }

    public void realmSet$weight(float f) {
        this.weight = f;
    }

    public UserWeight() {

    }

    public long getId() {
        return realmGet$id();
    }

    public void setId(long id) {
        realmSet$id(id);
    }

    public long getUserId() {
        return realmGet$user_id();
    }

    public void setUserId(long user_id) {
        realmSet$user_id(user_id);
    }

    public float getWeight(int metric) {
        if (metric == 1) {
            return MetricHelper.getInstance().convertKGtoLB(realmGet$weight());
        }
        return realmGet$weight();
    }

    public void setWeight(float weight) {
        realmSet$weight(weight);
    }

    public long getTime() {
        return realmGet$time();
    }

    public void setTime(long time) {
        realmSet$time(time);
    }

    public int compareTo(@NonNull UserWeight o) {
        return realmGet$time() < o.realmGet$time() ? -1 : 1;
    }
}
