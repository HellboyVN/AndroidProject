package com.photo.sketch.gallery;

import android.app.ActivityManager;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

public class VideoLoadAsync extends MediaAsync<String, String, String> {
    private static GalleryCache mCache;
    public Fragment fragment;
    private ImageView mImageView;
    private boolean mIsScrolling;
    private int mWidth;

    public VideoLoadAsync(Fragment fragment, ImageView imageView, boolean isScrolling, int width) {
        this.mImageView = imageView;
        this.fragment = fragment;
        this.mWidth = width;
        this.mIsScrolling = isScrolling;
        int size = (1048576 * ((ActivityManager) fragment.getActivity().getSystemService("activity")).getMemoryClass()) / 8;
        GalleryRetainCache c = GalleryRetainCache.getOrCreateRetainableCache();
        mCache = c.mRetainedCache;
        if (mCache == null) {
            mCache = new GalleryCache(size, this.mWidth, this.mWidth);
            c.mRetainedCache = mCache;
        }
    }

    protected String doInBackground(String... params) {
        return params[0].toString();
    }

    protected void onPostExecute(String result) {
        mCache.loadBitmap(this.fragment, result, this.mImageView, this.mIsScrolling);
    }
}
