package hb.homeworkout.homeworkouts.noequipment.fitnesspro.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import hb.homeworkout.homeworkouts.noequipment.fitnesspro.R;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.activity.PlanWorkoutListActivity;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.listener.OnListFragmentInteractionListener;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.modal.Plan;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.modal.RowItem;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.util.AppUtil;

public class PlanWorkoutListAdapter extends Adapter<PlanWorkoutListAdapter.ViewHolder> {
    public static final int VIEW_TYPE_NATIVE_APP = 1;
    public static final int VIEW_TYPE_WORKOUT = 0;
    private Context context;
    private final OnListFragmentInteractionListener listClickListener;
    private List<RowItem> rowItemList;

    public class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
        private final ImageView imageViewDelete;
        private final ImageView imageViewWorkout;
        private final View mView;
        private Plan plan;
        private final ProgressBar progressBar;
        private final TextView textViewSetsAndReps;
        private final TextView textViewWorkoutName;

        ViewHolder(View view) {
            super(view);
            this.mView = view;
            this.textViewWorkoutName = (TextView) view.findViewById(R.id.textViewWorkoutName);
            this.imageViewWorkout = (ImageView) view.findViewById(R.id.imageViewWorkout);
            this.textViewSetsAndReps = (TextView) view.findViewById(R.id.textViewSetsAndReps);
            this.imageViewDelete = (ImageView) view.findViewById(R.id.imageViewDelete);
            this.progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        }

        public String toString() {
            return super.toString() + " '" + this.textViewWorkoutName.getText() + "'";
        }
    }

    public PlanWorkoutListAdapter(Context context, OnListFragmentInteractionListener listClickListener) {
        this.context = context;
        this.listClickListener = listClickListener;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.plan_workout_list_row, parent, false);
        return new ViewHolder(view);
    }

    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (getItemViewType(position) == 0) {
            final Plan workout = (Plan) this.rowItemList.get(position);
            String workoutName = workout.getWorkout_name();
            String setsAndReps = workout.getSets_and_reps();
            String workoutImageUrl = workout.getWorkout_image_url();
            holder.textViewWorkoutName.setText(workoutName);
            holder.textViewSetsAndReps.setText(setsAndReps);
            if (workout.getPlan_type() == null || !workout.getPlan_type().equals("mine")) {
                holder.imageViewDelete.setVisibility(View.GONE);
            } else {
                holder.imageViewDelete.setVisibility(View.VISIBLE);
            }
            if (AppUtil.isEmpty(workoutImageUrl)) {
                Resources res = this.context.getResources();
                String mDrawableName = workout.getWorkout_image_name();
                if (mDrawableName != null) {
                    int resID = res.getIdentifier(mDrawableName, "drawable", this.context.getPackageName());
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
                }
            } else {
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
            }
            holder.mView.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    if (PlanWorkoutListAdapter.this.listClickListener != null) {
                        PlanWorkoutListAdapter.this.listClickListener.onListFragmentInteraction(workout, position);
                    }
                }
            });
            holder.imageViewDelete.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    ((PlanWorkoutListActivity) PlanWorkoutListAdapter.this.context).deleteWorkout(workout);
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

    public void setRowItemList(List<RowItem> rowItemList) {
        this.rowItemList = rowItemList;
        notifyDataSetChanged();
    }

    public int getItemViewType(int position) {
        if (((RowItem) this.rowItemList.get(position)) instanceof Plan) {
            return 0;
        }
        return 1;
    }



}
