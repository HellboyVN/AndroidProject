package hb.homeworkout.homeworkouts.noequipment.fitnesspro.adapter;

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
import android.widget.ProgressBar;
import android.widget.TextView;

import hb.homeworkout.homeworkouts.noequipment.fitnesspro.R;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.listener.OnListFragmentInteractionListener;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.modal.More;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.modal.Training;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.util.AppUtil;

import java.util.ArrayList;
import java.util.List;

public class MoreAdapter extends Adapter<MoreAdapter.ViewHolder> {
    private Context context;
    private final OnListFragmentInteractionListener listClickListener;
    private List<More> moreList;

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

    public MoreAdapter(Context context, List<More> moreList, OnListFragmentInteractionListener listClickListener) {
        this.context = context;
        this.moreList = moreList;
        this.listClickListener = listClickListener;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.favourite_workout_list_row, parent, false));
    }

    public void onBindViewHolder(ViewHolder holder, final int position) {
        final More more = (More) this.moreList.get(position);
        String name = more.getName();
        String imageUrl = more.getImage_url();
        holder.textViewWorkoutName.setText(name);
        if (VERSION.SDK_INT < 21 && holder.progressBar.getIndeterminateDrawable() != null) {
            holder.progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this.context, R.color.colorAccent), Mode.SRC_IN);
        }
        if (AppUtil.isEmpty(imageUrl)) {
            holder.progressBar.setVisibility(View.VISIBLE);
            Resources res = this.context.getResources();
            String mDrawableName = more.getImage_name();
            if (AppUtil.isEmpty(mDrawableName)) {
                holder.progressBar.setVisibility(View.GONE);
            } else {
                res.getIdentifier(mDrawableName, "drawable", this.context.getPackageName());
            }
        } else {
            holder.progressBar.setVisibility(View.VISIBLE);
        }
        holder.mView.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (MoreAdapter.this.listClickListener != null) {
                    MoreAdapter.this.listClickListener.onListFragmentInteraction(more, position);
                }
            }
        });
    }

    public int getItemCount() {
        return this.moreList.size();
    }

    public void setFilter(List<More> countryModels) {
        this.moreList = new ArrayList();
        this.moreList.addAll(countryModels);
        notifyDataSetChanged();
    }
}
