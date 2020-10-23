package com.workout.workout.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.NativeExpressAdView;
import com.workout.workout.R;
import com.workout.workout.R.drawable;
import com.workout.workout.activity.TrainingDetailActivity;
import com.workout.workout.constant.AppConstants;
import com.workout.workout.database.DatabaseManager;
import com.workout.workout.listener.OnListFragmentInteractionListener;
import com.workout.workout.managers.CachingManager;
import com.workout.workout.managers.NativeAdsTaskManager;
import com.workout.workout.managers.NetworkManager;
import com.workout.workout.modal.RowItem;
import com.workout.workout.modal.Training;
import com.workout.workout.modal.Workout;
import com.workout.workout.util.AppUtil;

import java.util.List;

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
        if (viewType == 0) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.workout_list_row, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.native_ad_view_container, parent, false);
            ((LinearLayout) view.findViewById(R.id.adContainer)).addView(prepareNativeAdView());
        }
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
        loadNativeAd(holder.itemView);
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
        return 1;
    }

    private NativeExpressAdView prepareNativeAdView() {
        NativeExpressAdView nativeExpressAdView = new NativeExpressAdView(this.context);
        nativeExpressAdView.setId(R.id.native_ads_id);
        nativeExpressAdView.setAdSize(new AdSize(((TrainingDetailActivity) this.context).getAvailableWidthForNativeAds(), ((TrainingDetailActivity) this.context).getAvailableHeightForNativeAds()));
        nativeExpressAdView.setAdUnitId(AppConstants.ADMOB_WORKOUT_LIST_NATIVE_AD_ID);
        return nativeExpressAdView;
    }

    public static void loadNativeAd(final View view) {
        if (NetworkManager.isNetworkAvailable(CachingManager.getAppContext())) {
            view.setVisibility(View.GONE);
            final NativeExpressAdView nativeExpressAdView = (NativeExpressAdView) view.findViewById(R.id.native_ads_id);
            nativeExpressAdView.setVisibility(View.GONE);
            nativeExpressAdView.loadAd(NativeAdsTaskManager.getNativeAdsRequest());
            nativeExpressAdView.setAdListener(new AdListener() {
                public void onAdLoaded() {
                    super.onAdLoaded();
                    nativeExpressAdView.setVisibility(View.VISIBLE);
                    view.setVisibility(View.VISIBLE);
                }
            });
            return;
        }
        view.setVisibility(View.GONE);
    }

    public void setRowItemList(List<RowItem> rowItemList) {
        this.rowItemList = rowItemList;
        notifyDataSetChanged();
    }
}
