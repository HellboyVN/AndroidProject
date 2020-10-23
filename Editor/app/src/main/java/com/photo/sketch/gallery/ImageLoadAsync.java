package com.photo.sketch.gallery;

import android.content.Context;
import android.widget.ImageView;

import com.aerotools.photo.sketch.maker.editor.R;
import com.squareup.picasso.Picasso;

import java.io.File;

public class ImageLoadAsync extends MediaAsync<String, String, String> {
    private Context mContext;
    private ImageView mImageView;
    private int mWidth;

    public ImageLoadAsync(Context context, ImageView imageView, int width) {
        this.mImageView = imageView;
        this.mContext = context;
        this.mWidth = width;
    }

    protected String doInBackground(String... params) {
        return params[0].toString();
    }

    protected void onPostExecute(String result) {
        Picasso.with(mContext).load(new File(result))
                .resize(mWidth, mWidth).centerCrop()
                .placeholder(R.drawable.ic_loading).into(mImageView);
    }
}
