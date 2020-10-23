package com.photo.sketch.photo;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.hbtools.photo.sketchpencil.editer.pencilsketch.R;

@SuppressLint("ValidFragment")
public class PhotoDialouge extends DialogFragment {
    ImageView imageView;
    Bitmap mBitmap;

    public PhotoDialouge(Bitmap bitmap) {
        this.mBitmap = bitmap;
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.photo_dialogue, null);
        this.imageView = (ImageView) rootView.findViewById(R.id.photoImage);
        this.imageView.setImageBitmap(this.mBitmap);
        return rootView;
    }
}
