package com.photo.sketch.gallery;

public class MediaModel {
    public boolean status = false;
    public String url = null;

    public MediaModel(String url, boolean status) {
        this.url = url;
        this.status = status;
    }
}
