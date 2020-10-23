package com.photo.sketch.gallery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;

import com.hbtools.photo.sketchpencil.editer.pencilsketch.R;

import java.util.List;

public class GridViewAdapter extends ArrayAdapter<MediaModel> {
    private Context mContext;
    private List<MediaModel> mGalleryModelList;
    private boolean mIsFromVideo;
    private int mWidth;
    LayoutInflater viewInflater;

    class ViewHolder {
        CheckedTextView checkBoxTextView;
        ImageView imageView;

        ViewHolder() {
        }
    }

    public GridViewAdapter(Context context, int resource, List<MediaModel> categories, boolean isFromVideo) {
        super(context, resource, categories);
        this.mGalleryModelList = categories;
        this.mContext = context;
        this.mIsFromVideo = isFromVideo;
        this.viewInflater = LayoutInflater.from(this.mContext);
    }

    public int getCount() {
        return this.mGalleryModelList.size();
    }

    public MediaModel getItem(int position) {
        return (MediaModel) this.mGalleryModelList.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            this.mWidth = this.mContext.getResources().getDisplayMetrics().widthPixels;
            convertView = this.viewInflater.inflate(R.layout.view_grid_item_media_chooser, parent, false);
            holder = new ViewHolder();
            holder.checkBoxTextView = (CheckedTextView) convertView.findViewById(R.id.checkTextViewFromMediaChooserGridItemRowView);
            holder.imageView = (ImageView) convertView.findViewById(R.id.imageViewFromMediaChooserGridItemRowView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        LayoutParams imageParams = (LayoutParams) holder.imageView.getLayoutParams();
        imageParams.width = this.mWidth / 2;
        imageParams.height = this.mWidth / 2;
        holder.imageView.setLayoutParams(imageParams);
        if (!this.mIsFromVideo) {
            new ImageLoadAsync(this.mContext, holder.imageView, this.mWidth / 2).executeOnExecutor(MediaAsync.THREAD_POOL_EXECUTOR, ((MediaModel) this.mGalleryModelList.get(position)).url);
        }
        holder.checkBoxTextView.setChecked(((MediaModel) this.mGalleryModelList.get(position)).status);
        return convertView;
    }
}
