package com.photo.sketch.gallery;

import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.util.LruCache;
import android.widget.ImageView;
import com.hbtools.photo.sketchpencil.editer.pencilsketch.R;
import java.util.ArrayList;

public class GalleryCache {
    private LruCache<String, Bitmap> mBitmapCache;
    private ArrayList<String> mCurrentTasks = new ArrayList();

    public GalleryCache(int size, int maxWidth, int maxHeight) {
        this.mBitmapCache = new LruCache<String, Bitmap>(size) {
            protected int sizeOf(String key, Bitmap b) {
                return (b.getHeight() * b.getWidth()) * 4;
            }
        };
    }

    private Bitmap getBitmapFromCache(String key) {
        return (Bitmap) this.mBitmapCache.get(key);
    }

    public void loadBitmap(Fragment mainActivity, String imageKey, ImageView imageView, boolean isScrolling) {
        Bitmap bitmap = getBitmapFromCache(imageKey);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            imageView.setImageResource(R.drawable.ic_loading);
        }
    }

    public void clear() {
        this.mBitmapCache.evictAll();
    }
}
