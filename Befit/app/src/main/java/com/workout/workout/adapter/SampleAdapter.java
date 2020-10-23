package com.workout.workout.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
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
import com.workout.workout.activity.PlanDescriptionActivity;
import com.workout.workout.listener.OnListFragmentInteractionListener;
import com.workout.workout.modal.Plan;
import com.workout.workout.modal.PlanCategory;
import com.workout.workout.util.AppUtil;

import java.util.ArrayList;
import java.util.List;

public class SampleAdapter  extends StickyHeaderGridAdapter {
    private Context context;
    private final OnListFragmentInteractionListener listClickListener;
    private List<PlanCategory> planCategoryList = new ArrayList();


    public static class MyHeaderViewHolder extends HeaderViewHolder {
        TextView labelView;

        MyHeaderViewHolder(View itemView) {
            super(itemView);
            this.labelView = (TextView) itemView.findViewById(R.id.label);
        }
    }

    public static class MyItemViewHolder extends ItemViewHolder {
        ImageView imageViewDescription;
        ImageView imageViewLock;
        ImageView imageViewPlan;
        ImageView imageViewfree;
        TextView labelView;
        ProgressBar progressBar;

        MyItemViewHolder(View itemView) {
            super(itemView);
            this.labelView = (TextView) itemView.findViewById(R.id.label);
            this.imageViewPlan = (ImageView) itemView.findViewById(R.id.imageViewPlan);
            this.progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
            this.imageViewLock = (ImageView) itemView.findViewById(R.id.imageViewLock);
            this.imageViewDescription = (ImageView) itemView.findViewById(R.id.imageViewDescription);
            this.imageViewfree = (ImageView) itemView.findViewById(R.id.imageViewfree);
        }
    }

    public SampleAdapter(Context context, List<PlanCategory> planCategoryList, OnListFragmentInteractionListener listClickListener) {
        this.context = context;
        this.planCategoryList = planCategoryList;
        this.listClickListener = listClickListener;
    }

    public int getSectionCount() {
        return this.planCategoryList.size();
    }

    public int getSectionItemCount(int section) {
        return ((PlanCategory) this.planCategoryList.get(section)).getPlansList().size();
    }

    public HeaderViewHolder onCreateHeaderViewHolder(ViewGroup parent, int headerType) {
        return new MyHeaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_header, parent, false));
    }

    public ItemViewHolder onCreateItemViewHolder(ViewGroup parent, int itemType) {
        return new MyItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_item, parent, false));
    }

    public void onBindHeaderViewHolder(HeaderViewHolder viewHolder, int section) {
        MyHeaderViewHolder holder = (MyHeaderViewHolder) viewHolder;
        holder.labelView.setText(((PlanCategory) this.planCategoryList.get(section)).getPlan_category_name());
    }

    public void onBindItemViewHolder(ItemViewHolder viewHolder, int section, final int position) {
        final MyItemViewHolder holder = (MyItemViewHolder) viewHolder;
        PlanCategory planCategory = (PlanCategory) this.planCategoryList.get(section);
        final Plan plans = (Plan) planCategory.getPlansList().get(position);
        final Plan plan = new Plan();
        plan.setPlan_image_url(plans.getPlan_image_url());
        plan.setPlan_image_name(plans.getPlan_image_name());
        plan.setPlan_id(plans.getPlan_id());
        plan.setPlan_type(plans.getPlan_type());
        plan.setPlan_description(plans.getPlan_description());
        plan.setPlan_name(plans.getPlan_name());
        plan.setPlan_image_url(plans.getPlan_image_url());
        plan.setLocked(plans.isLocked());
        plan.setPlan_image_name(plans.getPlan_image_name());
        holder.labelView.setText(((Plan) planCategory.getPlansList().get(position)).getPlan_name());
        String planImageUrl = plan.getPlan_image_url();
        if (AppUtil.isEmpty(planImageUrl)) {
            holder.progressBar.setVisibility(View.VISIBLE);
            Resources res = this.context.getResources();
            String mDrawableName = plan.getPlan_image_name();
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
                    }).into(holder.imageViewPlan);
                } else {
                    holder.progressBar.setVisibility(View.GONE);
                }
            }
        } else {
            holder.progressBar.setVisibility(View.VISIBLE);
            Glide.with(this.context).load(planImageUrl).centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).listener(new RequestListener<String, GlideDrawable>() {
                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                    holder.progressBar.setVisibility(View.GONE);
                    return false;
                }

                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                    holder.progressBar.setVisibility(View.GONE);
                    return false;
                }
            }).into(holder.imageViewPlan);
        }
        holder.labelView.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SampleAdapter.this.listClickListener.onListFragmentInteraction(plans, position);
            }
        });
        holder.imageViewDescription.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent intent1 = new Intent(SampleAdapter.this.context, PlanDescriptionActivity.class);
                intent1.putExtra("PLAN_OBJECT", plan);
                SampleAdapter.this.context.startActivity(intent1);
            }
        });
        holder.imageViewPlan.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SampleAdapter.this.listClickListener.onListFragmentInteraction(plans, position);
            }
        });
        if (plan.getPlan_type().equalsIgnoreCase("free")) {
            holder.imageViewfree.setImageResource(R.drawable.free);
            holder.imageViewfree.setVisibility(View.VISIBLE);
            holder.imageViewLock.setVisibility(View.GONE);
        } else if (plan.isLocked()) {
            holder.imageViewLock.setVisibility(View.VISIBLE);
            holder.imageViewLock.setImageResource(R.drawable.lock);
            holder.imageViewfree.setVisibility(View.GONE);
        }
        if (!plan.isLocked() && !plan.getPlan_type().equalsIgnoreCase("free")) {
            holder.imageViewfree.setVisibility(View.GONE);
            holder.imageViewLock.setVisibility(View.GONE);
        }
    }
}
