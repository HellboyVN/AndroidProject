package com.photo.sketch.gallery;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.hbtools.photo.sketchpencil.editer.pencilsketch.R;

import java.util.ArrayList;

public class BucketImageFragment extends Fragment {
    private static final String[] PROJECTION_BUCKET = new String[]{"bucket_id", "bucket_display_name", "_data"};
//    private static final String[] PROJECTION_BUCKET = {
//            MediaStore.Images.ImageColumns.BUCKET_ID,
//            MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
//            MediaStore.Images.ImageColumns.DATE_TAKEN,
//            MediaStore.Images.ImageColumns.DATA};
    private final int INDEX_BUCKET_ID = 0;
    private final int INDEX_BUCKET_NAME = 1;
    private final int INDEX_BUCKET_URL = 2;
    private BucketGridAdapter mBucketAdapter;
    private Cursor mCursor;
    private GridView mGridView;
    private View mView;

    public BucketImageFragment() {
        setRetainInstance(true);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (this.mView == null) {
            this.mView = inflater.inflate(R.layout.view_grid_layout_media_chooser, container, false);
            this.mGridView = (GridView) this.mView.findViewById(R.id.gridViewFromMediaChooser);
            init();
        } else {
            ((ViewGroup) this.mView.getParent()).removeView(this.mView);
            if (this.mBucketAdapter == null) {
                Toast.makeText(getActivity(), "No Image Available", Toast.LENGTH_SHORT).show();
            }
        }
        return this.mView;
    }

    private void init() {
        String orderBy = "datetaken";
        this.mCursor = getActivity().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, PROJECTION_BUCKET, null, null, "datetaken DESC");
        if (mCursor != null)
            mCursor.moveToFirst();
        ArrayList<BucketEntry> buffer = new ArrayList();
        while (this.mCursor.moveToNext()) {
            try {
                BucketEntry entry = new BucketEntry(this.mCursor.getInt(0), this.mCursor.getString(1), this.mCursor.getString(2));
                if (!buffer.contains(entry)) {
                    buffer.add(entry);
                }
            }catch (IllegalArgumentException e) {
                Log.e("error", e.toString());
            }
        }
        if (this.mCursor.getCount() > 0) {
            this.mBucketAdapter = new BucketGridAdapter(getActivity(), 0, buffer, false);
            this.mGridView.setAdapter(this.mBucketAdapter);
        } else {
            Toast.makeText(getActivity(), "No Image Available", Toast.LENGTH_SHORT).show();
        }
        this.mGridView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
                BucketEntry bucketEntry = (BucketEntry) adapter.getItemAtPosition(position);
                Intent selectImageIntent = new Intent(BucketImageFragment.this.getActivity(), HomeFragmentActivity.class);
                selectImageIntent.putExtra("name", bucketEntry.bucketName);
                selectImageIntent.putExtra("image", true);
                selectImageIntent.putExtra("isFromBucket", true);
                BucketImageFragment.this.getActivity().startActivityForResult(selectImageIntent, 1000);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mCursor != null) {
            mCursor.close();
        }
    }

    public BucketGridAdapter getAdapter() {
        return this.mBucketAdapter;
    }
}
