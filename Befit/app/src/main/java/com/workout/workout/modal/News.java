package com.workout.workout.modal;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class News extends RowItem implements Parcelable {
    public static final Creator<News> CREATOR = new Creator<News>() {
        public News createFromParcel(Parcel in) {
            return new News(in);
        }

        public News[] newArray(int size) {
            return new News[size];
        }
    };
    private String body;
    private String date;
    private String image;
    private List<News> newsList;
    private boolean read;
    private boolean seen;
    private String title;

    public News(){

    }
    public News(String date, String image, String title, String body, boolean read, boolean seen) {
        this.date = date;
        this.image = image;
        this.title = title;
        this.body = body;
        this.read = read;
        this.seen = seen;
    }

    public String toString() {
        return this.date;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return this.body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isSeen() {
        return this.seen;
    }

    public void seen(boolean seen) {
        this.seen = seen;
    }

    public boolean isRead() {
        return this.read;
    }

    public void read(boolean read) {
        this.read = read;
    }

    protected News(Parcel in) {
        boolean z = false;
        this.date = in.readString();
        this.image = in.readString();
        this.title = in.readString();
        this.body = in.readString();
        this.seen = in.readByte() != (byte) 0;
        if (in.readByte() != (byte) 0) {
            z = true;
        }
        this.read = z;
        if (in.readByte() == (byte) 1) {
            this.newsList = new ArrayList();
            in.readList(this.newsList, News.class.getClassLoader());
            return;
        }
        this.newsList = null;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        int i;
        dest.writeString(this.date);
        dest.writeString(this.image);
        dest.writeString(this.title);
        dest.writeString(this.body);
        dest.writeByte((byte) (this.seen ? 1 : 0));
        if (this.read) {
            i = 1;
        } else {
            i = 0;
        }
        dest.writeByte((byte) i);
        if (this.newsList == null) {
            dest.writeByte((byte) 0);
            return;
        }
        dest.writeByte((byte) 1);
        dest.writeList(this.newsList);
    }
}
