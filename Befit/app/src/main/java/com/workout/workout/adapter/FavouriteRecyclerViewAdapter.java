package com.workout.workout.adapter;

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
import com.workout.workout.R;
import com.workout.workout.database.DatabaseManager;
import com.workout.workout.fragment.FavouriteFragment;
import com.workout.workout.listener.OnListFragmentInteractionListener;
import com.workout.workout.modal.Workout;
import com.workout.workout.util.AppUtil;

import java.util.List;

public class FavouriteRecyclerViewAdapter extends Adapter<FavouriteRecyclerViewAdapter.ViewHolder> {
    private FavouriteFragment favouriteFragment;
    private final OnListFragmentInteractionListener listClickListener;
    private final List<Workout> workoutList;

    class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
        public final ImageView imageViewDelete;
        public final ImageView imageViewWorkout;
        final View mView;
        private final ProgressBar progressBar;
        public final TextView textViewBodyPartName;
        public final TextView textViewWorkoutName;
        public Workout workout;

        ViewHolder(View view) {
            super(view);
            this.mView = view;
            this.textViewWorkoutName = (TextView) view.findViewById(R.id.textViewWorkoutName);
            this.textViewBodyPartName = (TextView) view.findViewById(R.id.textViewBodyPartName);
            this.imageViewWorkout = (ImageView) view.findViewById(R.id.imageViewWorkout);
            this.progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
            this.imageViewDelete = (ImageView) view.findViewById(R.id.imageViewDelete);
        }

        public String toString() {
            return super.toString() + " '" + this.textViewWorkoutName.getText() + "'";
        }
    }

    public FavouriteRecyclerViewAdapter(FavouriteFragment favouriteFragment, List<Workout> workoutList, OnListFragmentInteractionListener listClickListener) {
        this.favouriteFragment = favouriteFragment;
        this.workoutList = workoutList;
        this.listClickListener = listClickListener;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.favourite_workout_list_row, parent, false));
    }

    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Workout workout = (Workout) this.workoutList.get(position);
        String bodyPartImageUrl = workout.getWorkout_image_url();
        holder.textViewWorkoutName.setText(workout.getWorkout_name());
        holder.textViewBodyPartName.setText(workout.getBody_part_name());
        holder.textViewBodyPartName.setVisibility(View.GONE);
        holder.imageViewDelete.setVisibility(View.VISIBLE);
        if (AppUtil.isEmpty(bodyPartImageUrl)) {
            int resID = this.favouriteFragment.getContext().getResources().getIdentifier(workout.getWorkout_image_name(), "drawable", this.favouriteFragment.getContext().getPackageName());
            if (resID != 0) {
                Glide.with(this.favouriteFragment.getContext()).load(Integer.valueOf(resID)).crossFade().centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).listener(new RequestListener<Integer, GlideDrawable>() {
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
            holder.progressBar.setVisibility(View.VISIBLE);
            Glide.with(this.favouriteFragment.getContext()).load(bodyPartImageUrl).centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).listener(new RequestListener<String, GlideDrawable>() {
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
                if (FavouriteRecyclerViewAdapter.this.listClickListener != null) {
                    FavouriteRecyclerViewAdapter.this.listClickListener.onListFragmentInteraction(workout, position);
                }
            }
        });
        holder.imageViewDelete.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                DatabaseManager.getInstance(FavouriteRecyclerViewAdapter.this.favouriteFragment.getContext()).removeWorkoutFromFavourite(workout.getWorkout_id());
                FavouriteRecyclerViewAdapter.this.workoutList.remove(position);
                FavouriteRecyclerViewAdapter.this.notifyDataSetChanged();
            }
        });
    }

    public int getItemCount() {
        if (this.favouriteFragment != null) {
            if (this.workoutList.size() > 0) {
                this.favouriteFragment.hideEmptyView();
            } else {
                this.favouriteFragment.showEmptyView();
            }
        }
        return this.workoutList.size();
    }
}
