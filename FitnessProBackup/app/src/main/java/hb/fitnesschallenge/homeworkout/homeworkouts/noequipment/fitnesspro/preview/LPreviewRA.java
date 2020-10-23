package hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.preview;

import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.ads.cards.AdCard;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.ads.listener.IAdCardClicked;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.home.tabs.tabone.cards.AbstractCard;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.models.Exercise;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.preview.cards.ItemTouchHelperAdapter;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.preview.cards.PreviewLevelCard;
import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.ViewGroup;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class LPreviewRA extends Adapter<ViewHolder> implements ItemTouchHelperAdapter {
    private static final int VIEW_TYPE_LABEL = 0;
    private static final int VIEW_TYPE_WORKOUT = 1;
    private List<Exercise> exerciseList;
    private IAdCardClicked iAdCardClicked;
    private Context mContext;

    public LPreviewRA(Context mContext, List<Exercise> exerciseList, IAdCardClicked iAdCardClicked) {
        this.mContext = mContext;
        this.exerciseList = exerciseList;
        this.iAdCardClicked = iAdCardClicked;
    }

    public int getItemViewType(int position) {
        return ((Exercise) this.exerciseList.get(position)).getViewType();
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 6:
                return new AdCard(this.mContext, parent);
            default:
                return new PreviewLevelCard(this.mContext, parent);
        }
    }

    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Exercise exercise = (Exercise) this.exerciseList.get(position);
        AbstractCard card = (AbstractCard) viewHolder;
        card.setiAdCardClicked(this.iAdCardClicked);
        card.bind(exercise);
    }

    public int getItemCount() {
        return this.exerciseList.size();
    }

    public void removeAt(Exercise levelData) {
        int position = this.exerciseList.indexOf(levelData);
        this.exerciseList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, this.exerciseList.size());
    }

    public void removeAds() {
        Iterator<Exercise> it = this.exerciseList.iterator();
        while (it.hasNext()) {
            if (((Exercise) it.next()).getViewType() == 6) {
                it.remove();
            }
        }
        notifyDataSetChanged();
    }

    public boolean onItemMove(int fromPosition, int toPosition) {
        int i;
        if (fromPosition < toPosition) {
            for (i = fromPosition; i < toPosition; i++) {
                Collections.swap(this.exerciseList, i, i + 1);
            }
        } else {
            for (i = fromPosition; i > toPosition; i--) {
                Collections.swap(this.exerciseList, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }
}
