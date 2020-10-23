package com.workout.workout.adapter;

import android.content.Context;
import android.content.res.Resources;
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
import com.workout.workout.fragment.UtilityFragment;
import com.workout.workout.listener.OnListFragmentInteractionListener;
import com.workout.workout.managers.NativeAdsTaskManager;
import com.workout.workout.modal.RowItem;
import com.workout.workout.modal.Training;
import com.workout.workout.modal.Utility;
import com.workout.workout.util.AppUtil;

import java.util.List;

public class UtilityAdapter extends Adapter<UtilityAdapter.ViewHolder> {
    public static final int VIEW_TYPE_NATIVE_APP = 1;
    public static final int VIEW_TYPE_WORKOUT = 0;
    private Context context;
    private final OnListFragmentInteractionListener listClickListener;
    private List<RowItem> rowItemList;
    private UtilityFragment utilityFragment;
    private List<Utility> utilityList;

    public class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
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
            this.progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        }

        public String toString() {
            return super.toString() + " '" + this.textViewWorkoutName.getText() + "'";
        }
    }

    public UtilityAdapter(Context context, List<Utility> utilityList, OnListFragmentInteractionListener listClickListener, UtilityFragment utilityFragment) {
        this.context = context;
        this.utilityList = utilityList;
        this.listClickListener = listClickListener;
        this.utilityFragment = utilityFragment;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == 0) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.utility_list_row, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.native_ad_view_container, parent, false);
            ((LinearLayout) view.findViewById(R.id.adContainer)).addView(prepareNativeAdView());
        }
        return new ViewHolder(view);
    }

    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (getItemViewType(position) == 0) {
            final Utility utility = (Utility) this.rowItemList.get(position);
            String name = utility.getName();
            String imageUrl = utility.getImage_url();
            holder.textViewWorkoutName.setText(name);
            if (VERSION.SDK_INT < 21 && holder.progressBar.getIndeterminateDrawable() != null) {
                holder.progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this.context, R.color.colorAccent), Mode.SRC_IN);
            }
            if (AppUtil.isEmpty(imageUrl)) {
                holder.progressBar.setVisibility(View.VISIBLE);
                Resources res = this.context.getResources();
                String mDrawableName = utility.getImage_name();
                if (AppUtil.isEmpty(mDrawableName)) {
                    holder.progressBar.setVisibility(View.GONE);
                } else {
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
                }
            } else {
                holder.progressBar.setVisibility(View.VISIBLE);
                Glide.with(this.context).load(imageUrl).centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).listener(new RequestListener<String, GlideDrawable>() {
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
                    if (UtilityAdapter.this.listClickListener != null) {
                        UtilityAdapter.this.listClickListener.onListFragmentInteraction(utility, position);
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

    public int getItemViewType(int position) {
        if (((RowItem) this.rowItemList.get(position)) instanceof Utility) {
            return 0;
        }
        return 1;
    }

    public void setRowItemList(List<RowItem> rowItemList) {
        this.rowItemList = rowItemList;
        notifyDataSetChanged();
    }

    private NativeExpressAdView prepareNativeAdView() {
        NativeExpressAdView nativeExpressAdView = new NativeExpressAdView(this.context);
        nativeExpressAdView.setId(R.id.native_ads_id);
        nativeExpressAdView.setAdSize(new AdSize(this.utilityFragment.getAvailableWidthForNativeAds(), this.utilityFragment.getAvailableHeightForNativeAds()));
        nativeExpressAdView.setAdUnitId(AppConstants.ADMOB_UTILITY_NATIVE_AD_ID);
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
