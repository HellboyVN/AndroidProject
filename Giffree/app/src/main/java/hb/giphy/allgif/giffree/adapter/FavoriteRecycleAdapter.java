package hb.giphy.allgif.giffree.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;

import java.util.List;

import hb.giphy.allgif.giffree.R;
import hb.giphy.allgif.giffree.model.FavoriteItemRealm;


/**
 * Created by nxtruong on 7/15/2017.
 */

public class FavoriteRecycleAdapter extends BaseAdapter {
    private List<FavoriteItemRealm> mData;
    private Context mContext;
    private LayoutInflater inflater;

    public FavoriteRecycleAdapter(Context context, List<FavoriteItemRealm> data) {
        inflater = LayoutInflater.from(context);
        mContext = context;
        mData = data;
    }

    public int getCount() {
        return mData.size();
    }

    public Object getItem(int position) {
        return mData.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        SimpleViewHolder girdViewImageholder;
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.fav_grid_image, parent, false);
            girdViewImageholder = new SimpleViewHolder();
            girdViewImageholder.imageView = (ImageView) view.findViewById(R.id.image);
            girdViewImageholder.textView = (TextView) view.findViewById(R.id.text_view_tags);
            girdViewImageholder.imageView.setMaxHeight(80);
            girdViewImageholder.imageView.setMaxWidth(80);
            view.setTag(girdViewImageholder);
        }
        girdViewImageholder = (SimpleViewHolder) view.getTag();
        FavoriteItemRealm item = mData.get(position);
        Ion.with(mContext).load(item.getGifPreview()).intoImageView(girdViewImageholder.imageView);
        return view;
    }

    class SimpleViewHolder {
        ImageView imageView;
        TextView textView;

        SimpleViewHolder() {
        }
    }
}
