package hb.homeworkout.homeworkouts.noequipment.fitnesspro.modal;

import android.os.Parcel;
import android.os.Parcelable;

public class Workout extends RowItem implements Parcelable {
    public static final Creator<Workout> CREATOR = new Creator<Workout>() {
        public Workout createFromParcel(Parcel in) {
            return new Workout(in);
        }

        public Workout[] newArray(int size) {
            return new Workout[size];
        }
    };
    String body_part_image_name;
    String body_part_image_url;
    String body_part_name;
    String workout_description;
    String workout_id;
    String workout_image_name;
    String workout_image_url;
    String workout_name;
    String workout_video_name;
    String workout_video_url;

    public String getBody_part_name() {
        return this.body_part_name;
    }

    public void setBody_part_name(String body_part_name) {
        this.body_part_name = body_part_name;
    }

    public String getWorkout_description() {
        return this.workout_description;
    }

    public void setWorkout_description(String workout_description) {
        this.workout_description = workout_description;
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

    public String getWorkout_video_name() {
        return this.workout_video_name;
    }

    public void setWorkout_video_name(String workout_video_name) {
        this.workout_video_name = workout_video_name;
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

    public String getWorkout_video_url() {
        return this.workout_video_url;
    }

    public void setWorkout_video_url(String workout_video_url) {
        this.workout_video_url = workout_video_url;
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
    public Workout(){

    }
    public Workout(Parcel in) {
        this.body_part_name = in.readString();
        this.workout_description = in.readString();
        this.workout_id = in.readString();
        this.workout_name = in.readString();
        this.workout_video_name = in.readString();
        this.workout_image_name = in.readString();
        this.workout_image_url = in.readString();
        this.workout_video_url = in.readString();
        this.body_part_image_name = in.readString();
        this.body_part_image_url = in.readString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.body_part_name);
        dest.writeString(this.workout_description);
        dest.writeString(this.workout_id);
        dest.writeString(this.workout_name);
        dest.writeString(this.workout_video_name);
        dest.writeString(this.workout_image_name);
        dest.writeString(this.workout_image_url);
        dest.writeString(this.workout_video_url);
        dest.writeString(this.body_part_image_name);
        dest.writeString(this.body_part_image_url);
    }
}
