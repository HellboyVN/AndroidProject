package hellboy.bka.tlvan.os11lockscreen.adapter;

import android.content.Context;
import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Gallery.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import java.util.ArrayList;

import hellboy.bka.tlvan.os11lockscreen.entity.BackEntity;

public class AdapterGalery extends ArrayAdapter<BackEntity> {
    private Context mContext;
    private ArrayList<BackEntity> myListBackground = new ArrayList();

    public AdapterGalery(Context context, int resource, ArrayList<BackEntity> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.myListBackground = objects;
    }

    public int getCount() {
        return this.myListBackground.size();
    }

    public BackEntity getItem(int position) {
        return (BackEntity) this.myListBackground.get(position);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = new ImageView(this.mContext);
        imageView.setImageResource(getItem(position).getResources());
        imageView.setScaleType(ScaleType.FIT_XY);
        imageView.setLayoutParams(new LayoutParams(Callback.DEFAULT_DRAG_ANIMATION_DURATION, -1));
        return imageView;
    }
}
