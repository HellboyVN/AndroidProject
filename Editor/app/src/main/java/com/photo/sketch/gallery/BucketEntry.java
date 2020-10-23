package com.photo.sketch.gallery;

public class BucketEntry {
    public int bucketId;
    public String bucketName;
    public String bucketUrl = null;

    public BucketEntry(int id, String name, String url) {
        this.bucketId = id;
        this.bucketName = ensureNotNull(name);
        this.bucketUrl = url;
    }

    public int hashCode() {
        return this.bucketId;
    }

    public boolean equals(Object object) {
        if (!(object instanceof BucketEntry)) {
            return false;
        }
        if (this.bucketId == ((BucketEntry) object).bucketId) {
            return true;
        }
        return false;
    }

    public static String ensureNotNull(String value) {
        return value == null ? "" : value;
    }
}
