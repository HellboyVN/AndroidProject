package hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.custom;

import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.custom.cards.CustomExAbstractCard;
import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.custom.cards.CustomExLevelCard;
import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.custom.cards.CustomExSavedCard;
import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.models.Exercise;
import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.ViewGroup;
import java.util.List;

public class LCustomRA extends Adapter<ViewHolder> {
    private static final int VIEW_TYPE_LABEL = 0;
    private static final int VIEW_TYPE_SAVED = 2;
    private static final int VIEW_TYPE_WORKOUT = 1;
    private List<Exercise> exerciseList;
    private IActions listener;
    private Context mContext;

    public LCustomRA(Context mContext, List<Exercise> exerciseList, IActions listener) {
        this.mContext = mContext;
        this.exerciseList = exerciseList;
        this.listener = listener;
    }

    public int getItemViewType(int position) {
        if (((Exercise) this.exerciseList.get(position)).getNameKey() == null) {
            return 2;
        }
        return 1;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CustomExAbstractCard card;
        switch (viewType) {
            case 2:
                card = new CustomExSavedCard(this.mContext, parent);
                ((CustomExSavedCard) card).setActionsListener((IPreviousWorkoutAactions) this.listener);
                return card;
            default:
                card = new CustomExLevelCard(this.mContext, parent);
                ((CustomExLevelCard) card).setDurationChangeListener((IDurationChange) this.listener);
                return card;
        }
    }

    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        ((CustomExAbstractCard) viewHolder).bind((Exercise) this.exerciseList.get(position));
    }

    public int getItemCount() {
        return this.exerciseList.size();
    }
}
