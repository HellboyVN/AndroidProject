package hb.homeworkout.homeworkouts.noequipment.fitnesspro.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import hb.homeworkout.homeworkouts.noequipment.fitnesspro.R;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.R.drawable;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.activity.TrainingDetailActivity;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.database.DatabaseManager;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.listener.OnListFragmentInteractionListener;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.modal.RowItem;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.modal.Training;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.modal.Workout;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.util.AppUtil;

public class WorkoutListAdapter extends Adapter<WorkoutListAdapter.ViewHolder> {
    private static final int VIEW_TYPE_NATIVE_APP = 1;
    private static final int VIEW_TYPE_WORKOUT = 0;
    private Context context;
    private final OnListFragmentInteractionListener listClickListener;
    private RequestManager requestManager;
    private List<RowItem> rowItemList;

    public class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
        public final ImageView imageViewLike;
        public final ImageView imageViewWorkout;
        public final View mView;
        private final ProgressBar progressBar;
        public final TextView textViewWorkoutName;
        public Training training;

        public ViewHolder(View view) {
            super(view);
            this.mView = view;
            this.textViewWorkoutName = (TextView) view.findViewById(R.id.textViewWorkoutName);
            this.imageViewWorkout = (ImageView) view.findViewById(R.id.imageViewWorkout);
            this.imageViewLike = (ImageView) view.findViewById(R.id.imageViewLike);
            this.progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        }

        public String toString() {
            return super.toString() + " '" + this.textViewWorkoutName.getText() + "'";
        }
    }

    public WorkoutListAdapter(Context context, OnListFragmentInteractionListener listClickListener, RequestManager requestManager) {
        this.context = context;
        this.listClickListener = listClickListener;
        this.requestManager = requestManager;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.workout_list_row, parent, false);
        return new ViewHolder(view);
    }

    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (getItemViewType(position) == 0) {
            final Workout workout = (Workout) this.rowItemList.get(position);
            holder.textViewWorkoutName.setText(workout.getWorkout_name());
            String bodyPartImageUrl = workout.getWorkout_image_url();
            final String workoutId = workout.getWorkout_id();
            if (DatabaseManager.getInstance(this.context).isFavouriteWorkout(workoutId)) {
                holder.imageViewLike.setImageResource(drawable.star_selected);
            } else {
                holder.imageViewLike.setImageResource(drawable.star);
            }
            if (AppUtil.isEmpty(bodyPartImageUrl)) {
                int resID = 0;
                try {
                    resID = drawable.class.getField(workout.getWorkout_image_name()).getInt(null);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (NoSuchFieldException e2) {
                    e2.printStackTrace();
                }
                Log.e("RESID = ", "" + resID);
                if (resID != 0) {
                    holder.progressBar.setVisibility(View.VISIBLE);
                    if ((this.context instanceof TrainingDetailActivity) && !((TrainingDetailActivity) this.context).isFinishing()) {
                        this.requestManager.load(Integer.valueOf(resID)).crossFade().centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).listener(new RequestListener<Integer, GlideDrawable>() {
                            public boolean onException(Exception e, Integer model, Target<GlideDrawable> target, boolean isFirstResource) {
                                holder.progressBar.setVisibility(View.GONE);
                                return false;
                            }

                            public boolean onResourceReady(GlideDrawable resource, Integer model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                holder.progressBar.setVisibility(View.GONE);
                                return false;
                            }
                        }).into(holder.imageViewWorkout);
                    }
                }
            } else {
                holder.progressBar.setVisibility(View.VISIBLE);
                if ((this.context instanceof TrainingDetailActivity) && !((TrainingDetailActivity) this.context).isFinishing()) {
                    this.requestManager.load(bodyPartImageUrl).centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).listener(new RequestListener<String, GlideDrawable>() {
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            holder.progressBar.setVisibility(View.GONE);
                            return false;
                        }

                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            holder.progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    }).into(holder.imageViewWorkout);
                }
            }
            holder.mView.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    if (WorkoutListAdapter.this.listClickListener != null) {
                        WorkoutListAdapter.this.listClickListener.onListFragmentInteraction(workout, position);
                    }
                }
            });
            holder.imageViewLike.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    DatabaseManager databaseManager = DatabaseManager.getInstance(WorkoutListAdapter.this.context);
                    if (databaseManager.isFavouriteWorkout(workoutId)) {
                        databaseManager.removeWorkoutFromFavourite(workoutId);
                        holder.imageViewLike.setImageResource(drawable.star);
                        return;
                    }
                    databaseManager.addWorkout(workout);
                    holder.imageViewLike.setImageResource(drawable.star_selected);
                }
            });
            return;
        }
    }

    public int getItemCount() {
        if (this.rowItemList != null) {
            return this.rowItemList.size();
        }
        return 0;
    }

    public int getItemViewType(int position) {
        if (((RowItem) this.rowItemList.get(position)) instanceof Workout) {
            return 0;
        }
        return 0;
    }





    public void setRowItemList(List<RowItem> rowItemList) {
        this.rowItemList = rowItemList;
        notifyDataSetChanged();
    }
}
