package hb.homeworkout.homeworkouts.noequipment.fitnesspro.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.R;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.activity.WorkoutDetailActivity;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.constant.AppConstants;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.modal.Workout;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.util.AppUtil;

public class SearchWorkoutAdapter extends SimpleCursorAdapter {
    private static final String tag = SearchWorkoutAdapter.class.getName();
    private Context context = null;

    public SearchWorkoutAdapter(Context context, int layout, Cursor cursor, String[] from, int[] to, int flags) {
        super(context, layout, cursor, from, to, flags);
        this.context = context;
    }

    public void bindView(View view, Context context, Cursor cursor) {
        ImageView imageViewWorkout = (ImageView) view.findViewById(R.id.imageViewWorkout);
        TextView textView = (TextView) view.findViewById(R.id.textViewWorkoutName);
        final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        String workoutId = cursor.getString(0);
        String workoutName = cursor.getString(1);
        String drawableName = cursor.getString(2);
        String workoutImageUrl = cursor.getString(3);
        String workoutBodyPartImageName = cursor.getString(4);
        String workoutDescription = cursor.getString(5);
        String workoutVideoName = cursor.getString(6);
        String workoutVideoUrl = cursor.getString(7);
        String workoutBodyPartImageUrl = cursor.getString(8);
        final Workout workout = new Workout();
        workout.setWorkout_id(workoutId);
        workout.setWorkout_video_name(workoutVideoName);
        workout.setWorkout_name(workoutName);
        workout.setWorkout_description(workoutDescription);
        workout.setBody_part_name(workoutBodyPartImageName);
        workout.setWorkout_video_url(workoutVideoUrl);
        workout.setWorkout_image_name(drawableName);
        workout.setWorkout_image_url(workoutImageUrl);
        workout.setBody_part_image_url(workoutBodyPartImageUrl);
        textView.setText(workoutName);
        textView.setTextColor(ContextCompat.getColor(context, R.color.white));
        if (!AppUtil.isEmpty(workoutImageUrl)) {
            progressBar.setVisibility(View.VISIBLE);
            Glide.with(context).load(workoutImageUrl).centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).listener(new RequestListener<String, GlideDrawable>() {
                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                    progressBar.setVisibility(View.GONE);
                    return false;
                }

                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                    progressBar.setVisibility(View.GONE);
                    return false;
                }
            }).into(imageViewWorkout);
        } else if (drawableName != null) {
            imageViewWorkout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            int resID = context.getResources().getIdentifier(drawableName, "drawable", context.getPackageName());
            if (resID != 0) {
                Glide.with(context).load(Integer.valueOf(resID)).override(AppUtil.dpToPx(80), AppUtil.dpToPx(80)).crossFade().centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).listener(new RequestListener<Integer, GlideDrawable>() {
                    public boolean onException(Exception e, Integer model, Target<GlideDrawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    public boolean onResourceReady(GlideDrawable resource, Integer model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                }).into(imageViewWorkout);
            } else {
                progressBar.setVisibility(View.GONE);
            }
        } else {
            imageViewWorkout.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
        }
        final Context context2 = context;
        view.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(context2, WorkoutDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable(AppConstants.WORKOUT_OBJECT, workout);
                intent.putExtras(bundle);
                context2.startActivity(intent);
            }
        });
    }
}
