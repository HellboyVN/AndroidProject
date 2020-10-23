package com.workout.workout.adapter;

import android.content.Context;
import android.graphics.PorterDuff.Mode;
import android.os.Build.VERSION;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.NativeExpressAdView;
import com.workout.workout.R;
import com.workout.workout.constant.AppConstants;
import com.workout.workout.fragment.TrainingFragment;
import com.workout.workout.listener.OnListFragmentInteractionListener;
import com.workout.workout.managers.NativeAdsTaskManager;
import com.workout.workout.modal.RowItem;
import com.workout.workout.modal.Training;
import com.workout.workout.util.AppUtil;

import java.util.List;

public class TrainingRecyclerViewAdapter extends Adapter<TrainingRecyclerViewAdapter.ViewHolder> {
    public static final int VIEW_TYPE_NATIVE_APP = 1;
    public static final int VIEW_TYPE_WORKOUT = 0;
    private Context context;
    private final OnListFragmentInteractionListener listClickListener;
    private List<RowItem> rowItemList;
    private TrainingFragment trainingFragment;

    public class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
        public final ImageView imageViewBodyPart;
        public final View mView;
        private final ProgressBar progressBar;
        public final TextView textViewTrainingName;
        public Training training;

        public ViewHolder(View view) {
            super(view);
            this.mView = view;
            this.textViewTrainingName = (TextView) view.findViewById(R.id.textViewTrainingName);
            this.imageViewBodyPart = (ImageView) view.findViewById(R.id.imageViewBodyPart);
            this.progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        }

        public String toString() {
            return super.toString() + " '" + this.textViewTrainingName.getText() + "'";
        }
    }

    public TrainingRecyclerViewAdapter(Context context, OnListFragmentInteractionListener listClickListener, TrainingFragment trainingFragment) {
        this.context = context;
        this.listClickListener = listClickListener;
        this.trainingFragment = trainingFragment;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == 0) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.training_row, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.native_ad_view_container, parent, false);
            ((LinearLayout) view.findViewById(R.id.adContainer)).addView(prepareNativeAdView());
        }
        return new ViewHolder(view);
    }

    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (getItemViewType(position) == 0) {
            final Training training = (Training) this.rowItemList.get(position);
            String bodyPartName = training.getPart_name();
            String bodyPartImageUrl = training.getImage_url();
            holder.textViewTrainingName.setText(bodyPartName);
            if (VERSION.SDK_INT < 21 && holder.progressBar.getIndeterminateDrawable() != null) {
                holder.progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this.context, R.color.colorAccent), Mode.SRC_IN);
            }
            if (AppUtil.isEmpty(bodyPartImageUrl)) {
                holder.progressBar.setVisibility(View.VISIBLE);
                Glide.with(this.context).load(Integer.valueOf(this.context.getResources().getIdentifier(training.getPart_image_name(), "drawable", this.context.getPackageName()))).crossFade().centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).listener(new RequestListener<Integer, GlideDrawable>() {
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
            }
            holder.mView.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    if (TrainingRecyclerViewAdapter.this.listClickListener != null) {
                        TrainingRecyclerViewAdapter.this.listClickListener.onListFragmentInteraction(training, position);
                    }
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

    public void setRowItemList(List<RowItem> rowItemList) {
        this.rowItemList = rowItemList;
        notifyDataSetChanged();
    }

    public int getItemViewType(int position) {
        if (((RowItem) this.rowItemList.get(position)) instanceof Training) {
            return 0;
        }
        return 1;
    }

    private NativeExpressAdView prepareNativeAdView() {
        NativeExpressAdView nativeExpressAdView = new NativeExpressAdView(this.context);
        nativeExpressAdView.setId(R.id.native_ads_id);
        nativeExpressAdView.setAdSize(new AdSize(this.trainingFragment.getAvailableWidthForNativeAds(), this.trainingFragment.getAvailableHeightForNativeAds()));
        nativeExpressAdView.setAdUnitId(AppConstants.ADMOB_BODY_PART_NATIVE_AD_ID);
        return nativeExpressAdView;
    }

    public static void loadNativeAd(final View view) {
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
    }
}
