package com.photo.sketch.gallery;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.aerotools.photo.sketch.maker.editor.R;
import java.io.File;
import java.util.List;

public class MediaGridViewAdapter extends ArrayAdapter<String> {
    private Context mContext;
    private int mWidth;
    private List<String> mediaFilePathList;
    LayoutInflater viewInflater = LayoutInflater.from(this.mContext);

    class ViewHolder {
        ImageView imageView;
        TextView nameTextView;

        ViewHolder() {
        }
    }

    public MediaGridViewAdapter(Context context, int resource, List<String> filePathList) {
        super(context, resource, filePathList);
        this.mediaFilePathList = filePathList;
        this.mContext = context;
    }

    public int getCount() {
        return this.mediaFilePathList.size();
    }

    public String getItem(int position) {
        return (String) this.mediaFilePathList.get(position);
    }

    public void addAll(List<String> mediaFile) {
        if (mediaFile != null) {
            int count = mediaFile.size();
            for (int i = 0; i < count; i++) {
                if (!this.mediaFilePathList.contains(mediaFile.get(i))) {
                    this.mediaFilePathList.add((String) mediaFile.get(i));
                }
            }
        }
    }

    public long getItemId(int position) {
        return (long) position;
    }

    @SuppressLint({"NewApi"})
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            this.mWidth = this.mContext.getResources().getDisplayMetrics().widthPixels;
            convertView = this.viewInflater.inflate(R.layout.view_grid_item, parent, false);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.imageViewFromGridItemRowView);
            holder.nameTextView = (TextView) convertView.findViewById(R.id.nameTextView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        LayoutParams imageParams = (LayoutParams) holder.imageView.getLayoutParams();
        imageParams.width = this.mWidth / 2;
        imageParams.height = this.mWidth / 2;
        holder.imageView.setLayoutParams(imageParams);
        File mediaFile = new File((String) this.mediaFilePathList.get(position));
        if (mediaFile.exists()) {
            if (mediaFile.getPath().contains("mp4") || mediaFile.getPath().contains("wmv") || mediaFile.getPath().contains("avi") || mediaFile.getPath().contains("3gp")) {
                holder.imageView.setImageBitmap(null);
            } else {
                Options options = new Options();
                options.inPurgeable = true;
                options.inSampleSize = 2;
                holder.imageView.setImageBitmap(BitmapFactory.decodeFile(mediaFile.getAbsolutePath(), options));
            }
            holder.nameTextView.setText(mediaFile.getName());
        }
        return convertView;
    }
}
