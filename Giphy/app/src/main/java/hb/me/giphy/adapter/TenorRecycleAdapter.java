package hb.me.giphy.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.koushikdutta.ion.Ion;

import java.util.List;
import java.util.Random;

import hb.me.giphy.Admob;
import hb.me.giphy.R;
import hb.me.giphy.activity.DetailGifActivity;
import hb.me.giphy.model.GifItem;
import hb.me.giphy.util.AppUtils;

import static android.R.attr.padding;

/**
 * Created by nxtruong on 7/12/2017.
 */

public class TenorRecycleAdapter extends RecyclerView.Adapter<TenorRecycleAdapter.SimpleViewHolder> {
    private Context mContext;
    private List<GifItem> mListItems;

    Admob admob;

    public void setAdmob(Admob admob) {
        this.admob = admob;
    }

    public TenorRecycleAdapter(Context context, List<GifItem> alldata) {

        this.mContext = context;
        mListItems = alldata;
    }

    @Override
    public TenorRecycleAdapter.SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        SimpleViewHolder viewHolder = new SimpleViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grid_image, parent, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, int position) {
        GifItem item = mListItems.get(position);
        ImageView image = holder.imageView;
        int width = screenWidth(image.getContext());
        image.setLayoutParams(new LinearLayout.LayoutParams(width, calculateHeight(item.getWidthTinyGif(), item.getHeightTinyGif(), width)));
        image.setBackgroundColor(randomColor());
        Ion.with(this.mContext).load(item.getTinyGifUrl()).intoImageView(image);
    }

    public void addItem(List<GifItem> moreData) {
        int start = mListItems.size();
        mListItems.addAll(moreData);
        notifyItemRangeInserted(start, moreData.size());
    }

    public int getItemCount() {
        return mListItems.size();
    }

    private int screenWidth(Context context) {
        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x / 2;
    }

    private int calculateHeight(String width, String high, int screenWidth) {
        return (Integer.parseInt(high) * screenWidth) / Integer.parseInt(width);
    }

    private int randomColor() {
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }

    public class SimpleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView imageView;

        public SimpleViewHolder(View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.image);
            itemView.setOnClickListener(this);
        }

        public void onClick(View view) {
            try {
                GifItem item = mListItems.get(getAdapterPosition());
                Intent intent = new Intent(mContext, DetailGifActivity.class);
                intent.putExtra("title", item.getTitle());
                intent.putExtra("gif_preview", item.getTinyGifUrl());
                intent.putExtra("gif_link", item.getKeyGifUrl());
                intent.putExtra("gif_mp4", item.getLoopedMp4Url());
                intent.putExtra("tags", item.getTags());
                intent.putExtra("facebook_link", item.getUrl());
                if (AppUtils.isLessShowAd() && admob != null && padding >= 8) {
                    admob.showInterstitialAd(() -> {
                        mContext.startActivity(intent);
                    });
                } else {
                    mContext.startActivity(intent);
                }


            } catch (ArrayIndexOutOfBoundsException e) {
                Toast.makeText(mContext, R.string.get_scroll, Toast.LENGTH_LONG).show();
            }
        }
    }
}
