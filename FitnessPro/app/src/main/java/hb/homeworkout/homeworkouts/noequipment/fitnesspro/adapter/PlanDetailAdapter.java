package hb.homeworkout.homeworkouts.noequipment.fitnesspro.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import hb.homeworkout.homeworkouts.noequipment.fitnesspro.R;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.database.DatabaseHelper;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.listener.OnListFragmentInteractionListener;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.modal.Plan;

import java.util.List;

public class PlanDetailAdapter extends Adapter<PlanDetailAdapter.ViewHolder> {
    private Context context;
    private final OnListFragmentInteractionListener listClickListener;
    private final List<Plan> planList;

    public class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
        private final ImageView imageViewDay;
        private final View mView;
        private Plan plan;
        private final TextView textViewDayName;
        private final TextView textViewExerciseCount;

        ViewHolder(View view) {
            super(view);
            this.mView = view;
            this.textViewDayName = (TextView) view.findViewById(R.id.textViewDayName);
            this.textViewExerciseCount = (TextView) view.findViewById(R.id.textViewExerciseCount);
            this.imageViewDay = (ImageView) view.findViewById(R.id.imageViewDay);
        }

        public String toString() {
            return super.toString() + " '" + this.textViewDayName.getText() + "'";
        }
    }

    public PlanDetailAdapter(Context context, List<Plan> planList, OnListFragmentInteractionListener listClickListener) {
        this.context = context;
        this.planList = planList;
        this.listClickListener = listClickListener;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.plan_detail_list_row, parent, false));
    }

    public void onBindViewHolder(ViewHolder holder, final int position) {
        boolean isMyPlan;
        String exerciseCountText;
        final Plan plan = (Plan) this.planList.get(position);
        String day = plan.getDay();
        String exerciseCount = "0";
        if (plan.getPlan_type().equals("mine")) {
            exerciseCount = DatabaseHelper.getInstance(this.context).getMyPlanExercisesCount(plan);
        } else {
            exerciseCount = DatabaseHelper.getInstance(this.context).getExercisesCount(plan);
        }
        if (plan.isMyPlan()) {
            isMyPlan = true;
        } else {
            isMyPlan = false;
        }
        if (isMyPlan || !exerciseCount.equals("0")) {
            exerciseCountText = exerciseCount + " exercises";
        } else {
            exerciseCountText = "Rest";
        }
        holder.textViewDayName.setText(day);
        holder.textViewExerciseCount.setText(exerciseCountText);
        holder.imageViewDay.setImageResource(this.context.getResources().getIdentifier(day.toLowerCase(), "drawable", this.context.getPackageName()));
        holder.mView.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (PlanDetailAdapter.this.listClickListener != null) {
                    PlanDetailAdapter.this.listClickListener.onListFragmentInteraction(plan, position);
                }
            }
        });
        if (holder.textViewExerciseCount.getText().toString().equals("Rest")) {
            holder.mView.setOnClickListener(null);
        }
    }

    public int getItemCount() {
        return this.planList.size();
    }
}
