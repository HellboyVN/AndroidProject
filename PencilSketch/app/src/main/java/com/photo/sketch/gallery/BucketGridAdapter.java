package com.photo.sketch.gallery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.hbtools.photo.sketchpencil.editer.pencilsketch.R;

import java.util.ArrayList;

public class BucketGridAdapter extends ArrayAdapter<BucketEntry> {
    private ArrayList<BucketEntry> mBucketEntryList;
    private Context mContext;
    private boolean mIsFromVideo;
    private int mWidth;
    LayoutInflater viewInflater ;

    class ViewHolder {
        ImageView imageView;
        TextView nameTextView;

        ViewHolder() {
        }
    }

    public BucketGridAdapter(Context context, int resource, ArrayList<BucketEntry> categories, boolean isFromVideo) {
        super(context, resource, categories);
        this.mBucketEntryList = categories;
        this.mContext = context;
        this.mIsFromVideo = isFromVideo;
        this.viewInflater = LayoutInflater.from(this.mContext);
    }

    public int getCount() {
        return this.mBucketEntryList.size();
    }

    public BucketEntry getItem(int position) {
        return (BucketEntry) this.mBucketEntryList.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public void addLatestEntry(String url) {
        int count = this.mBucketEntryList.size();
        boolean success = false;
        for (int i = 0; i < count; i++) {
            if (((BucketEntry) this.mBucketEntryList.get(i)).bucketName.equals(MediaChooserConstants.folderName)) {
                ((BucketEntry) this.mBucketEntryList.get(i)).bucketUrl = url;
                success = true;
                break;
            }
        }
        if (!success) {
            this.mBucketEntryList.add(0, new BucketEntry(0, MediaChooserConstants.folderName, url));
        }
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            this.mWidth = this.mContext.getResources().getDisplayMetrics().widthPixels;
            convertView = this.viewInflater.inflate(R.layout.view_grid_bucket_item_media_chooser, parent, false);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.imageViewFromMediaChooserBucketRowView);
            holder.nameTextView = (TextView) convertView.findViewById(R.id.nameTextViewFromMediaChooserBucketRowView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        LayoutParams imageParams = (LayoutParams) holder.imageView.getLayoutParams();
        imageParams.width = this.mWidth / 2;
        imageParams.height = this.mWidth / 2;
        holder.imageView.setLayoutParams(imageParams);
        if (!this.mIsFromVideo) {
            new ImageLoadAsync(this.mContext, holder.imageView, this.mWidth / 2).executeOnExecutor(MediaAsync.THREAD_POOL_EXECUTOR, ((BucketEntry) this.mBucketEntryList.get(position)).bucketUrl);
        }
        holder.nameTextView.setText(((BucketEntry) this.mBucketEntryList.get(position)).bucketName);
//        holder.imageView.setImageURI(Uri.parse(this.mBucketEntryList.get(position).bucketUrl));
        return convertView;
    }
}
