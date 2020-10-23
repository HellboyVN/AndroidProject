package hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.reminder.data;

import android.support.annotation.NonNull;
import java.util.List;

public class TimeData implements Comparable<TimeData> {
    private boolean active;
    private List<Boolean> listDay;
    private Time time;
    private int viewType = -1;
    public TimeData(){

    }
    public TimeData(Time time, List<Boolean> listDay, boolean active) {
        this.time = time;
        this.listDay = listDay;
        this.active = active;
    }

    public Time getTime() {
        return this.time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public List<Boolean> getListDay() {
        return this.listDay;
    }

    public void setListDay(List<Boolean> listDay) {
        this.listDay = listDay;
    }

    public boolean isActive() {
        return this.active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getViewType() {
        return this.viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public int compareTo(@NonNull TimeData o) {
        if ((getTime().getHour() * 60) + getTime().getMinute() < (o.getTime().getHour() * 60) + o.getTime().getMinute()) {
            return -1;
        }
        return 1;
    }
}
