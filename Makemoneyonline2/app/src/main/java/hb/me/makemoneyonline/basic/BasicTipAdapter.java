package hb.me.makemoneyonline.basic;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import hb.me.makemoneyonline.LogUtils;
import hb.me.makemoneyonline.R;
import hb.me.makemoneyonline.tip.DetailTipActivity;


/**
 * Created by nxtruong on 8/12/2017.
 */

public class BasicTipAdapter extends RecyclerView.Adapter<BasicTipAdapter.MyViewHolder> {
    private LayoutInflater inflator;
    private List<BasicPageItem> listTips = Collections.emptyList();
    private Context context;


    public BasicTipAdapter(Context context, List<BasicPageItem> tipsList) {
        this.context = context;
        inflator = LayoutInflater.from(context);
        this.listTips = tipsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflator.inflate(R.layout.item_list_tip, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        BasicPageItem tipItem = listTips.get(position);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd | HH:mm:ss");
        String currentDateandTime = sdf.format(new Date());
        holder.title.setText(tipItem.getTitle());
        holder.count.setText(tipItem.getLocalUrl());
        holder.date.setText(currentDateandTime);
        try {
            // get input stream
            InputStream ims = context.getAssets().open("page"+ String.valueOf(position+1)+"/demo.png");
            // load image as Drawable
            Drawable d = Drawable.createFromStream(ims, null);
            // set image to ImageView
            holder.profileView.setImageDrawable(d);
        }
        catch(IOException ex) {
            return;
        }
    }


    @Override
    public int getItemCount() {
        return listTips.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, date, count;
        ImageView profileView;
        View mainView;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            date = (TextView) itemView.findViewById(R.id.date);
            count = (TextView) itemView.findViewById(R.id.count);
            profileView = (ImageView) itemView.findViewById(R.id.image_thumb);

            mainView = itemView;
            mainView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LogUtils.d("Click Item");
                    Intent intent = new Intent(context, DetailTipActivity.class);
                    Log.e("levan",title.getText().toString());
                    intent.putExtra("title",title.getText().toString());
                    intent.putExtra("url",count.getText().toString());//"http://139.59.119.16/sample-page/"
                    context.startActivity(intent);
                }
            });
        }

    }

}
