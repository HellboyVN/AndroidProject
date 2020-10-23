package com.photo.sketch.photo;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import com.hbtools.photo.sketchpencil.editer.pencilsketch.R;

public class FilterFragment extends Fragment implements OnClickListener {
    Bitmap bitmap1;
    Bitmap bitmap2;
    Bitmap bitmap3;
    Bitmap bitmap4;
    ImageButton filterButton1;
    ImageButton filterButton2;
    ImageButton filterButton3;
    ImageButton filterButton4;

    public interface onFilterSlectListener {
        void onFilterSlect(int i);
    }

    public FilterFragment(Bitmap filterBitmap1, Bitmap filterBitmap2, Bitmap filterBitmap3, Bitmap filterBitmap4) {
        this.bitmap1 = filterBitmap1;
        this.bitmap2 = filterBitmap2;
        this.bitmap3 = filterBitmap3;
        this.bitmap4 = filterBitmap4;
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.filter_fragmnet, null);
        this.filterButton1 = (ImageButton) rootView.findViewById(R.id.filter1);
        this.filterButton2 = (ImageButton) rootView.findViewById(R.id.filter2);
        this.filterButton3 = (ImageButton) rootView.findViewById(R.id.filter3);
        this.filterButton4 = (ImageButton) rootView.findViewById(R.id.filter4);
        this.filterButton1.setImageBitmap(this.bitmap1);
        this.filterButton2.setImageBitmap(this.bitmap2);
        this.filterButton3.setImageBitmap(this.bitmap3);
        this.filterButton4.setImageBitmap(this.bitmap4);
        this.filterButton1.setOnClickListener(this);
        this.filterButton2.setOnClickListener(this);
        this.filterButton3.setOnClickListener(this);
        this.filterButton4.setOnClickListener(this);
        return rootView;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.filter1:
                ((onFilterSlectListener) getActivity()).onFilterSlect(1);
                return;
            case R.id.filter2:
                ((onFilterSlectListener) getActivity()).onFilterSlect(2);
                return;
            case R.id.filter3:
                ((onFilterSlectListener) getActivity()).onFilterSlect(3);
                return;
            case R.id.filter4:
                ((onFilterSlectListener) getActivity()).onFilterSlect(4);
                return;
            default:
                return;
        }
    }
}
