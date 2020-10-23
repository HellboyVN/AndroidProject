package com.workout.workout.modal;

import android.os.Parcel;
import android.os.Parcelable;

public class Training extends RowItem implements Parcelable {
    public static final Creator<Training> CREATOR = new Creator<Training>() {
        public Training createFromParcel(Parcel in) {
            return new Training(in);
        }

        public Training[] newArray(int size) {
            return new Training[size];
        }
    };
    private String image_url;
    private String part_image_name;
    private String part_name;

    public Training(String part_name, String image_url) {
        this.part_name = part_name;
        this.image_url = image_url;
    }



    public String getPart_name() {
        return this.part_name;
    }

    public void setPart_name(String part_name) {
        this.part_name = part_name;
    }

    public String getImage_url() {
        return this.image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getPart_image_name() {
        return this.part_image_name;
    }

    public void setPart_image_name(String part_image_name) {
        this.part_image_name = part_image_name;
    }
    public Training(){

    }
    public Training(Parcel in) {
        this.part_name = in.readString();
        this.part_image_name = in.readString();
        this.image_url = in.readString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.part_name);
        dest.writeString(this.part_image_name);
        dest.writeString(this.image_url);
    }
}
