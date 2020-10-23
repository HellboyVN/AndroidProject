package hb.me.homeworkout.gym.buttlegs.buttlegspro.preview;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import hb.me.homeworkout.gym.buttlegs.buttlegspro.ads.listener.IAdCardClicked;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.home.cards.AbstractCard;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.models.Exercise;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.preview.cards.PreviewExerciseCard;

public class LevelPreviewRA extends Adapter<ViewHolder> implements ItemTouchHelperAdapter {
    private IAdCardClicked iAdCardClicked;
    private Context mContext;
    private List<Exercise> mItems;

    public LevelPreviewRA(Context mContext, List<Exercise> exerciseList, IAdCardClicked iAdCardClicked) {
        this.mContext = mContext;
        this.mItems = exerciseList;
        this.iAdCardClicked = iAdCardClicked;
    }

    public int getItemViewType(int position) {
        return ((Exercise) this.mItems.get(position)).getViewType();
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
//            case 4:
//                return new AdCard(this.mContext, parent);
            default:
                return new PreviewExerciseCard(this.mContext, parent);
        }
    }

    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Exercise exercise = (Exercise) this.mItems.get(position);
        AbstractCard card = (AbstractCard) viewHolder;
        card.setiAdCardClicked(this.iAdCardClicked);
        card.bind(exercise);
    }

    public int getItemCount() {
        return this.mItems.size();
    }

    public void removeAt(Exercise levelData) {
        int position = this.mItems.indexOf(levelData);
        this.mItems.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, this.mItems.size());
    }

    public void removeAds() {
        Iterator<Exercise> it = this.mItems.iterator();
        while (it.hasNext()) {
            if (((Exercise) it.next()).getViewType() == 4) {
                it.remove();
            }
        }
        notifyDataSetChanged();
    }

    public boolean onItemMove(int fromPosition, int toPosition) {
        int i;
        if (fromPosition < toPosition) {
            for (i = fromPosition; i < toPosition; i++) {
                Collections.swap(this.mItems, i, i + 1);
            }
        } else {
            for (i = fromPosition; i > toPosition; i--) {
                Collections.swap(this.mItems, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }
}
