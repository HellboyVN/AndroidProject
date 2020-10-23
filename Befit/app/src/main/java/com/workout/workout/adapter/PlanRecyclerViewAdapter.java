package com.workout.workout.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.workout.workout.R;
import com.workout.workout.activity.MyPlanListActivity;
import com.workout.workout.listener.OnListFragmentInteractionListener;
import com.workout.workout.modal.Plan;

import java.util.List;

public class PlanRecyclerViewAdapter extends Adapter<PlanRecyclerViewAdapter.ViewHolder> {
    private Context context;
    private final OnListFragmentInteractionListener listClickListener;
    private final List<Plan> planList;

    public class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
        private final ImageView imageViewDelete;
        private final View mView;
        private Plan plan;
        private final TextView textViewPlanDescription;
        private final TextView textViewPlanName;

        ViewHolder(View view) {
            super(view);
            this.mView = view;
            this.textViewPlanName = (TextView) view.findViewById(R.id.textViewPlanName);
            this.textViewPlanDescription = (TextView) view.findViewById(R.id.textViewPlanDescription);
            this.imageViewDelete = (ImageView) view.findViewById(R.id.imageViewDelete);
        }

        public String toString() {
            return super.toString() + " '" + this.textViewPlanName.getText() + "'";
        }
    }

    public PlanRecyclerViewAdapter(Context context, List<Plan> planList, OnListFragmentInteractionListener listClickListener) {
        this.context = context;
        this.planList = planList;
        this.listClickListener = listClickListener;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.plan_list_row, parent, false));
    }

    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Plan plan = (Plan) this.planList.get(position);
        holder.textViewPlanName.setText(plan.getPlan_name());
        holder.textViewPlanDescription.setText(plan.getPlan_description());
        if (this.context instanceof MyPlanListActivity) {
            holder.imageViewDelete.setVisibility(View.VISIBLE);
        } else {
            holder.imageViewDelete.setVisibility(View.GONE);
        }
        holder.mView.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (PlanRecyclerViewAdapter.this.listClickListener != null) {
                    PlanRecyclerViewAdapter.this.listClickListener.onListFragmentInteraction(plan, position);
                }
            }
        });
        holder.imageViewDelete.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ((MyPlanListActivity) PlanRecyclerViewAdapter.this.context).deletePlan(plan);
            }
        });
    }

    public int getItemCount() {
        return this.planList.size();
    }
}
