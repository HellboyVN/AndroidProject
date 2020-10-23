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
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.modal.Workout;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.util.AppUtil;
import java.util.ArrayList;

public class AddWorkoutToPlanExerciseSpinnerAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Workout> suggestionList = new ArrayList();

    private static class ViewHolder {
        ImageView imageViewWorkout;
        ProgressBar progressBar;
        TextView textViewWorkoutName;

        private ViewHolder() {
        }
    }

    public AddWorkoutToPlanExerciseSpinnerAdapter(Context context, ArrayList<Workout> originalList) {
        this.context = context;
        this.suggestionList = originalList;
    }

    public int getCount() {
        return this.suggestionList.size();
    }

    public Workout getItem(int position) {
        return (Workout) this.suggestionList.get(position);
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
            holder.textViewWorkoutName = (TextView) convertView.findViewById(R.id.textViewWorkoutName);
            holder.imageViewWorkout = (ImageView) convertView.findViewById(R.id.imageViewWorkout);
            holder.progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String workoutName = ((Workout) this.suggestionList.get(position)).getWorkout_name();
        String workoutImageUrl = ((Workout) this.suggestionList.get(position)).getWorkout_image_url();
        holder.textViewWorkoutName.setText(workoutName);
        if (!AppUtil.isEmpty(workoutImageUrl)) {
            holder.progressBar.setVisibility(View.VISIBLE);
            Glide.with(this.context).load(workoutImageUrl).centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).listener(new RequestListener<String, GlideDrawable>() {
                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                    holder.progressBar.setVisibility(View.GONE);
                    return false;
                }

                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                    holder.progressBar.setVisibility(View.GONE);
                    return false;
                }
            }).into(holder.imageViewWorkout);
        } else if (((Workout) this.suggestionList.get(position)).getWorkout_image_name() != null) {
            int resID = this.context.getResources().getIdentifier(((Workout) this.suggestionList.get(position)).getWorkout_image_name(), "drawable", this.context.getPackageName());
            holder.imageViewWorkout.setVisibility(View.VISIBLE);
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
                }).into(holder.imageViewWorkout);
            } else {
                holder.progressBar.setVisibility(View.GONE);
            }
        } else {
            holder.progressBar.setVisibility(View.GONE);
            holder.imageViewWorkout.setVisibility(View.GONE);
        }
        return convertView;
    }

    public ArrayList<Workout> getSuggestionList() {
        return this.suggestionList;
    }
}
