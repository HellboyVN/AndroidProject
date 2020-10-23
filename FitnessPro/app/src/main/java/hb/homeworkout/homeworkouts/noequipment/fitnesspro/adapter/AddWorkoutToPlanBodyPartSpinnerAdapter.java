package hb.homeworkout.homeworkouts.noequipment.fitnesspro.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.R;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.modal.Training;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.util.AppUtil;
import java.util.ArrayList;

public class AddWorkoutToPlanBodyPartSpinnerAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Training> suggestionList = new ArrayList();

    private static class ViewHolder {
        ImageView imageViewBodyPart;
        ProgressBar progressBar;
        TextView textViewBodyPartName;

        private ViewHolder() {
        }
    }

    public AddWorkoutToPlanBodyPartSpinnerAdapter(Context context, ArrayList<Training> originalList) {
        this.context = context;
        this.suggestionList = originalList;
    }

    public int getCount() {
        return this.suggestionList.size();
    }

    public Training getItem(int position) {
        return (Training) this.suggestionList.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        LayoutInflater inflater = LayoutInflater.from(this.context);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.create_plan_workout_list_row, parent, false);
            holder = new ViewHolder();
            holder.textViewBodyPartName = (TextView) convertView.findViewById(R.id.textViewWorkoutName);
            holder.imageViewBodyPart = (ImageView) convertView.findViewById(R.id.imageViewWorkout);
            holder.progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String bodyPartName = ((Training) this.suggestionList.get(position)).getPart_name();
        String bodyPartImageUrl = ((Training) this.suggestionList.get(position)).getImage_url();
        holder.textViewBodyPartName.setText(bodyPartName);
        if (!AppUtil.isEmpty(bodyPartImageUrl)) {
            holder.progressBar.setVisibility(View.VISIBLE);
            Glide.with(this.context).load(bodyPartImageUrl).centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).listener(new RequestListener<String, GlideDrawable>() {
                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                    holder.progressBar.setVisibility(View.GONE);
                    return false;
                }

                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                    holder.progressBar.setVisibility(View.GONE);
                    return false;
                }
            }).into(holder.imageViewBodyPart);
        } else if (((Training) this.suggestionList.get(position)).getPart_image_name() != null) {
            int resID = this.context.getResources().getIdentifier(((Training) this.suggestionList.get(position)).getPart_image_name(), "drawable", this.context.getPackageName());
            holder.imageViewBodyPart.setVisibility(View.VISIBLE);
            if (resID != 0) {
                Glide.with(this.context).load(Integer.valueOf(resID)).crossFade().centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).listener(new RequestListener<Integer, GlideDrawable>() {
                    public boolean onException(Exception e, Integer model, Target<GlideDrawable> target, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    public boolean onResourceReady(GlideDrawable resource, Integer model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }
                }).into(holder.imageViewBodyPart);
            } else {
                holder.progressBar.setVisibility(View.GONE);
            }
        } else {
            holder.progressBar.setVisibility(View.GONE);
            holder.imageViewBodyPart.setVisibility(View.GONE);
        }
        return convertView;
    }
}
