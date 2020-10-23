package hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.home.tabs.tabtwo;

import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.home.tabs.tabtwo.othercards.ExerciseTimeItemCard;
import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.models.Exercise;
import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.ViewGroup;
import java.util.List;

public class ExTimeRA extends Adapter<ViewHolder> {
    private List<Exercise> exerciseList;
    private Context mContext;

    public ExTimeRA(Context mContext, List<Exercise> exerciseList) {
        this.mContext = mContext;
        this.exerciseList = exerciseList;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ExerciseTimeItemCard(this.mContext, parent);
    }

    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        ((ExerciseTimeItemCard) viewHolder).bind((Exercise) this.exerciseList.get(position));
    }

    public int getItemCount() {
        return this.exerciseList.size();
    }
}
