package hb.homeworkout.homeworkouts.noequipment.fitnesspro.modal;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Plan extends RowItem implements Parcelable {
    public static final Creator<Plan> CREATOR = new Creator<Plan>() {
        public Plan createFromParcel(Parcel in) {
            return new Plan(in);
        }

        public Plan[] newArray(int size) {
            return new Plan[size];
        }
    };
    private String body_part_image_name;
    private String body_part_image_url;
    private String body_part_name;
    private String day;
    private boolean isLocked;
    private boolean isMyPlan;
    private List<Plan> planList;
    private String plan_description;
    private String plan_id;
    private String plan_image_name;
    private String plan_image_url;
    private String plan_name;
    private Long plan_price;
    private String plan_type;
    private String sets_and_reps;
    private String week_number;
    private String workout_id;
    private String workout_image_name;
    private String workout_image_url;
    private String workout_name;

    public String getPlan_image_name() {
        return this.plan_image_name;
    }

    public void setPlan_image_name(String plan_image_name) {
        this.plan_image_name = plan_image_name;
    }

    public boolean isMyPlan() {
        return this.isMyPlan;
    }

    public void setMyPlan(boolean myPlan) {
        this.isMyPlan = myPlan;
    }

    public Long getPlan_price() {
        return this.plan_price;
    }

    public void setPlan_price(Long plan_price) {
        this.plan_price = plan_price;
    }

    public String getPlan_image_url() {
        return this.plan_image_url;
    }

    public void setPlan_image_url(String plan_image_url) {
        this.plan_image_url = plan_image_url;
    }

    public boolean isLocked() {
        return this.isLocked;
    }

    public void setLocked(boolean locked) {
        this.isLocked = locked;
    }

    public List<Plan> getPlanList() {
        return this.planList;
    }

    public void setPlanList(List<Plan> planList) {
        this.planList = planList;
    }

    public String getWorkout_image_name() {
        return this.workout_image_name;
    }

    public void setWorkout_image_name(String workout_image_name) {
        this.workout_image_name = workout_image_name;
    }

    public String getWorkout_image_url() {
        return this.workout_image_url;
    }

    public void setWorkout_image_url(String workout_image_url) {
        this.workout_image_url = workout_image_url;
    }

    public String getPlan_id() {
        return this.plan_id;
    }

    public void setPlan_id(String plan_id) {
        this.plan_id = plan_id;
    }

    public String getPlan_name() {
        return this.plan_name;
    }

    public void setPlan_name(String plan_name) {
        this.plan_name = plan_name;
    }

    public String getPlan_type() {
        return this.plan_type;
    }

    public void setPlan_type(String plan_type) {
        this.plan_type = plan_type;
    }

    public String getPlan_description() {
        return this.plan_description;
    }

    public void setPlan_description(String plan_description) {
        this.plan_description = plan_description;
    }

    public String getDay() {
        return this.day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getBody_part_name() {
        return this.body_part_name;
    }

    public void setBody_part_name(String body_part_name) {
        this.body_part_name = body_part_name;
    }

    public String getBody_part_image_name() {
        return this.body_part_image_name;
    }

    public void setBody_part_image_name(String body_part_image_name) {
        this.body_part_image_name = body_part_image_name;
    }

    public String getBody_part_image_url() {
        return this.body_part_image_url;
    }

    public void setBody_part_image_url(String body_part_image_url) {
        this.body_part_image_url = body_part_image_url;
    }

    public String getWorkout_id() {
        return this.workout_id;
    }

    public void setWorkout_id(String workout_id) {
        this.workout_id = workout_id;
    }

    public String getWorkout_name() {
        return this.workout_name;
    }

    public void setWorkout_name(String workout_name) {
        this.workout_name = workout_name;
    }

    public String getSets_and_reps() {
        return this.sets_and_reps;
    }

    public void setSets_and_reps(String sets_and_reps) {
        this.sets_and_reps = sets_and_reps;
    }

    public String getWeek_number() {
        return this.week_number;
    }

    public void setWeek_number(String week_number) {
        this.week_number = week_number;
    }

    public String toString() {
        return this.plan_name;
    }
    public Plan(){

    }
    public Plan(Parcel in) {
        boolean z;
        boolean z2 = true;
        this.plan_id = in.readString();
        this.plan_name = in.readString();
        this.plan_type = in.readString();
        this.plan_description = in.readString();
        this.day = in.readString();
        this.body_part_name = in.readString();
        this.body_part_image_name = in.readString();
        this.body_part_image_url = in.readString();
        this.workout_id = in.readString();
        this.workout_name = in.readString();
        this.sets_and_reps = in.readString();
        this.workout_image_name = in.readString();
        this.workout_image_url = in.readString();
        if (in.readByte() == (byte) 1) {
            this.planList = new ArrayList();
            in.readList(this.planList, Plan.class.getClassLoader());
        } else {
            this.planList = null;
        }
        this.week_number = in.readString();
        if (in.readByte() != (byte) 0) {
            z = true;
        } else {
            z = false;
        }
        this.isLocked = z;
        this.plan_price = in.readByte() == (byte) 0 ? null : Long.valueOf(in.readLong());
        this.plan_image_url = in.readString();
        if (in.readByte() == (byte) 0) {
            z2 = false;
        }
        this.isMyPlan = z2;
        this.plan_image_name = in.readString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        int i;
        int i2 = 1;
        dest.writeString(this.plan_id);
        dest.writeString(this.plan_name);
        dest.writeString(this.plan_type);
        dest.writeString(this.plan_description);
        dest.writeString(this.day);
        dest.writeString(this.body_part_name);
        dest.writeString(this.body_part_image_name);
        dest.writeString(this.body_part_image_url);
        dest.writeString(this.workout_id);
        dest.writeString(this.workout_name);
        dest.writeString(this.sets_and_reps);
        dest.writeString(this.workout_image_name);
        dest.writeString(this.workout_image_url);
        if (this.planList == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeList(this.planList);
        }
        dest.writeString(this.week_number);
        if (this.isLocked) {
            i = 1;
        } else {
            i = 0;
        }
        dest.writeByte((byte) i);
        if (this.plan_price == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(this.plan_price.longValue());
        }
        dest.writeString(this.plan_image_url);
        if (!this.isMyPlan) {
            i2 = 0;
        }
        dest.writeByte((byte) i2);
        dest.writeString(this.plan_image_name);
    }
}
